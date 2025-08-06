package com.marsol.sync.application;

import com.marsol.sync.domain.service.BarCodeTransferService;
import com.marsol.sync.domain.service.LabelsTransferService;
import com.marsol.sync.infraestructure.integration.SyncDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class BarCodeTransferController {

    @Value("${advance-code.path}")
    private String codeDirectory;

    Logger logger = LoggerFactory.getLogger(BarCodeTransferController.class);


    private final SyncDataLoader syncDataLoader;


    public BarCodeTransferController(

    ) {
        this.syncDataLoader = new SyncDataLoader();
    }

    public void loadAdvancedCode(String Ip){
        try{
            logger.info("Comienzo de carga de codigos de barra avanzados");
            syncDataLoader.loadAdvancedBarcodes(codeDirectory, Ip);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
