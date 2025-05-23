package com.marsol.sync.domain.service;

import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.infraestructure.api.ScaleService;
import com.marsol.sync.infraestructure.integration.SyncDataLoader;
import com.marsol.sync.utils.ConnectionTest;
import com.marsol.sync.utils.GlobalStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private final LabelsTransferService labelsTransferService;
    private final ImagesTransferService imagesTransferService;
    private final ScaleService scaleService;

    @Autowired
    public ScaleQueueService(DataTransformationService dataTransformationService,
                             DataLoadingService dataLoadingService,
                             LabelsTransferService labelsTransferService,
                             ImagesTransferService imagesTransferService,
                             ScaleService scaleService) {
        this.dataTransformationService = dataTransformationService;
        this.dataLoadingService = dataLoadingService;
        this.labelsTransferService = labelsTransferService;
        this.imagesTransferService = imagesTransferService;
        this.scaleService = scaleService;
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
                if(!ConnectionTest.sendPingRequest(scale.getIP_Balanza())){
                    throw new RuntimeException("Error de conexión con la balanza -> "+scale.getIP_Balanza());
                }
                priorityQueue.add(scale);
                scaleMap.put(scaleId, lastUpdate);
                logger.info("Balanza {} añadida a cola de prioridad",scale.getIP_Balanza());
            }else {
                logger.info("Balanza {} ya existe en cola de prioridad",scale.getIP_Balanza());
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
                if(!ConnectionTest.sendPingRequest(scale.getIP_Balanza())){
                    throw new RuntimeException("Error de conexión con la balanza -> "+scale.getIP_Balanza());
                }
                forcedQueue.add(scale);
                scaleSet.add(scaleId);
                logger.info("Balanza {} añadida a la cola de carga forzada.",scale.getIP_Balanza());
            }else{
                logger.info("Balanza {} ya está en cola de carga forzada.",scale.getIP_Balanza());
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
                logger.info("Procesando balanza: {}", scale.getIP_Balanza());
                if(!ConnectionTest.sendPingRequest(scale.getIP_Balanza())){
                    scaleMap.remove(scale.getId());
                    throw new RuntimeException("Error de conexión con la balanza -> "+scale.getIP_Balanza());
                }
                //Logica de procesamiento de la balanza...
                dataTransformationService.transformDataNotes(scale);
                dataTransformationService.transformDataPLUs(scale);

                dataLoadingService.loadNotes(scale);
                dataLoadingService.loadPlu(scale);

                //Eliminar del mapa de duplicados
                scaleMap.remove(scale.getId());
                logger.debug("Balanza {} eliminada del mapa de control de duplicados.",scale.getIP_Balanza());
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
                    logger.info("Procesando balanza con carga forzada: {}", scale.getIP_Balanza());
                    if(!ConnectionTest.sendPingRequest(scale.getIP_Balanza())){
                        scaleSet.remove(scale.getId());
                        throw new RuntimeException("Error de conexión con la balanza -> "+scale.getIP_Balanza());
                    }
                    //Logica de procesamiento de la balanza forzada...
                    dataTransformationService.transformDataNotes(scale);
                    dataTransformationService.transformDataPLUs(scale);

                    //Carga de etiquetas
                    //labelsTransferService.processLabelForScale(scale);

                    //Cargar imágenes
                    if(scale.isEsAutoservicio() && scale.isCargaLayout()){
                        imagesTransferService.cargarLayout(scale);
                        scaleService.updateCargaLayout(scale);
                    }

                    //Carga PLU y Notas
                    dataLoadingService.loadNotes(scale);
                    dataLoadingService.loadPlu(scale);

                    scaleService.updateCargaMaestra(scale);

                    //Eliminar del set de duplicados
                    scaleSet.remove(scale.getId());
                    logger.debug("Balanza {} eliminada del set de control de duplicados.",scale.getIP_Balanza());
                } catch (Exception e) {
                    logger.error("Error durante el proceso de actualización forzada de balanza {}",scale.getIP_Balanza());
                }

            }
        }
    }
}
