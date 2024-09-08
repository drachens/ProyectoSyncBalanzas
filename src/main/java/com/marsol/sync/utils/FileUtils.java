package com.marsol.sync.utils;

import com.marsol.sync.controller.ScalesNetworkController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static int countLines(String filename){
        int lineCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            while (reader.readLine() != null) {
                lineCount++;
            }
        } catch (IOException e) {
            logger.error("Error al leer el archivo: " + filename);
        }

        return lineCount-1; //Primera linea es un header
    }

}
