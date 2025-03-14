package com.marsol.sync.application.controller;
import com.marsol.sync.domain.service.ScaleQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class ProcessQueuesController {
    private static final Logger logger = LoggerFactory.getLogger(ScalesNetworkController.class);
    private final ScaleQueueService scaleQueueService;

    @Autowired
    private ThreadPoolTaskScheduler dataProcessingThreadPoolTaskScheduler;

    @Autowired
    public ProcessQueuesController(ScaleQueueService scaleQueueService) {
        this.scaleQueueService = scaleQueueService;
    }

    @Scheduled(fixedRateString = "${data.processing.period.milliseconds:30000}")
    public void processQueue(){
        logger.info("Evaluando si existen balanzas por actualizar.");
        dataProcessingThreadPoolTaskScheduler.execute(()->{
            try {
                scaleQueueService.processPriorityQueue();
            } catch (Exception e) {
                logger.error("Error durante la evaluacion de balanzas: {}",e.getMessage());
            }
        });
    }
    @Scheduled(fixedRateString = "60000")
    public void processForcedQueue(){
        logger.info("Evaluando si existe balanzas que requieran una cargaLayout o cargaMaestra.");
        scaleQueueService.processForcedUpdateQueue();
    }
}
