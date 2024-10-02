package com.marsol.sync.service.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyncDataDownloader {
    private final SyncSDKIntf sync;
    private static final Logger logger = LoggerFactory.getLogger(SyncDataDownloader.class);
    private static final String testDirectoryDownload = "C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\testDirectoryDownload\\";
    public SyncDataDownloader() {
        this.sync = SyncManager.getInstance();
    }

    public boolean downloadPLU(String filename, String ipString){
        long result;
        String testRoute = testDirectoryDownload+filename;
        int ip = SyncSDKDefine.ipToLong(ipString);
        TSDKOnProgressEvent onProgress = (var1, var2, var3, var4) -> System.out.println("ErrorCode:" + var1  + " nIndex:" +var2 + " nTotal:" + var3 + " nUserDataCode:" + var4 );
        try{
            sync.SDK_Initialize();
            result = sync.SDK_ExecTaskA(ip,1,0,testRoute,onProgress,111);
            sync.SDK_WaitForTask(result);
            System.out.println("PLUs Descargados en: "+testRoute);
            return true;
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        }
    }

    public boolean downloadDepartment(String filename, String ipString){
        long result;
        String testRoute = testDirectoryDownload+filename;
        int ip = SyncSDKDefine.ipToLong(ipString);
        TSDKOnProgressEvent onProgress = (var1, var2, var3, var4) -> System.out.println("ErrorCode:" + var1  + " nIndex:" +var2 + " nTotal:" + var3 + " nUserDataCode:" + var4 );
        try{
            sync.SDK_Initialize();
            result = sync.SDK_ExecTaskA(ip,1,1,testRoute,onProgress,111);
            sync.SDK_WaitForTask(result);
            System.out.println("Departamentos Descargados en: "+testRoute);
            return true;
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        }
    }
    public boolean downloadCustomBarcode(String filename, String ipString){
        long result;
        String testRoute = testDirectoryDownload+filename;
        int ip = SyncSDKDefine.ipToLong(ipString);
        TSDKOnProgressEvent onProgress = (var1, var2, var3, var4) -> System.out.println("ErrorCode:" + var1  + " nIndex:" +var2 + " nTotal:" + var3 + " nUserDataCode:" + var4 );
        try{
            sync.SDK_Initialize();
            result = sync.SDK_ExecTaskA(ip,1,4,testRoute,onProgress,111);
            sync.SDK_WaitForTask(result);
            System.out.println("CustomBarcodes Descargados en: "+testRoute);
            return true;
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        }
    }
    public boolean downloadNotes(String filename, String ipString){
        long result;
        String testRoute = testDirectoryDownload+filename;
        int ip = SyncSDKDefine.ipToLong(ipString);
        TSDKOnProgressEvent onProgress = (var1, var2, var3, var4) -> System.out.println("ErrorCode:" + var1  + " nIndex:" +var2 + " nTotal:" + var3 + " nUserDataCode:" + var4 );
        try{
            sync.SDK_Initialize();
            result = sync.SDK_ExecTaskA(ip,1,5,testRoute+"1",onProgress,111);
            sync.SDK_WaitForTask(result);
            result = sync.SDK_ExecTaskA(ip,1,6,testRoute+"2",onProgress,111);
            sync.SDK_WaitForTask(result);
            result = sync.SDK_ExecTaskA(ip,1,7,testRoute+"3",onProgress,111);
            sync.SDK_WaitForTask(result);
            result = sync.SDK_ExecTaskA(ip,1,8,testRoute+"4",onProgress,111);
            sync.SDK_WaitForTask(result);
            System.out.println("Notas Descargados en: "+testRoute);
            return true;
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        }
    }
    public boolean downloadSystemParameters(String filename, String ipString){
        long result;
        String testRoute = testDirectoryDownload+filename;
        int ip = SyncSDKDefine.ipToLong(ipString);
        TSDKOnProgressEvent onProgress = (var1, var2, var3, var4) -> {
            //var1 : ErrorCode
            //var2 : nIndex
            //var3 : nTotal
            //var4 : nUserDataCode
            String errorMessage = ErrorTranslator.getErrorMessage(var1);
            if(var1 != 0 && var1 != 1 && var1 != 2){
                logger.error("[ERROR EN CARGA DE BALANZA] ErrorCode {}: {} en indice: {} de {} elementos.",var1,errorMessage,var2,var3);
            }
            if(var1 == 0){
                logger.info("[CARGA DE BALANZA REALIZADA] Se han cargado todos los elementos ({}).",var3);
            }
            if(var1 == -1){
                logger.error("[ERROR EN CARGA DE BALANZA] Se ha producido un error inesperado durante la carga de la balanaza IP: {}",ipString);
            }
        };        try{
            sync.SDK_Initialize();
            logger.info("Iniciando descarga de System Parameters");
            result = sync.SDK_ExecTaskA(ip,1,12,testRoute,onProgress,111);
            sync.SDK_WaitForTask(result);
            logger.info("System Parameters Descargados en: {}",testRoute);
            return true;
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        }
    }
}
