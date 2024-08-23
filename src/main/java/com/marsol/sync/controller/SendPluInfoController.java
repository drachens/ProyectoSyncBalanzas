package com.marsol.sync.controller;

import com.marsol.sync.app.ConfigLoader;
import com.marsol.sync.model.Infonut;
import com.marsol.sync.model.Item;
import com.marsol.sync.model.Scale;
import com.marsol.sync.service.api.ApiService;
import com.marsol.sync.service.api.AuthService;
import com.marsol.sync.service.api.InfonutService;
import com.marsol.sync.service.api.ProductService;
import com.marsol.sync.service.communication.SyncSDKDefine;
import com.marsol.sync.service.communication.SyncSDKImpl;
import com.marsol.sync.service.communication.TSDKOnProgressEvent;
import com.marsol.sync.service.transform.TransformWalmartPLUs;
import com.marsol.sync.utils.GlobalStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.PriorityQueue;

@Component
public class SendPluInfoController {
    private final RestTemplate restTemplate;
    private final AuthService authService;
    private final InfonutService infonutService;
    private final ProductService productService;
    private final ConfigLoader configLoader;

    private final PriorityQueue<Scale> scalesQueue = GlobalStore.getInstance().getScalesQueue();
    private final HashMap<Integer, LocalDateTime> scaleMap = GlobalStore.getInstance().getScaleMap();
    private final TransformWalmartPLUs transformWalmartPLUs;

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
    }

    @Scheduled(fixedRate = 7000)
    public void evaluateScales() throws InterruptedException {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Map Scale Size: " + scaleMap.size()+" - Queue Size: " + scalesQueue.size());
        while(!scalesQueue.isEmpty()){
            Scale scale = scalesQueue.peek();
            Duration duration = Duration.between(scale.getLastUpdateDateTime(),now);

            if(duration.toHours() >= 1){
                testAction(scale);
                scalesQueue.poll();
                scaleMap.remove(scale.getId());
            }else {
                break;
            }
        }
        System.out.println("Map Scale Size: " + scaleMap.size()+" - Queue Size: " + scalesQueue.size());
    }

    public void testAction(Scale scale) throws InterruptedException {
        System.out.println("Scale ID: " + scale.getId()+" Last Update: " + scale.getLastUpdate());
    }

    public void action(Scale scale) throws InterruptedException {
        transformData(scale);
        Thread.sleep(1000);
        loadScale();
        Thread.sleep(60000);
    }

    public void transformData(Scale scale){
        try {
            transformWalmartPLUs.transformDataPLUs(scale);
            transformWalmartPLUs.transformDataNotes(scale);
        } catch (Exception e) {e.printStackTrace();}
    }

    public void loadScale(){
        String pluFile = "PLU_scale.txt";
        String note1File = "nota1_scale.txt";
        String note2File = "nota2_scale.txt";
        String note3File = "nota3_scale.txt";
        System.out.println("\nCargando...");
        SyncSDKImpl sync = new SyncSDKImpl();
        SyncSDKImpl sync2 = new SyncSDKImpl();
        sync.INSTANCE.SDK_Initialize();
        int ip = SyncSDKDefine.ipToLong(configLoader.getProperty("SFTP_host"));
        String ruta_base = configLoader.getProperty("directory_pendings");

        TSDKOnProgressEvent onProgress = new TSDKOnProgressEvent() {
            @Override
            public void callback(int var1, int var2, int var3, int var4) {
                System.out.println("ErrorCode:" + var1  + " nIndex:" +var2 + " nTotal:" + var3 + " nUserDataCode:" + var4 );
            }
        };
        try{
            System.out.println("\nCargando PLU's");
            long result = sync.INSTANCE.SDK_ExecTaskA(ip, 0, 0, ruta_base+pluFile,onProgress,111);
            sync.INSTANCE.SDK_WaitForTask(result);
            System.out.println("\nPLUS Cargados.");

            //Thread.sleep(5000);

            System.out.println("\nCargando Notas1");
            long result2 = sync.INSTANCE.SDK_ExecTaskA(ip, 0, 5, ruta_base+note1File,onProgress,111);
            sync.INSTANCE.SDK_WaitForTask(result2);
            System.out.println("\nNotas1 Cargadas.");

            //Thread.sleep(10000);

            System.out.println("\nCargando Notas2");
            long result3 = sync2.INSTANCE.SDK_ExecTaskA(ip, 0, 6, ruta_base+note2File,onProgress,111);
            sync2.INSTANCE.SDK_WaitForTask(result3);
            System.out.println("\nNotas2 Cargadas.");

            //Thread.sleep(3000);

            System.out.println("\nCargando Notas3");
            long result4 = sync2.INSTANCE.SDK_ExecTaskA(ip, 0, 7, ruta_base+note3File,onProgress,112);
            sync2.INSTANCE.SDK_WaitForTask(result4);
            System.out.println("\nNotas3 Cargadas.");

        }catch(Exception e){e.printStackTrace();}
    }
}
