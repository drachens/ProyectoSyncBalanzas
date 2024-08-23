package com.marsol.sync.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingService {
    private static final Logger logger = LoggerFactory.getLogger(LoggingService.class);

    public static void logError(String message, Exception e) {
        logger.error(message, e);
    }

    public static void logError(String message) {
        logger.error(message);
    }

    public static void logInfo(String message) {
        logger.info(message);
    }

    public static void logInfo(String message, Exception e) {
        logger.info(message, e);
    }

    public static void logDebug(String message) {
        logger.debug(message);
    }

    public static void logDebug(String message, Exception e) {
        logger.debug(message, e);
    }

    public static void logWarn(String message) {
        logger.warn(message);
    }

    public static void logWarn(String message, Exception e) {
        logger.warn(message, e);
    }

    public static void logTrace(String message) {
        logger.trace(message);
    }

    public static void logTrace(String message, Exception e) {
        logger.trace(message, e);
    }
}
