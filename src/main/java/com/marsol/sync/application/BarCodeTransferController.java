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

public class BarCodeTransferController {

    Logger logger = LoggerFactory.getLogger(BarCodeTransferController.class);


    private final SyncDataLoader syncDataLoader;


    public BarCodeTransferController(

    ) {
        this.syncDataLoader = new SyncDataLoader();
    }


    public void loadBarCodes(String ip){
        try{
            //String file = barCodeTransferService.getBarCodeFile();
            String file = "C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\Balanza HPRT\\Proyecto Walmart\\Codigos de barra avanzados\\27062025.txt";
            syncDataLoader.loadAdvancedBarcodes(file,ip);

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
