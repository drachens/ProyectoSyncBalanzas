package com.marsol.sync.utils;

import com.marsol.sync.infraestructure.adapter.ErrorTranslator;
import com.marsol.sync.infraestructure.adapter.TSDKOnProgressEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProgressEventFactory {
    public static TSDKOnProgressEvent create(String contextMessage, String ipString, ProgressResult resultHolder){
        Logger logger = LoggerFactory.getLogger(ProgressEventFactory.class);
        return (errorCode, index, total, userDataCode) -> {
            String errorMessage = ErrorTranslator.getErrorMessage(errorCode);

            if(errorCode != 0 && errorCode != 1 && errorCode != 2){
                logger.error("[{}] Error {} ({}) en índice: {} de {} elementos para balanza -> {}",
                                contextMessage, errorMessage, errorCode, index, total, ipString);
                resultHolder.setSuccessful(false);
            }else if(errorCode == 0){
                logger.info("[{}] Se han procesado {} elementos para la balanza -> {}",
                                contextMessage, total, ipString);
            }else if(errorCode == -1){
                logger.error("[{}] Error inesperado para la balanza -> {}",
                                contextMessage, ipString);
                resultHolder.setSuccessful(false);
            }
        };
    }
}
