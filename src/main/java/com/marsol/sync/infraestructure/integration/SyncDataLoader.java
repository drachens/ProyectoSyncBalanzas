package com.marsol.sync.infraestructure.integration;

import com.marsol.sync.infraestructure.adapter.*;
import com.marsol.sync.utils.ConnectionTest;
import com.marsol.sync.utils.ProgressEventFactory;
import com.marsol.sync.utils.ProgressResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class SyncDataLoader {
    private final SyncSDKIntf sync;
    private static final Logger logger = LoggerFactory.getLogger(SyncDataLoader.class);
    public SyncDataLoader() {
        this.sync = SyncManager.getInstance();
    }

    public boolean loadPLU(String path, String ipString){
        long idTask;
        int ip = SyncSDKDefine.ipToLong(ipString);
        ProgressResult progressResult = new ProgressResult();
        TSDKOnProgressEvent onProgress = ProgressEventFactory.create("Cargando PLU",ipString,progressResult);
        try{
            sync.SDK_Initialize();
            idTask = sync.SDK_ExecTaskA(ip,0,0,path,onProgress,111);
            sync.SDK_WaitForTask(idTask);
            return progressResult.isSuccessful();
        }catch(Exception e){
            logger.error("Error durante carga de PLU: {}",e.getMessage(),e);
            return false;
        }
    }

    public boolean loadNotes(String filename, String ipString, int typeNote) {
        int ip = SyncSDKDefine.ipToLong(ipString);
        ProgressResult progressResult = new ProgressResult();
        TSDKOnProgressEvent onProgress = ProgressEventFactory.create("Cargando Nota " + typeNote, ipString, progressResult);

        // Validar que el tipo de nota sea 1 a 4
        if (typeNote < 1 || typeNote > 4) {
            logger.error("Número de nota {} no es válido. Debe estar entre 1 y 4.", typeNote);
            return false;
        }

        int sdkTaskCode = 4 + typeNote; // Nota 1 = 5, Nota 2 = 6, etc.

        try {
            Path path = Paths.get(filename);
            if(Files.exists(path)){
                sync.SDK_Initialize();
                long taskId = sync.SDK_ExecTaskA(ip, 0, sdkTaskCode, filename, onProgress, 111);
                sync.SDK_WaitForTask(taskId);
                return progressResult.isSuccessful();
            }else{
                return false;
            }
        } catch (Exception e) {
            logger.error("Error durante la carga de Nota {}: {}", typeNote, e.getMessage(), e);
            return false;
        }
    }

    public boolean loadBarcodes(String path, String ipString){
        long idTask;
        int ip = SyncSDKDefine.ipToLong(ipString);
        ProgressResult progressResult = new ProgressResult();
        TSDKOnProgressEvent onProgress = ProgressEventFactory.create("Carga de Barcodes",ipString,progressResult);
        try{
            sync.SDK_Initialize();
            idTask = sync.SDK_ExecTaskA(ip,0,4,path,onProgress,111);
            sync.SDK_WaitForTask(idTask);
            return progressResult.isSuccessful();
        }catch(Exception e){
            logger.error("Error durante la carga de Barcodes: {}",e.getMessage(),e);
            return false;
        }finally {
            sync.SDK_Finalize();
        }
    }

    public boolean loadLabelDocument(String path, String ipString){
        long idTask;
        int ip = SyncSDKDefine.ipToLong(ipString);
        ProgressResult progressResult = new ProgressResult();
        TSDKOnProgressEvent onProgress = ProgressEventFactory.create("Carga de Label Document",ipString,progressResult);
        try{
            sync.SDK_Initialize();
            idTask = sync.SDK_ExecTaskA(ip,0,8194,path,onProgress,111);
            sync.SDK_WaitForTask(idTask);
            return progressResult.isSuccessful();
        }catch(Exception e){
            logger.error("Error durante la carga de Label Document: {}",e.getMessage(),e);
            return false;
        }finally {
            sync.SDK_Finalize();
        }
    }

    public boolean loadLabelElement(String path, String ipString){
        long idTask;
        int ip = SyncSDKDefine.ipToLong(ipString);
        ProgressResult progressResult = new ProgressResult();
        TSDKOnProgressEvent onProgress = ProgressEventFactory.create("Carga de Label Element",ipString,progressResult);
        try{
            sync.SDK_Initialize();
            idTask = sync.SDK_ExecTaskA(ip,0,8192,path,onProgress,111);
            sync.SDK_WaitForTask(idTask);
            return progressResult.isSuccessful();
        }catch(Exception e){
            logger.error("Error durante la carga de Label Element: {}",e.getMessage(),e);
            return false;
        }finally {
            sync.SDK_Finalize();
        }
    }

    public boolean loadLabelBackground(String path, String ipString){
        long idTask;
        int ip = SyncSDKDefine.ipToLong(ipString);
        ProgressResult progressResult = new ProgressResult();
        TSDKOnProgressEvent onProgress = ProgressEventFactory.create("Carga de Label Background",ipString,progressResult);
        try{
            sync.SDK_Initialize();
            idTask = sync.SDK_ExecTaskA(ip,0,8193,path,onProgress,111);
            sync.SDK_WaitForTask(idTask);
            return progressResult.isSuccessful();
        }catch(Exception e){
            logger.error("Error durante la carga de Label Background: {}",e.getMessage(),e);
            return false;
        }finally {
            sync.SDK_Finalize();
        }
    }

    public boolean loadAdvancedBarcodes(String path, String ipString) throws InterruptedException {
        final int maxIntentos = 1;
        long idTask;
        int ip = SyncSDKDefine.ipToLong(ipString);
        boolean success = false;

        try {
            sync.SDK_Initialize();
            for (int intento = 1; intento <= maxIntentos; intento++) {
                ProgressResult progressResult = new ProgressResult();
                TSDKOnProgressEvent onProgress = ProgressEventFactory.create("Carga de Advanced Barcodes", ipString, progressResult);
                try {
                    logger.info("Intento {} de {}", intento, maxIntentos);
                    idTask = sync.SDK_ExecTaskA(ip, 0, 33, path, onProgress, 111);
                    if (idTask < 0) {
                        logger.error("No se pudo iniciar la tarea, idTask={}", idTask);
                        continue;
                    }
                    sync.SDK_WaitForTask(idTask);
                    success = progressResult.isSuccessful();
                    if (success) {
                        logger.info("La carga fue exitosa en el intento {}.", intento);
                        break;
                    } else {
                        sync.SDK_Initialize();

                        logger.warn("La carga no fue exitosa en el intento {}.", intento);
                    }
                } catch (Exception e) {
                    sync.SDK_Initialize();
                    logger.error("Error durante la carga en el intento {}: {}", intento, e.getMessage(), e);
                }
                sync.SDK_Initialize();
                Thread.sleep(1000); // esperar entre intentos
            }
        } finally {
            sync.SDK_Finalize();
        }


        if (!success) {
            logger.error("Fallo la carga de Advanced Barcodes luego de {} intentos.", maxIntentos);
        }
        return success;
    }

    public boolean deletePLU(String path, String ipString) throws IOException {
        if(!ConnectionTest.sendPingRequest(ipString)){
            return false;
        }
        long idTask;
        int ip = SyncSDKDefine.ipToLong(ipString);
        ProgressResult progressResult = new ProgressResult();
        TSDKOnProgressEvent onProgress = ProgressEventFactory.create("Borrado de PLU",ipString,progressResult);
        try{
            sync.SDK_Initialize();
            idTask = sync.SDK_ExecTaskA(ip,0,34,path,onProgress,111);
            sync.SDK_WaitForTask(idTask);
            return progressResult.isSuccessful();
        } catch (Exception e) {
            logger.error("Error durante la eliminación de PLU: {}",e.getMessage(),e);
            return false;
        }
    }
    public boolean loadFormatLabel(String path, String ipString, int user){
        long result;
        int ip = SyncSDKDefine.ipToLong(ipString);
        ProgressResult progressResult = new ProgressResult();
        TSDKOnProgressEvent onProgress = ProgressEventFactory.create("Cargando formato de Labels",ipString,progressResult);
        try{
            //logger.info("Cargando Etiqueta {} en balanza {}",filename,ipString);
            result = sync.SDK_ExecTaskA(ip,0,8192,path,onProgress,user);

            sync.SDK_WaitForTask(result);
            //logger.info("Etiqueta cargada.");
            return true;
        }catch(Exception e){
            logger.error("Error durante la carga de formatos de la etiqueta.");
            return false;
        }
    }
    public boolean loadBackgroundLabel(String path, String ipString, int user){
        long result;
        int ip = SyncSDKDefine.ipToLong(ipString);
        ProgressResult progressResult = new ProgressResult();
        TSDKOnProgressEvent onProgress = ProgressEventFactory.create("Cargando Backgrounds de Labels",ipString,progressResult);
        try{
            //logger.info("Cargando Etiqueta {} en balanza {}",filename,ipString);
            result = sync.SDK_ExecTaskA(ip,0,8193,path,onProgress,user);
            sync.SDK_WaitForTask(result);
            //logger.info("Etiqueta cargada.");
            return true;
        }catch(Exception e){
            logger.error("Error durante la carga de background de la etiqueta.");
            return false;
        }
    }
    public boolean loadFileLabel(String path, String ipString, int user){
        long result;
        int ip = SyncSDKDefine.ipToLong(ipString);
        ProgressResult progressResult = new ProgressResult();
        TSDKOnProgressEvent onProgress = ProgressEventFactory.create("Cargando File de Labels",ipString,progressResult);
        try{
            //logger.info("Cargando Etiqueta {} en balanza {}",filename,ipString);
            result = sync.SDK_ExecTaskA(ip,0,8194,path,onProgress,user);
            sync.SDK_WaitForTask(result);
            //logger.info("Etiqueta cargada.");
            return true;
        }catch(Exception e){
            logger.error("Error durante la carga File de la etiqueta.");
            return false;
        }
    }

    public boolean loadSystemParameters(String path, String ipString, int user){
        long result;
        int ip = SyncSDKDefine.ipToLong(ipString);
        ProgressResult progressResult = new ProgressResult();
        TSDKOnProgressEvent onProgress = ProgressEventFactory.create("Cargando archivo Configuraciones de la balanza ",ipString,progressResult);

        try{
            //logger.info("Cargando configuraciones del sistema {} en balanza {}",filename,ipString);
            result = sync.SDK_ExecTaskA(ip,0,12291,path,onProgress,user);
            sync.SDK_WaitForTask(result);
            //logger.info("Configuraion cargada correctamente.");
            return true;
        }catch(Exception e){
            logger.error("Error durante la carga de configuraciones de la balanza.");
            return false;
        }

    }

    public boolean createFilePLU(String path, String ipString) throws IOException {
        if(!ConnectionTest.sendPingRequest(ipString)){
            return false;
        }
        long idTask;
        int ip = SyncSDKDefine.ipToLong(ipString);
        ProgressResult progressResult = new ProgressResult();
        TSDKOnProgressEvent onProgress = ProgressEventFactory.create("Creado archivo?",ipString,progressResult);
        try{
            sync.SDK_Initialize();
            idTask = sync.SDK_ExecTaskA(ip,2,0,path,onProgress,111);
            sync.SDK_WaitForTask(idTask);
            return progressResult.isSuccessful();
        } catch (Exception e) {
            logger.error("Error durante la eliminación de PLU: {}",e.getMessage(),e);
            return false;
        }
    }
}
