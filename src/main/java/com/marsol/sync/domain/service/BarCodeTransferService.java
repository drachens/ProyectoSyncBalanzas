package com.marsol.sync.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class BarCodeTransferService {

    private static final Logger logger = LoggerFactory.getLogger(BarCodeTransferService.class);

    private String dir;

    public BarCodeTransferService() {}

    public String getBarCodeFile() {
        System.out.println("Directory: " + dir);
        File directory = new File(dir);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new RuntimeException(directory + " no es un directorio.");
        }

        File[] files = directory.listFiles();

        if (files == null || files.length == 0) {
            throw new RuntimeException("No se encontraron archivos en la carpeta " + directory);
        }

        for (File file : files) {
            if (file.isFile()) {
                logger.info("Código de barra encontrado: {}", file.getName());
                return dir + "\\" + file.getName();
                //return file.getAbsolutePath();
            }
        }

        throw new RuntimeException("No se encontró ningún archivo válido en la carpeta " + directory);
    }



}
