package com.marsol.sync.service.transform;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marsol.sync.app.ConfigLoader;
import com.marsol.sync.model.*;
import com.marsol.sync.model.structures.HeadersFilesHPRT;
import com.marsol.sync.service.api.InfonutService;
import com.marsol.sync.service.api.LogService;
import com.marsol.sync.service.api.ProductService;
import com.marsol.sync.utils.io.NoteWriter;
import com.marsol.sync.utils.NotesForWalmart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;


/*
Es necesario agregar una forma de identificar si una balanza es autoservicio, el programa debe comportarse
de la siguiente forma:
    SI ES autoservicio, entonces el archivo PLU.txt debe contener solo los items correspondientes al layout
    de la balanza, e incluir las imagenes.
    SI NO ES autoservicio, entonces el archivo PLU.txt contiene todos los items correspondiente a la balanza/tienda/depto
 */

@Service
public class TransformWalmartPLUs implements TransformationStrategy <Item>{
    private InfonutService infonutService;
    private List<Infonut> infoNut;
    private ConfigLoader configLoader;
    private ProductService productService;
    private LogService logService;
    @Value("${directory.pendings}")
    private String directoryPendings;
    @Value("${wm.enpoint.logs.enable}")
    private boolean wmEnpointLogsEnable;
    @Value("${date.time.formatter}")
    private String dateTimeFormatter;
    private Log log;
    String now;

    public TransformWalmartPLUs(LogService logService){
        this.logService = logService;
        configLoader = new ConfigLoader();
    }

    @Autowired
    public void setInfonutService(InfonutService infonutService){
        this.infonutService = infonutService;
    }
    public void setProductService(ProductService productService){
        this.productService = productService;
    }
    public void setInfonut(List<Infonut> infoNut){
        this.infoNut = infoNut;
    }

    @Override
    public void transformDataPLUs(Scale scale){
        int storeNbr = scale.getStore();
        int deptNbr = scale.getDepartamento();
        LocalDateTime dateTime = LocalDateTime.now();
        boolean esAutoServicio = scale.getIsEsAutoservicio();
        boolean esDual = scale.getIsEsDual();
        String filename = String.format("%splu_%s_%s.txt",directoryPendings,scale.getStore(),scale.getDepartamento());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormatter);
        String dateTimeFormated = dateTime.format(formatter);
        String[] header = HeadersFilesHPRT.PLUHeader;
        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"))){
            writer.write(String.join("\t",header));
            writer.newLine();
            try{
                Gson gson_items = new Gson();
                String itemsJSON = productService.getItemsDept(storeNbr,deptNbr);
                Type type = new TypeToken<List<Item>>(){}.getType();
                List<Item> items = gson_items.fromJson(itemsJSON, type);
                now = LocalDateTime.now().format(formatter);

                if(!items.isEmpty() && wmEnpointLogsEnable){
                    log = new Log(scale.getStore(),scale.getDepartamento(),"Descarga de productos",
                            items.size(),scale.getIp_Balanza(),now,"Success");
                    logService.createLog(log);
                }

                Gson gson_infonut = new Gson();
                String infonutsJSON = infonutService.getInfonut(storeNbr, deptNbr);
                List<Infonut> infonuts;
                try{
                    Type infonutType = new TypeToken<List<Infonut>>(){}.getType();
                    infonuts = gson_infonut.fromJson(infonutsJSON,infonutType);
                    now = LocalDateTime.now().format(formatter);

                    if(!infonuts.isEmpty() && wmEnpointLogsEnable){
                        log = new Log(scale.getStore(), scale.getDepartamento(),"Descarga de Infonut",
                                infonuts.size(),scale.getIp_Balanza(),now,"Success");
                        logService.createLog(log);
                    }
                }catch (Exception e){
                    infonuts = infoNut;
                }
                Map<Integer, Infonut> infonutMap = new HashMap<>();
                for(Infonut infonut : infonuts){
                    infonutMap.put(infonut.getPlu_nbr(),infonut);
                }
                for(Item item : items){
                    int pluNbr = (int) item.getPlu_nbr();
                    String itemCode = String.valueOf(item.getUpc_nbr()).substring(0,7);
                    int dept_nbr = item.getDept_nbr();
                    String name1 = item.getItem1_desc();
                    String itemStatusCode = item.getItem_status_code();
                    int weightUnit;
                    int unitPrice = item.getSell_price();
                    int tareWeight = 0;
                    switch (itemStatusCode){
                        case "A":
                            weightUnit = 0; //Unidad de paso Kg=0
                            break;
                        case "I":
                            weightUnit = 8; //Unidad por piezas=8
                            break;
                        default:
                            weightUnit = 0;
                    }
                    //Label1 depende de ImagenSellos de Infonut de un producto
                    int label1;
                    //Buscar infonut por PLU para obtener ImagenSellos
                    Infonut infonut = infonutMap.get(pluNbr);
                    if(infonut != null){
                        String imagenSellos = infonut.getImagenSellos();
                        int imagenSellosID = Integer.parseInt(imagenSellos);
                        label1 = (imagenSellosID >=1 && imagenSellosID <=15) ? imagenSellosID:32;
                    }else {label1 = 32;}
                    PLU plu = new PLU(pluNbr,itemCode,dept_nbr,name1,label1,
                            104,unitPrice,weightUnit,tareWeight,
                            dateTimeFormated,dateTimeFormated,dateTimeFormated);

                    writer.write(plu.toString());
                    writer.newLine();
                }
                System.out.println("PLU.txt creado exitosamente!");

            }catch (IOException e){e.printStackTrace();}
        }catch (IOException e){e.printStackTrace();}
    }

    @Override
    public void transformDataNotes(Scale scale) {
        LocalDateTime dateTime = LocalDateTime.now();
        String note1FileName = directoryPendings+"Note1_"+scale.getStore()+"_"+scale.getDepartamento()+".txt";
        String note2FileName = directoryPendings+"Note2_"+scale.getStore()+"_"+scale.getDepartamento()+".txt";
        String note3FileName = directoryPendings+"Note3_"+scale.getStore()+"_"+scale.getDepartamento()+".txt";

        int storeNbr = scale.getStore();
        int deptNbr = scale.getDepartamento();

        String infonutsJSON = infonutService.getInfonut(storeNbr, deptNbr);
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Infonut>>() {}.getType();
        List<Infonut> infonuts = gson.fromJson(infonutsJSON, listType);

        //Note1
        try {
            Map<Integer, String> infonutMap = new HashMap<>();
            for (Infonut infonut : infonuts) {
                int pluNbr = infonut.getPlu_nbr();
                String value = NotesForWalmart.resolucion(infonut);
                infonutMap.put(pluNbr, value);
            }
            NoteWriter.writeNote(note1FileName, infonutMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Note2
        try {
            Map<Integer, String> infonutMap = new HashMap<>();
            for (Infonut infonut : infonuts) {
                int pluNbr = infonut.getPlu_nbr();
                String value = NotesForWalmart.ingredientes(infonut);
                if (value.length() > 0) {
                    infonutMap.put(pluNbr, value);
                }
            }
            NoteWriter.writeNote(note2FileName, infonutMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Note3
        try {
            Map<Integer, String> infonutMap = new HashMap<>();
            for (Infonut infonut : infonuts) {
                int pluNbr = infonut.getPlu_nbr();
                String value = NotesForWalmart.tablaNutricional(infonut);
                if (value.length() > 0) {
                    infonutMap.put(pluNbr, value);
                }
            }
            NoteWriter.writeNote(note3FileName, infonutMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void transformDataLayouts() {

    }
}
