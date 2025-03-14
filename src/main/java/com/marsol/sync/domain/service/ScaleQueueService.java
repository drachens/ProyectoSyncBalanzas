package com.marsol.sync.domain.service;

import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.infraestructure.integration.SyncDataLoader;
import com.marsol.sync.utils.GlobalStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    public ScaleQueueService(DataTransformationService dataTransformationService,
                             DataLoadingService dataLoadingService) {
        this.dataTransformationService = dataTransformationService;
        this.dataLoadingService = dataLoadingService;
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
                priorityQueue.add(scale);
                scaleMap.put(scaleId, lastUpdate);
                logger.info("Balanza {} añadida a cola de prioridad",scale.getIp_Balanza());
            }else {
                logger.info("Balanza {} ya existe en cola de prioridad",scale.getIp_Balanza());
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
                forcedQueue.add(scale);
                scaleSet.add(scaleId);
                logger.info("Balanza {} añadida a la cola de carga forzada.",scale.getIp_Balanza());
            }else{
                logger.info("Balanza {} ya está en cola de carga forzada.",scale.getIp_Balanza());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Procesa las Balanzas en la cola de prioridad y elimina las referencias del mapa.
     */
    public void processPriorityQueue(){
        Queue<Scale> priorityQueue = globalStore.getPriorityQueue();
        Map<Integer, LocalDateTime> scaleMap = globalStore.getPriorityMap();

        while(!priorityQueue.isEmpty()) {
            Scale scale = priorityQueue.poll();
            if(scale != null) {
                logger.info("Procesando balanza: {}", scale.getIp_Balanza());

                //Logica de procesamiento de la balanza...
                dataTransformationService.transformDataNotes(scale);
                dataTransformationService.transformDataPLUs(scale);

                dataLoadingService.loadNotes(scale);
                dataLoadingService.loadPlu(scale);

                //Eliminar del mapa de duplicados
                scaleMap.remove(scale.getId());
                logger.debug("Balanza {} eliminada del mapa de control de duplicados.",scale.getIp_Balanza());
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
                logger.info("Procesando balanza forzada: {}", scale.getIp_Balanza());

                //Logica de procesamiento de la balanza forzada...
                dataTransformationService.transformDataNotes(scale);
                dataTransformationService.transformDataPLUs(scale);

                dataLoadingService.loadNotes(scale);
                dataLoadingService.loadPlu(scale);


                //Eliminar del set de duplicados
                scaleSet.remove(scale.getId());
                logger.debug("Balanza {} eliminada del set de control de duplicados.",scale.getIp_Balanza());
            }
        }
    }
}
