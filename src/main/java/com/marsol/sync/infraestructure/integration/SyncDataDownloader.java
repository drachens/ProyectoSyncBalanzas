package com.marsol.sync.infraestructure.integration;

import com.marsol.sync.infraestructure.adapter.*;
import com.marsol.sync.utils.ConnectionTest;
import com.marsol.sync.utils.FileUtils;
import com.marsol.sync.utils.ProgressEventFactory;
import com.marsol.sync.utils.ProgressResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * Clase para descargar archivos/datos desde la balanza, es decir,
 * transferir los datos de la balanza al PC
 */
@Component
public class SyncDataDownloader {
    private final SyncSDKIntf sync;
    private static final Logger logger = LoggerFactory.getLogger(SyncDataDownloader.class);

    public SyncDataDownloader() {
        this.sync = SyncManager.getInstance();
    }

    public boolean downloadPLU(String path, String ipString) throws IOException {
        if(!ConnectionTest.sendPingRequest(ipString)){
            logger.error("Error de conexión con la balanza -> {}",ipString);
            return false;
        }
        long idTask;
        int ip = SyncSDKDefine.ipToLong(ipString);
        ProgressResult progressResult = new ProgressResult();
        TSDKOnProgressEvent onProgress = ProgressEventFactory.create("Descarga de PLU",ipString,progressResult);
        try{
            sync.SDK_Initialize();
            idTask = sync.SDK_ExecTaskA(ip,1,0,path,onProgress,111);
            sync.SDK_WaitForTask(idTask);
            return progressResult.isSuccessful();
        }catch(Exception e){
            logger.error("Error durante descarga de PLU : {}",e.getMessage(),e);
            return false;
        }
    }

    public boolean downloadDepartment(String path, String ipString) throws IOException {
        if(!ConnectionTest.sendPingRequest(ipString)){
            logger.error("Error de conexión con la balanza -> {}",ipString);
            return false;
        }
        long idTask;
        int ip = SyncSDKDefine.ipToLong(ipString);
        ProgressResult progressResult = new ProgressResult();
        TSDKOnProgressEvent onProgress = ProgressEventFactory.create("Descarga de Departamentos",ipString,progressResult);
        try{
            sync.SDK_Initialize();
            idTask = sync.SDK_ExecTaskA(ip,1,1,path,onProgress,111);
            sync.SDK_WaitForTask(idTask);
            return progressResult.isSuccessful();
        }catch(Exception e){
            logger.error("Error durante la descarga de Departamentos : {}",e.getMessage(),e);
            return false;
        }finally {
            sync.SDK_Finalize();
        }
    }
    public boolean downloadBarcode(String path, String ipString) throws IOException {
        if(!ConnectionTest.sendPingRequest(ipString)){
            logger.error("Error de conexión con la balanza -> {}",ipString);
            return false;
        }
        long idTask;
        int ip = SyncSDKDefine.ipToLong(ipString);
        ProgressResult progressResult = new ProgressResult();
        TSDKOnProgressEvent onProgress = ProgressEventFactory.create("Descarga de Barcode",ipString,progressResult);
        try{
            sync.SDK_Initialize();
            idTask = sync.SDK_ExecTaskA(ip,1,4,path,onProgress,111);
            sync.SDK_WaitForTask(idTask);
            return progressResult.isSuccessful();
        }catch(Exception e){
            logger.error("Error durante descarga de Barcode : {}",e.getMessage(),e);
            return false;
        }finally {
            sync.SDK_Finalize();
        }
    }

