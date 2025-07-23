package com.marsol.sync.domain.service;

import com.marsol.sync.application.BarCodeTransferController;
import com.marsol.sync.application.DeleteProductsController;
import com.marsol.sync.application.LabelTransfererController;
import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.infraestructure.api.AdvertisingService;
import com.marsol.sync.infraestructure.api.LogService;
import com.marsol.sync.infraestructure.api.ScaleService;
import com.marsol.sync.infraestructure.integration.SyncDataLoader;
import com.marsol.sync.model.Log;
import com.marsol.sync.utils.ConnectionTest;
import com.marsol.sync.utils.GlobalStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

@Service
public class ScaleQueueService {

    private static final Logger logger = LoggerFactory.getLogger(ScaleQueueService.class);
    private final GlobalStore globalStore = GlobalStore.getInstance();
    private final DataTransformationService dataTransformationService;
    private final DataLoadingService dataLoadingService;
    private final LabelTransfererController labelsTransferService;
    private final ImagesTransferService imagesTransferService;
    private final ScaleService scaleService;
    private final DeleteProductsController deleteProductsController;
    private final LogService logService;
    private final AdvertisingService advertisingService;

    private final SyncDataLoader syncDataLoader;

    @Autowired
    public ScaleQueueService(DataTransformationService dataTransformationService,
                             DataLoadingService dataLoadingService,
                             LabelTransfererController labelTransfererController,
                             ImagesTransferService imagesTransferService,
                             ScaleService scaleService,
                             DeleteProductsController deleteProductsController,
                             LogService logService, AdvertisingService advertisingService,
                             SyncDataLoader syncDataLoader
    ) {
        this.dataTransformationService = dataTransformationService;
        this.dataLoadingService = dataLoadingService;
        this.labelsTransferService = labelTransfererController;
        this.imagesTransferService = imagesTransferService;
        this.scaleService = scaleService;
        this.deleteProductsController = deleteProductsController;
        this.logService = logService;
        this.advertisingService = advertisingService;
        this.syncDataLoader = syncDataLoader;
    }

