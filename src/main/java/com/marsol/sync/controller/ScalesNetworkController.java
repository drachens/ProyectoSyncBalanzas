package com.marsol.sync.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.marsol.sync.MainClass;
import com.marsol.sync.model.Infonut;
import com.marsol.sync.model.Scale;
import com.marsol.sync.service.api.ApiService;
import com.marsol.sync.service.api.AuthService;
import com.marsol.sync.service.api.ScaleService;
import com.marsol.sync.utils.GlobalStore;
import com.marsol.sync.utils.LoggingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class ScalesNetworkController {
    private static final Logger logger = LoggerFactory.getLogger(ScalesNetworkController.class);
    private final RestTemplate restTemplate;
    private final AuthService authService;
    private final ApiService<Scale> apiService;
    private final ScaleService scaleService;
    private int periodoMinutos;


    //Estructuras para manejar las Scales
    private final PriorityQueue<Scale> scalesQueue = GlobalStore.getInstance().getScalesQueue();
    private final HashMap<Integer, LocalDateTime> scaleMap = GlobalStore.getInstance().getScaleMap();
    private final Lock queueLock = new ReentrantLock();
    @Value("${marca:HPRT}")
    private String marca;
    @Value("${directory.pendings}")
    private String directoryPath;
    @Autowired
    private ThreadPoolTaskScheduler scaleNetThreadPoolTaskScheduler;


    @Autowired
    public ScalesNetworkController(RestTemplate restTemplate,
                                   AuthService authService,
                                   ApiService<Scale> apiService,
                                   ScaleService scaleService) {
        this.restTemplate = restTemplate;
        this.authService = authService;
        this.apiService = apiService;
        this.scaleService = scaleService;
    }

    @Scheduled(fixedRateString = "${scale.network.period.milliseconds:6000}")
    public void scheduleTask(){
        logger.info("[scheduleTask] Actualizando lista de balanzas.");
        //getScalesMarca(marca);
        scaleNetThreadPoolTaskScheduler.execute(()->{
            //fetchScalesFromFile("C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\scales\\scales_test.json");
            fetchScalesFromAPI("HPRT");
        });
    }

    public void setPeriodoMinutos(int periodoMinutos) {
        long periodMilliseconds = periodoMinutos*60*1000L;
        //configLoader.setProperty("ScaleNetworkPeriodMilliseconds",String.valueOf(periodMilliseconds));
        //configLoader.saveProperty();
    }

    public int getPeriodoMinutos() {
        return periodoMinutos;
    }

    public void fetchScalesFromFile(String filepath){

        try{
            String scaleJson = new String(Files.readAllBytes(Paths.get(filepath)));
            int counter = 0;
            Gson gson = new Gson();
            Type scalesType = new TypeToken<List<Scale>>(){}.getType();
            List<Scale> scales = gson.fromJson(scaleJson, scalesType);
            for(Scale scale : scales){
                if(addScaleToQueue(scale)){
                    counter++;
                }
            }
            logger.info("Se han añadido {} balanzas a la cola.", counter);
        } catch (IOException e) {
            logger.error("Error al leer el archivo: ", e);
            throw new RuntimeException(e);
        }
    }

    public void fetchScalesFromAPI(String marca){
        String scaleJSON = scaleService.getScalesByMarca(marca);

        if (scaleJSON == null || scaleJSON.isEmpty()) {
            logger.error("[ScalesNetworkController] No se pudo obtener la lista de balanzas para la marca '" + marca + "'. La respuesta JSON es nula o vacia.");
            return;
        }
        Gson gson = new Gson();
        Type scalesType = new TypeToken<List<Scale>>(){}.getType();
        List<Scale> scales = gson.fromJson(scaleJSON, scalesType);
        for(Scale scale : scales){
            addScaleToQueue(scale);
        }

    }

    public boolean addScaleToQueue(Scale scale){
        queueLock.lock();
        try{
            int scaleId = scale.getId();
            LocalDateTime lastUpdate = scale.getLastUpdateDateTime();

            //Verificar si existe un scale con mismo id y lastUpdate
            //Objects.equals(scaleMap.get(scaleId), lastUpdate)-> devuelve true si ambos son null, devuelve false si solo 1 es null, y evalua scaleMap.get(scaleId).equals(lastUpdate) si ninguno es null
            if(!scaleMap.containsKey(scaleId) || !Objects.equals(scaleMap.get(scaleId), lastUpdate)){
                scalesQueue.add(scale);
                logger.info("Se ha agregado la Balanza con ID: "+scaleId+" a la cola de balanzas");
                scaleMap.put(scaleId,lastUpdate);//Agregar al mapa de duplicados
                return true;
            }else {
                logger.info("No se han anadido mas balanzas a la cola de balanzas.");
                return false;
            }
        }finally {
            queueLock.unlock();
        }


    }

    public void getScalesMarca(String marca){
        String scalesJSON = scaleService.getScalesByMarca(marca);
        Gson gson = new Gson();
        Type scalesType = new TypeToken<List<Scale>>(){}.getType();
        List<Scale> scales = gson.fromJson(scalesJSON, scalesType);
        //Crear directorio si no existe.
        Path path = Path.of(directoryPath);
        if(!Files.exists(path)){
            try{
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        String filePath = directoryPath+"scales.json";
        try(FileWriter fileWriter = new FileWriter(filePath)){
            fileWriter.write(scalesJSON);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
