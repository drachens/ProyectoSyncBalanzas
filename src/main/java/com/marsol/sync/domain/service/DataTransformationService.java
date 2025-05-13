package com.marsol.sync.domain.service;

import com.marsol.sync.domain.handler.dates.DatesHandler;
import com.marsol.sync.domain.handler.dates.PerishabilityHandler;
import com.marsol.sync.domain.model.PLU;
import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.domain.handler.label.NotesInfoHandler;
import com.marsol.sync.domain.handler.label.GraphicsHandler;
import com.marsol.sync.domain.handler.label.LabelHandler;
import com.marsol.sync.model.Infonut;
import com.marsol.sync.model.Item;
import com.marsol.sync.model.structures.HeadersFilesHPRT;
import com.marsol.sync.utils.NoteWriter;
import com.marsol.sync.utils.NotesForWalmart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataTransformationService {
    private final static Logger logger = LoggerFactory.getLogger(DataTransformationService.class);
    private final DataExtractionService dataExtractionService;

    @Value("${directory.pendings}")
    private String directoryPendings;
    @Value("${date.time.formatter}")
    private String dateTimeFormatter;

    @Autowired
    public DataTransformationService(DataExtractionService dataExtractionService) {
        this.dataExtractionService = dataExtractionService;
    }

    public void transformDataNotes(Scale scale){
        int storeNbr = scale.getStore();
        int deptNbr = scale.getDepartamento();
        List<Infonut> infonuts = dataExtractionService.getInfonut(storeNbr, deptNbr);

        String note1FileName = directoryPendings+"Note1_"+scale.getStore()+"_"+scale.getDepartamento()+".txt";
        String note2FileName = directoryPendings+"Note2_"+scale.getStore()+"_"+scale.getDepartamento()+".txt";
        String note3FileName = directoryPendings+"Note3_"+scale.getStore()+"_"+scale.getDepartamento()+".txt";
        String note4FileName = directoryPendings+"Note4_"+scale.getStore()+"_"+scale.getDepartamento()+".txt";

        //Note1 RESOLUCION
        try {
            Map<Integer, String> infonutMap = new HashMap<>();
            for (Infonut infonut : infonuts) {
                int pluNbr = infonut.getPlu_nbr();
                String value = NotesForWalmart.resolucion(infonut);
                infonutMap.put(pluNbr, value);
            }
            NoteWriter.writeNote(note1FileName, infonutMap);
            logger.info("Nota 1 creada.");
        } catch (Exception e) {
            logger.error("No se ha podido crear Nota 1: {}",e.getMessage());
        }

        //Note2 y Note4 INGREDIENTES
        try {
            Map<Integer, String> infonutMap = new HashMap<>();
            Map<Integer, String> infonutMapNota4 = new HashMap<>();
            for (Infonut infonut : infonuts) {
                if(!infonut.isEs_etiqueta_propia()){
                    int pluNbr = infonut.getPlu_nbr();
                    List<String> ingredientes = NotesForWalmart.ingredientes2(infonut);
                    String value_1 = ingredientes.get(0);
                    if(!value_1.isEmpty()){
                        infonutMap.put(pluNbr, value_1);
                    }
                    if(ingredientes.size() > 1){
                        String value_2 = ingredientes.get(1);
                        if(!value_2.isEmpty()){
                            infonutMapNota4.put(pluNbr,value_2);
                        }
                    }
                }
            }
            if(!infonuts.isEmpty()){
                NoteWriter.writeNote(note2FileName, infonutMap);
                logger.info("Nota 2 creada.");
            }
            if(!infonutMapNota4.isEmpty()){
                NoteWriter.writeNote(note4FileName,infonutMapNota4);
                logger.info("Nota 4 creada.");
            }
        } catch (Exception e) {
            logger.error("No se ha podido crear Notas 2 o 4: {}",e.getMessage());
        }

        //Note3
        try {
            Map<Integer, String> infonutMap = new HashMap<>();
            for (Infonut infonut : infonuts) {
                if(!infonut.isEs_etiqueta_propia()){
                    int pluNbr = infonut.getPlu_nbr();
                    String value = NotesForWalmart.tablaNutricional(infonut);
                    if (value.length() > 0) {
                        infonutMap.put(pluNbr, value);
                    }
                }
            }
            NoteWriter.writeNote(note3FileName, infonutMap);
            logger.info("Nota 3 creada.");
        } catch (Exception e) {
            logger.error("No se ha podido crear Nota 3: {}",e.getMessage());
        }
    }

    public void transformDataPLUs(Scale scale){
        LabelHandler labelHandlerChain = initializeLabelHandlerChain();
        DatesHandler datesHandlerChain = initializeDatesHandlerChain();
        int storeNbr = scale.getStore();
        int deptNbr = scale.getDepartamento();
        boolean esAutoServicio = scale.getIsEsAutoservicio();
        List<Item> items = new ArrayList<>();
        String[] header = HeadersFilesHPRT.PLUHeader;

        String filename = String.format("%splu_%s_%s.txt",directoryPendings,scale.getStore(),scale.getDepartamento());
        List<Infonut> infonuts = dataExtractionService.getInfonut(storeNbr, deptNbr);
        if(esAutoServicio){
            try{
                items = dataExtractionService.getAutoservicioItemsDept(storeNbr,deptNbr);
            }catch(Exception e){
                logger.error("Error al obtener lista de productos para balanza de Autoservicio {} : {}",scale.getIp_Balanza(),e.getMessage());
            }
        }else{
            try{
                items = dataExtractionService.getItemsDept(storeNbr, deptNbr);
            } catch (Exception e) {
                logger.error("Error al obtener lista de productos para balanza {} : {}",scale.getIp_Balanza(),e.getMessage());
            }
        }

        Map<Integer, Infonut> infonutMap = new HashMap<>();
        for(Infonut infonut : infonuts){
            infonutMap.put(infonut.getPlu_nbr(),infonut);
        }
        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))){
            writer.write(String.join("\t",header));
            writer.newLine();
            for(Item item : items){
                Infonut infonut = infonutMap.get((int) item.getPlu_nbr());
                PLU plu = createPLU(item,infonut);
                labelHandlerChain.handleLabel1(item,infonut,plu);
                datesHandlerChain.handleDate(infonut,plu);

                writer.write(plu.toString());
                writer.newLine();
            }
            logger.info("{} creado.",filename);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private LabelHandler initializeLabelHandlerChain(){
        //LabelHandler departmentHanlder = new NotesInfoHandler();
        LabelHandler graphicsHandler = new GraphicsHandler();
        //departmentHanlder.setNext(graphicsHandler);
        return graphicsHandler;
    }

    private DatesHandler initializeDatesHandlerChain(){
        return new PerishabilityHandler();
    }

    private PLU createPLU(Item item, Infonut infonut){
        return new PLU.Builder()
                .setLFCode((int) item.getPlu_nbr())
                .setItemCode(String.valueOf(item.getUpc_nbr()).substring(0, 7))
                .setDepartment(item.getDept_nbr())
                .setName1(item.getItem1_desc())
                .setName2(" ")
                .setName3(item.getBrand_name())
                .setBarcodeType1(104)
                .setBarcodeType2(item.getSell_uom_code().equals("EA") ? 107 : 106)
                .setUnitPrice(item.getSell_price())
                .setWeightUnit(item.getSell_uom_code().equals("EA") ? 8 : 0)
                .setValidDays(infonut.getDiasPerecibilidad())
                .setProducedDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateTimeFormatter)))
                .build();
    }

}
