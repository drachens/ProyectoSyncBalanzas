package com.marsol.sync.domain.service;

import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.model.structures.HeadersFilesHPRT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class DeleteFileWriterService {
    public final static Logger logger = LoggerFactory.getLogger(DeleteFileWriterService.class);

    @Value("${directory.pendings}")
    private String pendings;

    public void generateDeleteFile(List<Integer> productsToDelete, Scale scale) {
        try{
            if(productsToDelete.isEmpty()){
                throw new Exception("Lista de productos a eliminar es vacia.");
            }
            String filename = String.join("_","pluDelete",String.valueOf(scale.getStore()),String.valueOf(scale.getDepartamento()));
            String[] header = HeadersFilesHPRT.PluDeleteHeader;
            String path = pendings+filename;
            try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))) {
                writer.write(String.join("\t", header));
                writer.newLine();

                for(int product : productsToDelete){
                    writer.write(String.valueOf(product));
                    writer.newLine();
                }

            } catch (FileNotFoundException e) {
                logger.error("Error al escribir el archivo de plu a eliminar {} : {}",path,e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            logger.error("Error al generar el archivo de productos a eliminar: {}",e.getMessage());
        }

    }
}
