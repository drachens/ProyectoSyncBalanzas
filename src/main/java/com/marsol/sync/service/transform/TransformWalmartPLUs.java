package com.marsol.sync.service.transform;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marsol.sync.MainClass;
import com.marsol.sync.model.*;
import com.marsol.sync.model.structures.HeadersFilesHPRT;
import com.marsol.sync.service.api.InfonutService;
import com.marsol.sync.service.api.LayoutService;
import com.marsol.sync.service.api.LogService;
import com.marsol.sync.service.api.ProductService;
import com.marsol.sync.utils.NoteWriter;
import com.marsol.sync.utils.NotesForWalmart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(TransformWalmartPLUs.class);
    private InfonutService infonutService;
    private List<Infonut> infoNut;
    private LayoutService layoutService;
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

    public TransformWalmartPLUs(LogService logService) {
        this.logService = logService;
    }

    @Autowired
    public void setInfonutService(InfonutService infonutService){
        this.infonutService = infonutService;
    }
    @Autowired
    public void setProductService(ProductService productService){
        this.productService = productService;
    }
    @Autowired
    public void setInfonut(List<Infonut> infoNut){
        this.infoNut = infoNut;
    }
    @Autowired
    public void setLayoutService(LayoutService layoutService) {
        this.layoutService = layoutService;
    }

    @Override
    public void transformDataPLUsAsistida(Scale scale){
        int storeNbr = scale.getStore();
        int deptNbr = scale.getDepartamento();
        LocalDateTime dateTime = LocalDateTime.now();
        boolean esAutoServicio = scale.getIsEsAutoservicio();
        boolean esDual = scale.getIsEsDual();
        String filename = String.format("%splu_%s_%s.txt",directoryPendings,scale.getStore(),scale.getDepartamento());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormatter);
        String dateTimeFormated = dateTime.format(formatter);
        String[] header = HeadersFilesHPRT.PLUHeader;
        List<Item> items = new ArrayList<>();

        if(esAutoServicio){
            logger.info("Creando documento para balanza de autoservicio...");
            if(!esDual){ //Si es autoservicio y no es dual, es solo de un departamento.
                try{
                    //Obtener Layouts de productos
                    Gson gson_layouts = new Gson();
                    String layoutJSON = layoutService.getLayout(storeNbr,deptNbr);
                    List<Layout> layouts = gson_layouts.fromJson(layoutJSON, new TypeToken<List<Layout>>(){}.getType());
                    //Crear un Mapa <PLU,item> para buscar items y agregarlo a la lista.
                    Gson gson_products = new Gson();
                    String productJSON = productService.getItemsDept(storeNbr,deptNbr);
                    List<Item> products = gson_products.fromJson(productJSON, new TypeToken<List<Item>>(){}.getType());
                    Map<Integer,Item> productsMap = new HashMap<>();
                    for(Item product: products){
                        productsMap.put((int) product.getPlu_nbr(), product);
                    }
                    //Si el PLU de layout está en el mapa, se agrega el item a la lista de items.
                    for(Layout layout: layouts){
                        Item item_layout = productsMap.get(layout.getPlu());
                        if(item_layout!=null){
                            items.add(item_layout);
                        }
                    }
                    logger.info("ITEMS AGREGADOS:{}", items.size());
                }catch (Exception e){
                    logger.error("Error: {}", e.getMessage());
                }
            }else{
                //Si es dual, entonces hay que enviar los 2 departamentos.

            }
        }else{
            Gson gson_items = new Gson();
            String itemsJSON = productService.getItemsDept(storeNbr,deptNbr);
            Type type = new TypeToken<List<Item>>(){}.getType();
            items = gson_items.fromJson(itemsJSON, type);
        }

        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"))){
            writer.write(String.join("\t",header));
            writer.newLine();
            try{
                Gson gson_infonut = new Gson();
                String infonutsJSON = infonutService.getInfonut(storeNbr, deptNbr);
                List<Infonut> infonuts;
                try{
                    Type infonutType = new TypeToken<List<Infonut>>(){}.getType();
                    infonuts = gson_infonut.fromJson(infonutsJSON,infonutType);
                    now = LocalDateTime.now().format(formatter);
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
                        String ingredientes = infonut.getIngredientes();
                        boolean esEtiquetaPropia = infonut.isEs_etiqueta_propia();
                        int imagenSellosID = Integer.parseInt(imagenSellos);
                        if(imagenSellosID>=1 && imagenSellosID<=15){
                            label1=imagenSellosID;
                        }else{
                            if(!ingredientes.isEmpty()){
                                if(dept_nbr == 94){
                                    label1=32;
                                } else if (!esEtiquetaPropia) {
                                    label1=30;
                                } else{label1=31;}
                            }else{
                                label1=32;
                            }
                        }
                    }else {label1 = 32;}
                    PLU plu = new PLU(pluNbr,itemCode,dept_nbr,name1,label1,
                            104,unitPrice,weightUnit,tareWeight,
                            dateTimeFormated,dateTimeFormated,dateTimeFormated);

                    writer.write(plu.toString());
                    writer.newLine();
                }
                System.out.println("PLU.txt creado exitosamente!");

            }catch (IOException e){
                logger.error("Error: {}",e.getMessage());
            }
        }catch (IOException e){logger.error("Error: {}",e.getMessage());}
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
    public void transformDataPLUsAutoservicio(Scale scale) {

    }

}
