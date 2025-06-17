package com.marsol.sync.infraestructure.integration;

import com.marsol.sync.infraestructure.adapter.*;
import com.marsol.sync.utils.ConnectionTest;
import com.marsol.sync.utils.ProgressEventFactory;
import com.marsol.sync.utils.ProgressResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    public boolean loadAdvancedBarcodes(String path, String ipString){
        long idTask;
        int ip = SyncSDKDefine.ipToLong(ipString);
        ProgressResult progressResult = new ProgressResult();
        TSDKOnProgressEvent onProgress = ProgressEventFactory.create("Carga de Advanced Barcodes",ipString,progressResult);
        try{
            sync.SDK_Initialize();
            idTask = sync.SDK_ExecTaskA(ip,0,33,path,onProgress,111);
            sync.SDK_WaitForTask(idTask);
            return progressResult.isSuccessful();
        }catch(Exception e){
            logger.error("Error durante la carga de Advanced Barcodes: {}",e.getMessage(),e);
            return false;
        }finally {
            sync.SDK_Finalize();
        }
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
}
