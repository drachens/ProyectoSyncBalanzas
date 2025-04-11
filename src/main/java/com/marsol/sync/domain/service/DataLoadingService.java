package com.marsol.sync.domain.service;

import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.infraestructure.integration.SyncDataLoader;
import com.marsol.sync.model.Layout;
import com.marsol.sync.model.Log;
import com.marsol.sync.infraestructure.api.LogService;
import com.marsol.sync.infraestructure.api.ScaleService;
import com.marsol.sync.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DataLoadingService {
    private static final Logger logger = LoggerFactory.getLogger(DataLoadingService.class);
    private final DataExtractionService dataExtractionService;
    private final LogService logService;
    private final ImagesTransferService imagesTransferService;
    private final SyncDataLoader syncData;
    private final ScaleService scaleService;

    @Autowired
    public DataLoadingService(DataExtractionService dataExtractionService,
                              ImagesTransferService imagesTransferService,
                              LogService logService,
                              ScaleService scaleService) {
        this.dataExtractionService = dataExtractionService;
        this.imagesTransferService = imagesTransferService;
        this.logService = logService;
        this.syncData = new SyncDataLoader();
        this.scaleService = scaleService;
    }

    @Value("${directory.pendings}")
    private String directoryPendings;

    @Value("${date.time.formatter}")
    private String dateTimeFormatter;

    public void loadPlu(Scale scale){
        int storeNbr = scale.getStore(); //Numero de tienda
        int deptNbr = scale.getDepartamento(); //Numero de departamento
        String pluFile = String.format("%splu_%s_%s.txt",directoryPendings,storeNbr,deptNbr); //filepath de plu.txt
        String ipString = scale.getIp_Balanza(); //IP Balanza
        LocalDateTime now = LocalDateTime.now(); //Hora actual
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormatter);
        String dateTimeFormated = now.format(formatter);

        boolean boolPLU = syncData.loadPLU(pluFile, ipString); //Se realiza la carga del archivo plu.txt

        if(boolPLU){
            int datos1 = FileUtils.countLines(pluFile);
            Log log = new Log(0,storeNbr,deptNbr,"Carga de PLU's",
                    datos1,scale.getIp_Balanza(),dateTimeFormated,"Success");
            logService.createLog(log);
            logService.updateStatus(log);
        }else{
            logger.error("Error durante la carga de PLU en balanza {}",ipString);
        }

    }

    public void loadNotes(Scale scale){
        String note1File = directoryPendings+"Note1_"+scale.getStore()+"_"+scale.getDepartamento()+".txt";
        String note2File = directoryPendings+"Note2_"+scale.getStore()+"_"+scale.getDepartamento()+".txt";
        String note3File = directoryPendings+"Note3_"+scale.getStore()+"_"+scale.getDepartamento()+".txt";
        String note4File = directoryPendings+"Note4_"+scale.getStore()+"_"+scale.getDepartamento()+".txt";

        String ipString = scale.getIp_Balanza();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormatter);
        String dateTimeFormated = now.format(formatter);

        boolean boolNote1 = syncData.loadNotes(note1File,ipString,1);
        boolean boolNote2 = syncData.loadNotes(note2File,ipString,2);
        boolean boolNote3 = syncData.loadNotes(note3File,ipString,3);
        boolean boolNote4 = syncData.loadNotes(note4File,ipString,4);

        if(boolNote1){
            //logger.info("Archivo {} cargado correctamente a la balanza.",note1File);
            int datos2 = FileUtils.countLines(note1File);
            Log log = new Log(0,scale.getStore(),scale.getDepartamento(),"Carga de Nota 1",
                    datos2,scale.getIp_Balanza(),dateTimeFormated,"Success");
            logService.createLog(log);
            logService.updateStatus(log);
        }else{
            logger.warn("Error al cargar el archivo {}",note1File);
        }
        if(boolNote2){
            //logger.info("Archivo {} cargado correctamente a la balanza.",note2File);
            int datos3 = FileUtils.countLines(note2File);
            Log log = new Log(0,scale.getStore(),scale.getDepartamento(),"Carga de Nota 2",
                    datos3,scale.getIp_Balanza(),dateTimeFormated,"Success");
            logService.createLog(log);
            logService.updateStatus(log);
        }else{
            logger.warn("Error al cargar el archivo {}",note2File);
        }
        if (boolNote3){
            //logger.info("Archivo {} cargado correctamente a la balanza.",note3File);
            int datos4 = FileUtils.countLines(note3File);
            Log log = new Log(0,scale.getStore(),scale.getDepartamento(),"Carga de Nota 3",
                    datos4,scale.getIp_Balanza(),dateTimeFormated,"Success");
            logService.createLog(log);
            logService.updateStatus(log);
        }else{
            logger.warn("Error al cargar el archivo {}", note3File);
        }
        if(boolNote4){
            //logger.info("Archivo {} cargado correctamente a la balanza.",note4File);
            int datos5 = FileUtils.countLines(note4File);
            Log log = new Log(0,scale.getStore(),scale.getDepartamento(),"Carga de Nota 4",
                    datos5,scale.getIp_Balanza(),dateTimeFormated,"Success");
            logService.createLog(log);
            logService.updateStatus(log);
        }else{
            logger.warn("Error al cargar el archivo {}",note4File);
        }
    }
}
