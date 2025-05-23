package com.marsol.sync.domain.service;

import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.model.structures.HeadersFilesHPRT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class WriteDeleteFileService {
    public final static Logger logger = LoggerFactory.getLogger(WriteDeleteFileService.class);

    @Value("${directory.pendings}")
    private String pendings;

    public void generateDeleteFile(List<Integer> productsToDelete, Scale scale) {
        try{
            if(productsToDelete.isEmpty()){
                throw new IllegalStateException("No hay productos que eliminar.");
            }
            String filename = String.join("_","pluDelete",String.valueOf(scale.getStore()),String.valueOf(scale.getDepartamento()),".txt");
            String[] header = HeadersFilesHPRT.PluDeleteHeader;
            String path = pendings+filename;
            try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))) {
                writer.write(String.join("\t", header));
                writer.newLine();

                for(int product : productsToDelete){
                    writer.write(String.valueOf(product));
                    writer.newLine();
                }

                logger.info("Archivo de productos a eliminar escrito correctamente en: {}", path);
            } catch (FileNotFoundException e) {
                logger.warn("Error al escribir el archivo de plu a eliminar {} : {}",path,e.getMessage());
            }

        } catch (IllegalStateException e) {
            logger.warn("No se puedo generar el archivo de productos a eliminar: {}",e.getMessage());
        } catch (IOException e) {
            logger.error("Error al escribir el archivo de productos a eliminar: {}",e.getMessage());
        }

    }
}
