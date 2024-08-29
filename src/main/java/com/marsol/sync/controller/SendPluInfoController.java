package com.marsol.sync.controller;

import com.marsol.sync.app.ConfigLoader;
import com.marsol.sync.model.Infonut;
import com.marsol.sync.model.Item;
import com.marsol.sync.model.Scale;
import com.marsol.sync.service.api.ApiService;
import com.marsol.sync.service.api.AuthService;
import com.marsol.sync.service.api.InfonutService;
import com.marsol.sync.service.api.ProductService;
import com.marsol.sync.service.communication.SyncDataLoader;
import com.marsol.sync.service.communication.SyncSDKDefine;
import com.marsol.sync.service.communication.SyncSDKImpl;
import com.marsol.sync.service.communication.TSDKOnProgressEvent;
import com.marsol.sync.service.transform.TransformWalmartPLUs;
import com.marsol.sync.utils.GlobalStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class SendPluInfoController {
    private static final Logger logger = LoggerFactory.getLogger(ScalesNetworkController.class);
    private final RestTemplate restTemplate;
    private final AuthService authService;
    private final InfonutService infonutService;
    private final ProductService productService;
    private final ConfigLoader configLoader;

    private final PriorityQueue<Scale> scalesQueue = GlobalStore.getInstance().getScalesQueue();
    private final HashMap<Integer, LocalDateTime> scaleMap = GlobalStore.getInstance().getScaleMap();
    private final TransformWalmartPLUs transformWalmartPLUs;
    private final ReentrantLock lock = new ReentrantLock();
    private final SyncDataLoader syncData;
    @Autowired
    private ThreadPoolTaskScheduler dataProcessingThreadPoolTaskScheduler;

    @Value("${directory.pendings}")
    String directoryPendings;
    @Autowired
    public SendPluInfoController(RestTemplate restTemplate,
                                 AuthService authService,
                                 InfonutService infonutService,
                                 ProductService productService,
                                 ConfigLoader configLoader, TransformWalmartPLUs transformWalmartPLUs){
        this.restTemplate = restTemplate;
        this.authService = authService;
        this.infonutService = infonutService;
        this.productService = productService;
        this.configLoader = configLoader;
        this.transformWalmartPLUs = transformWalmartPLUs;
        this.syncData = new SyncDataLoader();
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
                Duration duration = Duration.between(scale.getLastUpdateDateTime(),now);

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
        logger.info("Realizando carga de datos a la balanza ip: {} tienda: {} depto: {}", scale.getIp_Balanza(), scale.getStore(), scale.getDepartamento());
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
        logger.info("Carga de datos a balanza {} realizada.",scale.getIp_Balanza());
    }

    public void transformData(Scale scale){
        try {
            logger.debug("Iniciando creación de documentos Notas y PLU en balanza {}",scale.getIp_Balanza());
            transformWalmartPLUs.setProductService(productService);
            transformWalmartPLUs.setInfonutService(infonutService);
            transformWalmartPLUs.transformDataPLUs(scale);
            transformWalmartPLUs.transformDataNotes(scale);
            logger.debug("Documentos creados para balanza {}",scale.getIp_Balanza());
        } catch (Exception e) {
            logger.error("Error al cargar la balanza {} {}",scale.getIp_Balanza(),e.getMessage());
            }
    }

    public void loadScale(Scale scale){
        logger.info("Cargando archivos a balanza {}",scale.getIp_Balanza());
        String pluFile = String.format("%splu_%s_%s.txt",directoryPendings,scale.getStore(),scale.getDepartamento());
        String note1File = directoryPendings+"Note1_"+scale.getStore()+"_"+scale.getDepartamento()+".txt";
        String note2File = directoryPendings+"Note2_"+scale.getStore()+"_"+scale.getDepartamento()+".txt";
        String note3File = directoryPendings+"Note3_"+scale.getStore()+"_"+scale.getDepartamento()+".txt";
        String ipString = scale.getIp_Balanza();
        syncData.loadPLU(pluFile,ipString);
        syncData.loadNotes(note1File,ipString,1);
        syncData.loadNotes(note2File,ipString,2);
        syncData.loadNotes(note3File,ipString,3);

    }
    public void deleteNotes(Scale scale){
        System.out.println("\nBorrando notas...\n");

    }
}