    /**
     * Añade una balanza a una cola de prioridad si no existe
     */
    public void addScaleToPriorityQueue(Scale scale) {
        try{
            int scaleId = scale.getId();
            LocalDateTime lastUpdate = scale.getLastUpdateDateTime();
            Map<Integer, LocalDateTime> scaleMap = globalStore.getPriorityMap();
            Queue<Scale> priorityQueue = globalStore.getPriorityQueue();

            if(!scaleMap.containsKey(scaleId) || !scaleMap.get(scaleId).equals(lastUpdate)) {
                if(!ConnectionTest.sendPingRequest(scale.getiP_Balanza())){
                    Log log = new Log(0,scale.getStore(),scale.getDepartamento(),"Procesando balanza",
                            0,scale.getiP_Balanza(),scale.getLastUpdate(),"Failure");
                    log.setStatus("0");
                    logService.createLog(log);
                    logService.updateStatus(log);
                    throw new RuntimeException("Error de conexión con la balanza -> "+scale.getiP_Balanza());
                }
                priorityQueue.add(scale);
                scaleMap.put(scaleId, lastUpdate);
                logger.info("Balanza {} añadida a cola de prioridad",scale.getiP_Balanza());
            }else {
                logger.info("Balanza {} ya existe en cola de prioridad",scale.getiP_Balanza());
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Añade una balanza a la cola de actualización forzada si aún no está presente.
     */
    public void addScaleToForcedUpdateQueue(Scale scale) {
        try{
            int scaleId = scale.getId();
            Set<Integer> scaleSet = globalStore.getForcedSet();
            Queue<Scale> forcedQueue = globalStore.getForcedScalesQueue();

            if(!scaleSet.contains(scaleId)) {
                if(!ConnectionTest.sendPingRequest(scale.getiP_Balanza())){
                    Log log = new Log(0,scale.getStore(),scale.getDepartamento(),"Procesando balanza",
                            0,scale.getiP_Balanza(),scale.getLastUpdate(),"Failure");
                    log.setStatus("0");
                    logService.createLog(log);
                    logService.updateStatus(log);
                    throw new RuntimeException("Error de conexión con la balanza -> "+scale.getiP_Balanza());
                }
                forcedQueue.add(scale);
                scaleSet.add(scaleId);
                logger.info("Balanza {} añadida a la cola de carga forzada.",scale.getiP_Balanza());
            }else{
                logger.info("Balanza {} ya está en cola de carga forzada.",scale.getiP_Balanza());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Procesa las Balanzas en la cola de prioridad y elimina las referencias del mapa.
     */
    public void processPriorityQueue() throws IOException {
        Queue<Scale> priorityQueue = globalStore.getPriorityQueue();
        Map<Integer, LocalDateTime> scaleMap = globalStore.getPriorityMap();

        while(!priorityQueue.isEmpty()) {
            Scale scale = priorityQueue.poll();
            if(scale != null) {
                logger.info("Procesando balanza: {}", scale.getiP_Balanza());
                if(!ConnectionTest.sendPingRequest(scale.getiP_Balanza())){
                    scaleMap.remove(scale.getId());
                    throw new RuntimeException("Error de conexión con la balanza -> "+scale.getiP_Balanza());
                }


                //Logica de procesamiento de la balanza...


                //Eliminación de productos obsoletos
                deleteProductsController.deleteProducts(scale);

                dataTransformationService.transformDataNotes(scale);
                dataTransformationService.transformDataPLUs(scale);

                dataLoadingService.loadNotes(scale);
                dataLoadingService.loadPlu(scale);

                //carga de publicidades!
                advertisingService.cargaPublicidad(scale);






                //Eliminar del mapa de duplicados
                scaleMap.remove(scale.getId());
                logger.debug("Balanza {} eliminada del mapa de control de duplicados.",scale.getiP_Balanza());
            }
        }
    }

    /**
     * Procesa las Balanzas en la cola de actualización forzada y elimina referencias del set.
     */
    public void processForcedUpdateQueue(){
        Queue<Scale> forcedQueue = globalStore.getForcedScalesQueue();
        Set<Integer> scaleSet = globalStore.getForcedSet();

        while(!forcedQueue.isEmpty()) {
            Scale scale = forcedQueue.poll();
            if(scale != null) {
                try{
                    logger.info("Procesando balanza con carga forzada: {}", scale.getiP_Balanza());
                    if(!ConnectionTest.sendPingRequest(scale.getiP_Balanza())){
                        scaleSet.remove(scale.getId());
                        throw new RuntimeException("Error de conexión con la balanza -> "+scale.getiP_Balanza());
                    }
                    //Logica de procesamiento de la balanza forzada...

//                    //Eliminación productos obsoletos
                    deleteProductsController.deleteProducts(scale);
                    dataTransformationService.transformDataNotes(scale);
                    dataTransformationService.transformDataPLUs(scale);

//                    //Carga de etiquetas
                    labelsTransferService.loadLabels(scale.getiP_Balanza());
//
//                    //Carga de Codigos de barra
//                    //no implementado
//                    //barCodeTransferController.loadBarCodes(scale.getiP_Balanza());
//
//                    //File f = new File("C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\Balanza HPRT\\Proyecto Walmart\\Codigos de barra avanzados\\27062025.txt");
//
//                    //NO SE HA IMPLEMENTADO ESTA CARGA AUTOMATICAMENTE, PUESTO QUE TIENE ERROR POR EL SDK!!!
//                    //LO CUAL HACE QUE AVECES SE EJECUTE CORRECTAMENTE Y OTRAS LAS HAGA INCORRECTAMENTE
//                    //POR LO TANTO, SE DEJA EN STAND-BY
//
//                    //syncDataLoader.loadAdvancedBarcodes("C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\Balanza HPRT\\Proyecto Walmart\\Codigos de barra avanzados\\27062025.txt",scale.getiP_Balanza());


                    //Cargar imágenes
                    if(scale.isEsAutoservicio() && scale.isCargaLayout()){
                        imagesTransferService.cargarLayout(scale);
                        scaleService.updateCargaLayout(scale);
                    }
//
                    //Carga PLU y Notas
                    dataLoadingService.loadNotes(scale);
                    dataLoadingService.loadPlu(scale);

                    //Carga de publicidades
                    advertisingService.cargaPublicidad(scale);

                    scaleService.updateCargaMaestra(scale);

                    //Eliminar del set de duplicados
                    scaleSet.remove(scale.getId());
                    logger.debug("Balanza {} eliminada del set de control de duplicados.",scale.getiP_Balanza());
                } catch (Exception e) {
                    logger.error("Error durante el proceso de actualización forzada de balanza {}",scale.getiP_Balanza());
                }

            }
        }
    }
}
