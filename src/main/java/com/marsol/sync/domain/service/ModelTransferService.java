package com.marsol.sync.domain.service;

import com.marsol.sync.domain.model.Scale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class ModelTransferService {

    private final static Logger logger = LoggerFactory.getLogger(ModelTransferService.class);

    public void transferModelForScale(Scale scale){
        String python_path = "C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\Balanza HPRT\\Driver_Modelos\\Script\\modelTransfer.py";
        ProcessBuilder processBuilder = new ProcessBuilder("python",python_path,scale.getiP_Balanza());
        processBuilder.redirectErrorStream(true);
        try{
            Process process = processBuilder.start();

            // Leer la salida para evitar que se llene el buffer.
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info(line);
                }
            }

            int exitCode = process.waitFor();
            if(exitCode == 0){
                logger.info("Modelo cargado para balanza {}",scale.getiP_Balanza());
            }else{
                logger.error("Error durante la carga del modelo para balanza {}",scale.getiP_Balanza());
            }

        }catch (IOException e){
            logger.error(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
