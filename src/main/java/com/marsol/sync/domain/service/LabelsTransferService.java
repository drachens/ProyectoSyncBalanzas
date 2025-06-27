package com.marsol.sync.domain.service;

import com.marsol.sync.domain.model.Scale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class LabelsTransferService {

    private static final Logger logger = LoggerFactory.getLogger(LabelsTransferService.class);

    @Value("${label.path}")
    private String Dir;

    public LabelsTransferService() {}

    public List<String> getLabelsFiles(String dir) {
        System.out.println("Directory: " + dir);
        File directory = new File(dir);
        if(!directory.exists() || !directory.isDirectory()){
            //logger.error("{} no es un directorio.", directory);
            throw new RuntimeException(directory+" no es un directorio.");
        }

        //Filtrar archivos formato .lbl
        String[] files = directory.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".lbl");
            }
        });

        //Verificar si se encontraron archivos
        if(files == null || files.length == 0){
            //logger.error("No se encontraron archivos .lbl en {}",directory.getAbsolutePath());
            throw new RuntimeException("No se encontraron archivos .lbl");
        } else{
            List<String> labels = new ArrayList<>();
            for(String file : files){
                labels.add(directory + File.separator + file);
            }
            logger.info("Etiquetas encontradas: {}",labels.size());
            return labels;
        }
    }

}
