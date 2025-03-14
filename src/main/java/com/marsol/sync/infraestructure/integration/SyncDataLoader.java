package com.marsol.sync.infraestructure.integration;

import com.marsol.sync.infraestructure.adapter.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SyncDataLoader {
    private final SyncSDKIntf sync;
    private static final Logger logger = LoggerFactory.getLogger(SyncDataLoader.class);
    public SyncDataLoader() {
        this.sync = SyncManager.getInstance();
    }

    public boolean loadPLU(String filename, String ipString){
        final boolean[] isSuccessful = {true};
        long result;
        long result_0;
        int ip = SyncSDKDefine.ipToLong(ipString);
        TSDKOnProgressEvent onProgress = (var1, var2, var3, var4) -> {
            //var1 : ErrorCode
            //var2 : nIndex
            //var3 : nTotal
            //var4 : nUserDataCode
            String errorMessage = ErrorTranslator.getErrorMessage(var1);
            if(var1 != 0 && var1 != 1 && var1 != 2){
                logger.error("[ERROR EN CARGA DE BALANZA] ErrorCode {}: {} en indice: {} de {} elementos para balanza {}.",var1,errorMessage,var2,var3,ipString);
                isSuccessful[0] = false;
            }
            if(var1 == 0){
                logger.info("[CARGA DE BALANZA REALIZADA] Se han cargado todos los elementos ({}) para balanza {}",var3,ipString);
            }
            if(var1 == -1){
                logger.error("[ERROR EN CARGA DE BALANZA] Se ha producido un error inesperado durante la carga de la balanaza IP: {}",ipString);
                isSuccessful[0] = false;
            }
        };
        try{
            logger.info("Comienza Carga de PLU para balanza {}",ipString);
            result = sync.SDK_ExecTaskA(ip,0,0,filename,onProgress,111);
            SyncSDKIntf.INSTANCE.SDK_WaitForTask(result);
            //logger.info("Carga PLU para balanza: {}",ipString);
            return isSuccessful[0];
        }catch(Exception e){
            logger.error("Error al cargar PLU para balanza: {}, error: {}",ipString,e.getMessage());
            return false;
        }
    }

    public boolean loadNotes(String filename, String ipString, int typeNote){
        final boolean[] isSuccessful = {true};
        long result;
        long result_0;
        int ip = SyncSDKDefine.ipToLong(ipString);
        TSDKOnProgressEvent onProgress = (var1, var2, var3, var4) -> {
            //var1 : ErrorCode
            //var2 : nIndex
            //var3 : nTotal
            //var4 : nUserDataCode
            String errorMessage = ErrorTranslator.getErrorMessage(var1);
            if(var1 != 0 && var1 != 1 && var1 != 2){
                logger.error("[ERROR EN CARGA DE BALANZA] ErrorCode {}: {} en indice: {} de {} elementos para balanza {}",var1,errorMessage,var2,var3, ipString);
                isSuccessful[0] = false;
            }
            if(var1 == 0){
                logger.info("[CARGA DE BALANZA REALIZADA] Se han cargado todos los elementos ({}) para balanza {}",var3,ipString);
            }
            if(var1 == -1){
                logger.error("[ERROR EN CARGA DE BALANZA] Se ha producido un error inesperado durante la carga de la balanaza IP: {}",ipString);
                isSuccessful[0] = false;
            }
        };
        try{
            switch(typeNote){
                case 1:
                    try{
                        logger.info("Comienza Carga de Nota 1 para balanza: {}",ipString);
                        result = sync.SDK_ExecTaskA(ip,0,5,filename,onProgress,111);
                        sync.SDK_WaitForTask(result);
                        //logger.info("Carga completa de Nota 1 para balanza: {}",ipString);
                        break;
                    } catch (Exception e) {
                        logger.error("Error al cargar Nota 1 para balanza: {}, {}",ipString,e.getMessage());
                        break;
                    }
                case 2:
                    try{
                        //System.out.println("\nEliminando Nota 2...");
                        //result_0 = sync.SDK_ExecTaskA(ip,2,6,"",onProgress,111);
                        //sync.SDK_WaitForTask(result_0);
                        //System.out.println("\nEliminación Completa.");
                        logger.info("Comienza Carga de Nota 2 para balanza: {}",ipString);
                        result = sync.SDK_ExecTaskA(ip,0,6,filename,onProgress,111);
                        sync.SDK_WaitForTask(result);
                        //logger.info("Carga completa de Nota 2 para balanza: {}",ipString);
                        break;
                    } catch (Exception e) {
                        logger.error("Error al cargar Nota 2 en balanza: {}, error: {}",ipString,e.getMessage());
                        break;
                    }
                case 3:
                    try{
                        //System.out.println("\nEliminando Nota 3...");
                        //result_0 = sync.SDK_ExecTaskA(ip,2,7,"",onProgress,111);
                        //sync.SDK_WaitForTask(result_0);
                        //System.out.println("\nEliminación Completa.");
                        logger.info("Comienza Carga de Nota 3 para balanza: {}",ipString);
                        result = sync.SDK_ExecTaskA(ip,0,7,filename,onProgress,111);
                        sync.SDK_WaitForTask(result);
                        //logger.info("Carga completa de Nota 3 para balanza: {}",ipString);
                        break;
                    } catch (Exception e) {
                        logger.error("Error al cargar Nota 3 en balanza: {}, error: {}",ipString,e.getMessage());
                        break;
                    }
                case 4:
                    try{
                        logger.info("Comienza Carga de Nota 4 para balanza: {}",ipString);
                        result = sync.SDK_ExecTaskA(ip,0,8,filename,onProgress,111);
                        sync.SDK_WaitForTask(result);
                        //logger.info("Carga completa de Nota 4 para balanza: {}",ipString);
                        break;
                    } catch (Exception e) {
                        logger.error("Error al cargar Nota 4 en balanza: {}, error: {}",ipString,e.getMessage());
                        break;
                    }
                default:
                    logger.error("Numero de nota {} no encontrada",typeNote);
                    break;
            }
            return isSuccessful[0];
        }catch(Exception e){
            logger.error("Error al cargar notas {}",e.getMessage());
            return false;
        }
    }

    public boolean loadCustomBarcode(String filename, String ipString){
        long result;
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
        };
        try{
            logger.info("Cargando CustomBarcode para balanza {}",ipString);
            result = sync.SDK_ExecTaskA(ip,0,4,filename,onProgress,111);
            sync.SDK_WaitForTask(result);
            logger.info("CustomBarcode cargado.");
            return true;
        }catch(Exception e){
            logger.error("Error al cargar CustomBarcode en balanza {} error: {}",ipString,e.getMessage());
            return false;
        }
    }

    public boolean loadLabel(String filename, String ipString){
        long result;
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
        };
        try{
            logger.info("Cargando Etiqueta {} en balanza {}",filename,ipString);
            result = sync.SDK_ExecTaskA(ip,0,8194,filename,onProgress,111);
            sync.SDK_WaitForTask(result);
            logger.info("Etiqueta cargada.");
            return true;
        }catch(Exception e){
            logger.error("Error durante la carga de la etiqueta.");
            return false;
        }
    }
}
