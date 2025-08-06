package com.marsol.sync.application;

import com.marsol.sync.domain.service.LabelsTransferService;
import com.marsol.sync.infraestructure.api.ScaleService;
import com.marsol.sync.infraestructure.integration.SyncDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class LabelTransfererController {

    @Value("${label.path}")
    private String directory;

    Logger logger = LoggerFactory.getLogger(LabelTransfererController.class);

    private final LabelsTransferService labelsTransferService;
    private SyncDataLoader syncDataLoader;


    @Autowired
    public LabelTransfererController(LabelsTransferService labelsTransferService) {
        this.labelsTransferService = labelsTransferService;


    }

    public void loadLabels(String Ip){
        try{
            List<String> labelsTransferFiles = labelsTransferService.getLabelsFiles(directory);
            logger.info("LabelsFiles: {}", labelsTransferFiles);
            logger.info("Cargando etiquetas balanza Ip: {}", Ip);

            for(String label : labelsTransferFiles){
                syncDataLoader = new SyncDataLoader();
                syncDataLoader.loadFormatLabel(label,Ip,111);
                syncDataLoader.loadBackgroundLabel(label, Ip, 111);
                syncDataLoader.loadFileLabel(label,Ip,111);
            }


        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


}
