package com.marsol.sync.application.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.domain.service.ScaleQueueService;
import com.marsol.sync.infraestructure.api.ScaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Component
public class ScalesNetworkController {
    private static final Logger logger = LoggerFactory.getLogger(ScalesNetworkController.class);
    private final ScaleService scaleService;
    private final ScaleQueueService scaleQueueService;
    //Estructuras para manejar las Scales

    @Value("${marca:HPRT-Test}")
    private String marca;
    @Value("${directory.pendings}")
    private String directoryPath;
    @Value("${lab.mode}")
    private boolean labMode;
    @Autowired
    private ThreadPoolTaskScheduler scaleNetThreadPoolTaskScheduler;

    @Autowired
    public ScalesNetworkController
            (ScaleService scaleService, ScaleQueueService scaleQueueService) {
        this.scaleService = scaleService;
        this.scaleQueueService = scaleQueueService;
    }

    @Scheduled(fixedRateString = "${scale.network.period.milliseconds:6000}")
    public void scheduleTask(){
        logger.info("Actualizando lista de balanzas");
        if(labMode){
            scaleNetThreadPoolTaskScheduler.execute(this::fetchScalesFromLab);
        }else{
            fetchScalesFromAPI(marca);
        }

    }
    @Scheduled(fixedRateString = "60000")
    public void scheduleTask2(){
        logger.info("Actualizando Set de Balanzas Actualizacion Forzada.");
        if(labMode){
            fetchScalesUpdateFromLab();
        }else {
            fetchScalesUpdateFromApi();
        }
    }

    private List<Scale> parseScalesJson(String scaleJSON) {
        Gson gson = new Gson();
        Type scalesType = new TypeToken<List<Scale>>(){}.getType();
        return gson.fromJson(scaleJSON, scalesType);
    }

    public void fetchScalesUpdateFromApi(){
        String scaleJSON = scaleService.getScalesByMarca(marca);
        if(scaleJSON == null || scaleJSON.isEmpty()){
            logger.error("No existe una lista de balanzas.");
            return;
        }

        List<Scale> scales = parseScalesJson(scaleJSON);
        for (Scale scale : scales) {
            if (scale.getIsCargaMaestra() || scale.getIsCargaLayout()) {
                scaleQueueService.addScaleToForcedUpdateQueue(scale);
                logger.debug("Balanza {} añadida a la cola de actualización forzada.", scale.getIp_Balanza());
            }
        }
    }

    public void fetchScalesFromAPI(String marca){
        String scaleJSON = scaleService.getScalesByMarca(marca);
        if (scaleJSON == null || scaleJSON.isEmpty()) {
            logger.error("[ScalesNetworkController] No se pudo obtener la lista de balanzas para la marca '{}'. La respuesta JSON es nula o vacia.", marca);
            return;
        }
        List<Scale> scales = parseScalesJson(scaleJSON);
        for (Scale scale : scales) {
            scaleQueueService.addScaleToPriorityQueue(scale);
            logger.debug("Balanza {} añadida a cola de actualizacion.", scale.getIp_Balanza());
        }
    }

    public void fetchScalesFromLab(){
        String scaleJSON = scaleService.getScalesByMarca(marca);
        if (scaleJSON == null || scaleJSON.isEmpty()) {
            logger.error("No existe una lista de balanzas.");
            return;
        }
        List<Scale> scales = parseScalesJson(scaleJSON);
        for (Scale scale : scales) {
            logger.debug("Evaluando balanza: {}",scale.getIp_Balanza());
            String ip = scale.getIp_Balanza();
            if (!ip.contains("10.105.197.")){
                logger.debug("Balanza {} no es de lab.",ip);
                //scales.remove(scale);
            }else{
                scaleQueueService.addScaleToPriorityQueue(scale);
                logger.debug("Balanza {} añadida a cola de actualizacion.", scale.getIp_Balanza());
            }
        }
    }

    public void fetchScalesUpdateFromLab(){
        String scaleJSON = scaleService.getScalesByMarca(marca);
        if(scaleJSON == null || scaleJSON.isEmpty()){
            logger.error("No existe una lista de balanzas.");
            return;
        }
        List<Scale> scales = parseScalesJson(scaleJSON);
        for (Scale scale : scales) {
            logger.debug("Evaluando balanza: {}",scale.getIp_Balanza());
            String ip = scale.getIp_Balanza();
            if (!ip.contains("10.105.197.")){
                logger.debug("Balanza {} no es de lab.",ip);
                //scales.remove(scale);
            }
            else{
                if (scale.getIsCargaMaestra() || scale.getIsCargaLayout()) {
                    scaleQueueService.addScaleToForcedUpdateQueue(scale);
                    logger.debug("Balanza {} añadida a la cola de actualización forzada.", scale.getIp_Balanza());
                }
            }
        }
    }



/*
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

 */
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
