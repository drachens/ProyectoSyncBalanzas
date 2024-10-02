package com.marsol.sync.controller;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marsol.sync.model.Item;
import com.marsol.sync.model.Layout;
import com.marsol.sync.model.Log;
import com.marsol.sync.model.Scale;
import com.marsol.sync.service.api.*;
import com.marsol.sync.service.communication.SyncDataLoader;
import com.marsol.sync.service.images.Transfer;
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


import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
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
    @Autowired
    private Transfer transfer;
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
            try{
                transformWalmartPLUs.setProductService(productService);
            }catch (Exception e){
                logger.error("Error al configurar productService para balanza: {}",scale.getIp_Balanza());
            }
            try{
                transformWalmartPLUs.setInfonutService(infonutService);
            }catch (Exception e){
                logger.error("Error al configurar infonutService para balanza: {}",scale.getIp_Balanza());
            }
            try{
                transformWalmartPLUs.setLayoutService(layoutService);
            }catch (Exception e){
                logger.error("Error al configurar layoutService para balanza: {}",scale.getIp_Balanza());
            }
            try{
                transformWalmartPLUs.transformDataPLUs(scale);
            } catch (Exception e) {
                logger.error("Error al transformar los datos de PLU's para la balanza: {}, {}",scale.getIp_Balanza(), e.getMessage());
            }
            try{
                transformWalmartPLUs.transformDataNotes(scale);
            }catch (Exception e){
                logger.error("Error al transformar los datos de Notas para la balanza: {}",scale.getIp_Balanza());
            }
            logger.debug("[SendPluInfoController] Documentos creados para balanza {}",scale.getIp_Balanza());
        } catch (Exception e) {
            logger.error("[SendPluInfoController] Error al cargar la balanza {} {}",scale.getIp_Balanza(),e.getMessage());
            }
    }

    public void loadScale(Scale scale){
        logger.info("Cargando archivos a balanza {}",scale.getIp_Balanza());
        String pluFile = String.format("%splu_%s_%s.txt",directoryPendings,scale.getStore(),scale.getDepartamento());
        String note1File = directoryPendings+"Note1_"+scale.getStore()+"_"+scale.getDepartamento()+".txt";
        String note2File = directoryPendings+"Note2_"+scale.getStore()+"_"+scale.getDepartamento()+".txt";
        String note3File = directoryPendings+"Note3_"+scale.getStore()+"_"+scale.getDepartamento()+".txt";
        String note4File = directoryPendings+"Note4_"+scale.getStore()+"_"+scale.getDepartamento()+".txt";

        String ipString = scale.getIp_Balanza();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormatter);
        String dateTimeFormated = now.format(formatter);
        if(scale.getIsEsAutoservicio() || scale.getIsEsDual()){
            logger.info("Realizando envio de imagenes para balanza de autoservicio");
            try{
                transformWalmartPLUs.setLayoutService(layoutService);
            }catch (Exception e){
                logger.error("Error al configurar layoutService para balanza: {}",scale.getIp_Balanza());
            }
            String layout_string =layoutService.getLayout(scale.getStore(),scale.getDepartamento());
            Gson gson = new Gson();
            Type type = new TypeToken<List<Layout>>(){}.getType();
            List<Layout> layouts = gson.fromJson(layout_string,type);
            transfer.cargarLayout(scale, layouts);
        }

        boolean boolPlu = syncData.loadPLU(pluFile,ipString);
        boolean boolNote1 = syncData.loadNotes(note1File,ipString,1);
        boolean boolNote2 = syncData.loadNotes(note2File,ipString,2);
        boolean boolNote3 = syncData.loadNotes(note3File,ipString,3);
        boolean boolNote4 = syncData.loadNotes(note4File,ipString,4);

        if(boolPlu){
            logger.info("Archivo {} cargado correctamente a la balanza.",pluFile);
            int datos1 = FileUtils.countLines(pluFile);
            if(wmEnpointLogsEnable){
                log = new Log(0,scale.getStore(),scale.getDepartamento(),"Carga de PLU's",
                        datos1,scale.getIp_Balanza(),dateTimeFormated,"Success");
                logService.createLog(log);
                logService.updateStatus(log);
            }
        }else{
            logger.warn("Error al cargar el archivo {}",pluFile);
        }
        if(boolNote1){
            logger.info("Archivo {} cargado correctamente a la balanza.",note1File);
            int datos2 = FileUtils.countLines(note1File);
            if(wmEnpointLogsEnable){
                log = new Log(0,scale.getStore(),scale.getDepartamento(),"Carga de Nota 1",
                        datos2,scale.getIp_Balanza(),dateTimeFormated,"Success");
                logService.createLog(log);
                logService.updateStatus(log);
            }
        }else{
            logger.warn("Error al cargar el archivo {}",note1File);
        }
        if(boolNote2){
            logger.info("Archivo {} cargado correctamente a la balanza.",note2File);
            int datos3 = FileUtils.countLines(note2File);
            if(wmEnpointLogsEnable){
                log = new Log(0,scale.getStore(),scale.getDepartamento(),"Carga de Nota 2",
                        datos3,scale.getIp_Balanza(),dateTimeFormated,"Success");
                logService.createLog(log);
                logService.updateStatus(log);
            }
        }else{
            logger.warn("Error al cargar el archivo {}",note2File);
        }
        if (boolNote3){
            logger.info("Archivo {} cargado correctamente a la balanza.",note3File);
            int datos4 = FileUtils.countLines(note3File);
            if(wmEnpointLogsEnable){
                log = new Log(0,scale.getStore(),scale.getDepartamento(),"Carga de Nota 3",
                        datos4,scale.getIp_Balanza(),dateTimeFormated,"Success");
                logService.createLog(log);
                logService.updateStatus(log);
            }
        }else{
            logger.warn("Error al cargar el archivo {}", note3File);
        }
        if(boolNote4){
            logger.info("Archivo {} cargado correctamente a la balanza.",note4File);
            int datos5 = FileUtils.countLines(note4File);
            if(wmEnpointLogsEnable){
                log = new Log(0,scale.getStore(),scale.getDepartamento(),"Carga de Nota 4",
                        datos5,scale.getIp_Balanza(),dateTimeFormated,"Success");
                logService.createLog(log);
                logService.updateStatus(log);
            }
        }else{
            logger.warn("Error al cargar el archivo {}",note4File);
        }

    }
}