    public boolean downloadNote(int noteNumber, String path, String ipString) throws IOException {
        if (noteNumber < 1 || noteNumber > 4) {
            logger.error("Número de nota {} no es válido. Debe estar entre 1 y 4.", noteNumber);
            return false;
        }
        if(!ConnectionTest.sendPingRequest(ipString)){
            logger.error("Error de conexión con la balanza -> {}",ipString);
            return false;
        }

        int ip = SyncSDKDefine.ipToLong(ipString);
        ProgressResult progressResult = new ProgressResult();
        String mensaje = "Descarga de Note " + noteNumber;
        TSDKOnProgressEvent onProgress = ProgressEventFactory.create(mensaje, ipString, progressResult);
        int taskCode = 4 + noteNumber; // Note 1 = 5, Note 2 = 6, etc.

        try {
            sync.SDK_Initialize();
            long taskId = sync.SDK_ExecTaskA(ip, 1, taskCode, path, onProgress, 111);
            sync.SDK_WaitForTask(taskId);
            return progressResult.isSuccessful();
        } catch (Exception e) {
            logger.error("Error durante {}: {}", mensaje, e.getMessage(), e);
            return false;
        } finally {
            sync.SDK_Finalize();
        }
    }

    public boolean downloadSystemParameters(String path, String ipString) throws IOException {
        if(!ConnectionTest.sendPingRequest(ipString)){
            logger.error("Error de conexión con la balanza -> {}",ipString);
            return false;
        }
        long idTask;
        int ip = SyncSDKDefine.ipToLong(ipString);
        ProgressResult progressResult = new ProgressResult();
        TSDKOnProgressEvent onProgress = ProgressEventFactory.create("Descarga de System Parameters",ipString,progressResult);
        try{
            sync.SDK_Initialize();
            idTask = sync.SDK_ExecTaskA(ip,1,12291,path,onProgress,111);
            sync.SDK_WaitForTask(idTask);
            return progressResult.isSuccessful();
        }catch(Exception e){
            logger.error("Error durante descarga de System Parameters : {}",e.getMessage(),e);
            return false;
        }finally {
            sync.SDK_Finalize();
        }
    }

    public boolean downloadLabel(String path, String ipString) throws IOException {
        if(!ConnectionTest.sendPingRequest(ipString)){
            logger.error("Error de conexión con la balanza -> {}",ipString);
            return false;
        }
        String extension = FileUtils.getFileExtension(path);
        //Verificar que sea un archivo de etiquetas .lbl
        if(!".lbl".equalsIgnoreCase("."+extension) || extension.isEmpty()){
            logger.error("El formato de la etiqueta debe ser .lbl y no {}",
                    extension.isEmpty() ? "[archivo sin extension]" : "."+extension);
            return false;
        }
        long idTask;
        int ip = SyncSDKDefine.ipToLong(ipString);
        ProgressResult progressResult = new ProgressResult();
        TSDKOnProgressEvent onProgress = ProgressEventFactory.create("Descarga de Label",ipString,progressResult);
        try{
            sync.SDK_Initialize();
            idTask = sync.SDK_ExecTaskA(ip,1,8194,path,onProgress,111);
            sync.SDK_WaitForTask(idTask);
            return progressResult.isSuccessful();
        }catch(Exception e){
            logger.error("Error durante descarga de Label : {}",e.getMessage(),e);
            return false;
        }finally {
            sync.SDK_Finalize();
        }
    }

    public boolean downloadAdvancedBarcodes(String path, String ipString) throws IOException {
        if(!ConnectionTest.sendPingRequest(ipString)){
            logger.error("Error de conexión con la balanza -> {}",ipString);
            return false;
        }
        long idTask;
        int ip = SyncSDKDefine.ipToLong(ipString);
        ProgressResult progressResult = new ProgressResult();
        TSDKOnProgressEvent onProgress = ProgressEventFactory.create("Descarga de Advanced Barcodes",ipString,progressResult);
        try{
            sync.SDK_Initialize();
            idTask = sync.SDK_ExecTaskA(ip,1,33,path,onProgress,111);
            sync.SDK_WaitForTask(idTask);
            return progressResult.isSuccessful();
        }catch(Exception e){
            logger.error("Error durante descarga de Barcodes : {}",e.getMessage(),e);
            return false;
        }finally {
            sync.SDK_Finalize();
        }
    }


}
