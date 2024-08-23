package com.marsol.sync.service.transform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marsol.sync.app.ConfigLoader;
import com.marsol.sync.model.Infonut;
import com.marsol.sync.model.Item;
import com.marsol.sync.model.PLU;
import com.marsol.sync.model.Scale;
import com.marsol.sync.model.structures.HeadersFilesHPRT;
import com.marsol.sync.service.api.InfonutService;
import com.marsol.sync.service.api.ProductService;
import com.marsol.sync.service.io.NoteWriter;
import com.marsol.sync.utils.NotesForWalmart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

    public TransformWalmartPLUs(){
        configLoader = new ConfigLoader();
    }


    @Autowired
    public void setInfonutService(InfonutService infonutService){
        this.infonutService = infonutService;
        //this.productService = productService;
    }
    public void setProductService(ProductService productService){
        this.productService = productService;
    }

    public void setInfonut(List<Infonut> infoNut){
        this.infoNut = infoNut;
    }

    //Método para realizar consulta API y obtener lista de Infonut
    public String fetchInfonutData(int storeNbr, int deptNbr){
        try{
            return infonutService.getInfonut(storeNbr, deptNbr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void transformDataPLUs(Scale scale){
        String filePathPendings = configLoader.getProperty("directory_pendings");
        int storeNbr = scale.getStore();
        int deptNbr = scale.getDepartamento();
        boolean esAutoServicio = scale.getIsEsAutoservicio();
        boolean esDueal = scale.getIsEsDual();
        String pluFilenameOutput = filePathPendings+"PLU_scale.txt";
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss");
        String dateTimeFormated = dateTime.format(formatter);
        String[] header = HeadersFilesHPRT.PLUHeader;
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(pluFilenameOutput))){
            writer.write(String.join("\t",header));
            writer.newLine();
            try{
                Gson gson_items = new Gson();
                String itemsJSON = productService.getItemsDept(storeNbr,deptNbr);
                Type type = new TypeToken<List<Item>>(){}.getType();
                List<Item> items = gson_items.fromJson(itemsJSON, type);

                Gson gson_infonut = new Gson();
                String infonutsJSON = infonutService.getInfonut(storeNbr, deptNbr);
                List<Infonut> infonuts;
                try{
                    Type infonutType = new TypeToken<List<Infonut>>(){}.getType();
                    infonuts = gson_infonut.fromJson(infonutsJSON,infonutType);
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
                    PLU plu = new PLU(pluNbr,itemCode,dept_nbr,name1,"","",label1,0,104,0,unitPrice,weightUnit,tareWeight,dateTimeFormated,0,0,0,0,0,0,0,0,0,0,0,dateTimeFormated,dateTimeFormated);
                    writer.write(plu.toString());
                    writer.newLine();
                }
                System.out.println("PLU.txt creado exitosamente!");

            }catch (IOException e){e.printStackTrace();}
        }catch (IOException e){e.printStackTrace();}
    }

    /*
    @Override
    public void transformDataPLUs(List<Item> items) {
        String filePathPendings = configLoader.getProperty("directory_pendings");
        String pluFilename = filePathPendings+"PLU.txt";
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss");
        String dateTimeFormated = dateTime.format(formatter);
        String[] header = HeadersFilesHPRT.PLUHeader;
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(pluFilename))){
            writer.write(String.join("\t",header));
            writer.newLine();
            try{
                Gson gson = new Gson();
                String json = gson.toJson(items);
                Type itemsType = new TypeToken<List<Item>>(){}.getType();
                List<Item> itemList = gson.fromJson(json, itemsType);
                int store_nbr = itemList.get(0).getStore_nbr();
                int dept_nbr = itemList.get(0).getDept_nbr();

                String infonutJson;
                List<Infonut> infonutsList;
                try{
                    infonutJson = gson.toJson(fetchInfonutData(store_nbr, dept_nbr));
                    Type infonutType = new TypeToken<List<Infonut>>(){}.getType();
                    infonutsList = gson.fromJson(infonutJson,infonutType);
                }catch (Exception e){
                    infonutsList = infoNut;
                }

                Map<Integer, Infonut> infonutMap = new HashMap<>();
                for(Infonut infonut : infonutsList){
                    infonutMap.put(infonut.getPlu_nbr(),infonut);
                }

                for(Item item : itemList){
                    int pluNbr = (int) item.getPlu_nbr();
                    String itemCode = String.valueOf(item.getUpc_nbr()).substring(0,7);
                    int deptNbr = item.getDept_nbr();
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
                        switch (imagenSellosID){
                            case 1:
                                label1 = 1;
                                break;
                            case 2:
                                label1 = 2;
                                break;
                            case 3:
                                label1 = 3;
                                break;
                            case 4:
                                label1 = 4;
                                break;
                            case 5:
                                label1 = 5;
                                break;
                            case 6:
                                label1 = 6;
                                break;
                            case 7:
                                label1 = 7;
                                break;
                            case 8:
                                label1 = 8;
                                break;
                            case 9:
                                label1 = 9;
                                break;
                            case 10:
                                label1 = 10;
                                break;
                            case 11:
                                label1 = 11;
                                break;
                            case 12:
                                label1 = 12;
                                break;
                            case 13:
                                label1 = 13;
                                break;
                            case 14:
                                label1 = 14;
                                break;
                            case 15:
                                label1 = 15;
                                break;
                            default:
                                label1 = 32;
                                break;
                        }
                    }else {label1 = 32;}
                    PLU plu = new PLU(pluNbr,itemCode,deptNbr,name1,"","",label1,0,104,0,unitPrice,weightUnit,tareWeight,dateTimeFormated,0,0,0,0,0,0,0,0,0,0,0,dateTimeFormated,dateTimeFormated);
                    writer.write(plu.toString());
                    writer.newLine();
                }
                System.out.println("Archivo PLU.txt creado exitosamente!");

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

     */
    @Override
    public void transformDataNotes(Scale scale) {
        String filePathPendings = configLoader.getProperty("directory_pendings");
        String note1FileName = filePathPendings + "nota1_scale.txt";
        String note2FileName = filePathPendings + "nota2_scale.txt";
        String note3FileName = filePathPendings + "nota3_scale.txt";
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
