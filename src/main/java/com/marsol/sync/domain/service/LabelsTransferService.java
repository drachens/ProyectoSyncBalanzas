package com.marsol.sync.domain.service;

import com.marsol.sync.domain.model.Scale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class LabelsTransferService {

    private static final Logger logger = LoggerFactory.getLogger(LabelsTransferService.class);

    public void processLabelForScale(Scale scale){
        //@Value("${script.label.transfer.path}")
        String python_path = "C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\Balanza HPRT\\Driver_Etiquetas\\Script\\labelTransfer.py";
        ProcessBuilder processBuilder = new ProcessBuilder("python", python_path,scale.getiP_Balanza());
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
                logger.info("Etiquetas cargadas");
            }else {
                logger.error("Error al cargar etiquetas");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
