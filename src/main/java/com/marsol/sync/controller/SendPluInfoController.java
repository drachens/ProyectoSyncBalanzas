package com.marsol.sync.controller;

import com.marsol.sync.app.ConfigLoader;
import com.marsol.sync.model.Log;
import com.marsol.sync.model.Scale;
import com.marsol.sync.service.api.*;
import com.marsol.sync.service.communication.SyncDataLoader;
import com.marsol.sync.service.transform.TransformWalmartPLUs;
import com.marsol.sync.utils.FileUtils;
import com.marsol.sync.utils.GlobalStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class SendPluInfoController {
    private static final Logger logger = LoggerFactory.getLogger(ScalesNetworkController.class);
    private final InfonutService infonutService;
    private final ProductService productService;
    private final LayoutService layoutService;
    @Autowired
    private LogService logService;
    private final PriorityQueue<Scale> scalesQueue = GlobalStore.getInstance().getScalesQueue();
    private final HashMap<Integer, LocalDateTime> scaleMap = GlobalStore.getInstance().getScaleMap();
    private final TransformWalmartPLUs transformWalmartPLUs;
    private final ReentrantLock lock = new ReentrantLock();
    private final SyncDataLoader syncData;
    @Value("${wm.enpoint.logs.enable}")
    private boolean wmEnpointLogsEnable;
    @Value("${date.time.formatter}")
    private String dateTimeFormatter;
    private Log log;
    @Autowired
    private ThreadPoolTaskScheduler dataProcessingThreadPoolTaskScheduler;

    @Value("${directory.pendings}")
    String directoryPendings;
    @Autowired
    public SendPluInfoController(InfonutService infonutService,
                                 ProductService productService,
                                 TransformWalmartPLUs transformWalmartPLUs,
                                 LayoutService layoutService){
        this.infonutService = infonutService;
        this.productService = productService;
        this.transformWalmartPLUs = transformWalmartPLUs;
        this.syncData = new SyncDataLoader();
        this.layoutService = layoutService;
    }

    @Scheduled(fixedRateString = "${data.processing.period.milliseconds:30000}")
    public void scheduleTask(){
        logger.info("Evaluando si existen balanzas por actualizar.");
        dataProcessingThreadPoolTaskScheduler.execute(()->{
            try {
                evaluateScales();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void evaluateScales() throws InterruptedException, ExecutionException {
        LocalDateTime now = LocalDateTime.now();
        lock.lock();
        try{
            while(!scalesQueue.isEmpty()){
                Scale scale = scalesQueue.peek();
                LocalDateTime timeLastUpdate = scale.getLastUpdateDateTime();
                if(timeLastUpdate == null){
                    timeLastUpdate = now.minusHours(1);
                }
                Duration duration = Duration.between(timeLastUpdate,now);

                if(duration.toHours() >= 1){
                    action(scale);
                    scalesQueue.poll();
                    scaleMap.remove(scale.getId());
                }else {
                    break;
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void testAction(Scale scale) throws InterruptedException {
        System.out.println("Actualizar Scale ID: " + scale.getId()+" Last Update: " + scale.getLastUpdate());
    }

    public void action(Scale scale) throws InterruptedException, ExecutionException {
        logger.info("[SendPluInfoController] Realizando carga de datos a la balanza ip: {} tienda: {} depto: {}", scale.getIp_Balanza(), scale.getStore(), scale.getDepartamento());
        String ip = scale.getIp_Balanza();

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> transformData(scale))
                .thenRun(() -> loadScale(scale))
                .exceptionally(ex -> {
                   System.out.println("Error: "+ex.getMessage());
                   return null;
                });
        /*
        CompletableFuture<Void> future = CompletableFuture.runAsync(()->transformData(scale));
        future.get();
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(()->loadScale(scale));
        future2.get();
         */
        future.join();
        logger.info("[SendPluInfoController] Carga de datos a balanza {} realizada.",scale.getIp_Balanza());
    }

    public void transformData(Scale scale){
        try {
            logger.debug("[SendPluInfoController] Iniciando creación de documentos Notas y PLU en balanza {}",scale.getIp_Balanza());
            transformWalmartPLUs.setProductService(productService);
            transformWalmartPLUs.setInfonutService(infonutService);
            transformWalmartPLUs.setLayoutService(layoutService);
            transformWalmartPLUs.transformDataPLUsAsistida(scale);
            transformWalmartPLUs.transformDataNotes(scale);
            logger.debug("[SendPluInfoController] Documentos creados para balanza {}",scale.getIp_Balanza());
        } catch (Exception e) {
            logger.error("[SendPluInfoController] Error al cargar la balanza {} {}",scale.getIp_Balanza(),e.getMessage());
            }
    }

    public void loadScale(Scale scale){
        logger.info("Cargando archivos a balanza {}",scale.getIp_Balanza());
        String pluFile = String.format("%splu_%s_%s.txt",directoryPendings,scale.getStore(),scale.getDepartamento());
        logger.info("Archivo {} cargado.",pluFile);
        String note1File = directoryPendings+"Note1_"+scale.getStore()+"_"+scale.getDepartamento()+".txt";
        logger.info("Archivo {} cargado.",note1File);
        String note2File = directoryPendings+"Note2_"+scale.getStore()+"_"+scale.getDepartamento()+".txt";
        logger.info("Archivo {} cargado.",note2File);
        String note3File = directoryPendings+"Note3_"+scale.getStore()+"_"+scale.getDepartamento()+".txt";
        logger.info("Archivo {} cargado.",note3File);
        String ipString = scale.getIp_Balanza();
        LocalDateTime nowMinus2Hours = LocalDateTime.now().minusHours(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormatter);
        String dateTimeFormated = nowMinus2Hours.format(formatter);

        boolean boolPlu = syncData.loadPLU(pluFile,ipString);
        boolean boolNote1 = syncData.loadNotes(note1File,ipString,1);
        boolean boolNote2 = syncData.loadNotes(note2File,ipString,2);
        boolean boolNote3 = syncData.loadNotes(note3File,ipString,3);
        if(boolPlu){
            int datos1 = FileUtils.countLines(pluFile);
            if(wmEnpointLogsEnable){
                log = new Log(scale.getStore(),scale.getDepartamento(),"Carga de PLU's",
                        datos1,scale.getIp_Balanza(),dateTimeFormated,"Success");
                logService.createLog(log);
            }
        }
        if(boolNote1){
            int datos2 = FileUtils.countLines(note1File);
            if(wmEnpointLogsEnable){
                log = new Log(scale.getStore(),scale.getDepartamento(),"Carga de Nota 1",
                        datos2,scale.getIp_Balanza(),dateTimeFormated,"Success");
                logService.createLog(log);
            }
        }
        if(boolNote2){
            int datos3 = FileUtils.countLines(note2File);
            if(wmEnpointLogsEnable){
                log = new Log(scale.getStore(),scale.getDepartamento(),"Carga de Nota 2",
                        datos3,scale.getIp_Balanza(),dateTimeFormated,"Success");
                logService.createLog(log);
            }
        }
        if (boolNote3){
            int datos4 = FileUtils.countLines(note3File);
            if(wmEnpointLogsEnable){
                log = new Log(scale.getStore(),scale.getDepartamento(),"Carga de Nota 3",
                        datos4,scale.getIp_Balanza(),dateTimeFormated,"Success");
                logService.createLog(log);
            }
        }


    }
}
