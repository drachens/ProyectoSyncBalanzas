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
import java.nio.charset.StandardCharsets;
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
    private final LogService logService;
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
    //@Autowired
    //public void setInfonut(List<Infonut> infoNut){
    //    this.infoNut = infoNut;
    //}
    @Autowired
    public void setLayoutService(LayoutService layoutService) {
        this.layoutService = layoutService;
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
        List<Item> items = new ArrayList<>();

        if(esAutoServicio){
            logger.info("[TransformWalmartPLUs] Creando documento para balanza de autoservicio...");
            if(!esDual){
                logger.info("[TransformWalmartPLUs] Balanza de autoservicio del departamento: {}",scale.getDepartamento());//Si es autoservicio y no es dual, es solo de un departamento.
                try{
                    //Obtener Layouts de productos
                    items = getProductListAutoservicio(storeNbr,deptNbr);
                    logger.info("[TransformWalmartPLUs] Cantidad de productos obtenidos: {}", items.size());
                }catch (Exception e){
                    logger.error("[TransformWalmartPLUs] Error: {}", e.getMessage());
                }
            }else{
                logger.info("[TransformWalmartPLUs] Balanza de autoservicio Dual.");
                items = getProductListAutoservicio(storeNbr,deptNbr);
                logger.info("[TransformWalmartPLUs] Cantidad de productos obtenidos: {}", items.size());
            }
        }else{
            logger.info("[TransformWalmartPLUs] Balanza de venta asistida, departamento: {}",scale.getDepartamento());
            Gson gson_items = new Gson();
            String itemsJSON = productService.getItemsDept(storeNbr,deptNbr);
            Type type = new TypeToken<List<Item>>(){}.getType();
            items = gson_items.fromJson(itemsJSON, type);
            logger.info("[TransformWalmartPLUs] Cantidad de producto obtenidos: {}", items.size());
        }

        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))){
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
                    infonuts = new ArrayList<Infonut>();
                }
                Map<Integer, Infonut> infonutMap = new HashMap<>();
                for(Infonut infonut : infonuts){
                    infonutMap.put(infonut.getPlu_nbr(),infonut);
                }
                for(Item item : items){
                    int pluNbr = (int) item.getPlu_nbr();
                    int weightUnit;
                    int unitPrice = item.getSell_price();
                    int tareWeight = 0;
                    int dept_nbr = item.getDept_nbr();
                    String itemCode = String.valueOf(item.getUpc_nbr()).substring(0,7);
                    String name1 = item.getItem1_desc();
                    String itemStatusCode = item.getItem_status_code();
                    weightUnit = switch (itemStatusCode) {
                        case "A" -> 0; //Unidad de Kg=0
                        case "I" -> 8; //Unidad por piezas=8
                        default -> 0;
                    };
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
            logger.info("Creando Nota 1 ...");
            Map<Integer, String> infonutMap = new HashMap<>();
            for (Infonut infonut : infonuts) {
                int pluNbr = infonut.getPlu_nbr();
                String value = NotesForWalmart.resolucion(infonut);
                infonutMap.put(pluNbr, value);
            }
            NoteWriter.writeNote(note1FileName, infonutMap);
            logger.info("Nota 1 creada exitosamente.");
        } catch (Exception e) {
            logger.error("Error creando Nota 1: {}",e.getMessage());
        }
        //Note2
        try {
            logger.info("Creando Nota 2 ...");
            Map<Integer, String> infonutMap = new HashMap<>();
            for (Infonut infonut : infonuts) {
                int pluNbr = infonut.getPlu_nbr();
                String value = NotesForWalmart.ingredientes(infonut);
                if (value.length() > 0) {
                    infonutMap.put(pluNbr, value);
                }
            }
            NoteWriter.writeNote(note2FileName, infonutMap);
            logger.info("Nota 2 creada exitosamente.");
        } catch (Exception e) {
            logger.error("Error creando Nota 2: {}",e.getMessage());
        }
        //Note3
        try {
            logger.info("Creando Nota 3 ...");
            Map<Integer, String> infonutMap = new HashMap<>();
            for (Infonut infonut : infonuts) {
                int pluNbr = infonut.getPlu_nbr();
                String value = NotesForWalmart.tablaNutricional(infonut);
                if (value.length() > 0) {
                    infonutMap.put(pluNbr, value);
                }
            }
            NoteWriter.writeNote(note3FileName, infonutMap);
            logger.info("Nota 3 creada exitosamente.");
        } catch (Exception e) {
            logger.error("Error creando Nota 3: {}",e.getMessage());
        }
    }

    @Override
    public void transformDataPLUsAutoservicio(Scale scale) {

    }

    public List<Item> getProductListAutoservicio(int storeNbr, int deptNbr){
        logger.info("[TransformWalmartPLUs] Obteniendo lista de producto para balanza de autoservicio...");
        List<Item> items = new ArrayList<>();
        Gson gson_layout = new Gson();
        Gson gson_productos = new Gson();
        Map<Integer,Item> productMap = new HashMap<>();
        String layoutJSON = layoutService.getLayout(storeNbr, deptNbr);
        String productJSON = productService.getItemsDept(storeNbr, deptNbr);
        List<Layout> layouts = gson_layout.fromJson(layoutJSON, new TypeToken<List<Layout>>(){}.getType());
        List<Item> products = gson_productos.fromJson(productJSON, new TypeToken<List<Item>>(){}.getType());
        for(Item product : products){
            productMap.put((int) product.getPlu_nbr(), product);
        }
        //Si el PLU de layout está en el mapa -> Agregar Item a List<Item>
        for(Layout layout : layouts){
            Item item_layout = productMap.get(layout.getPlu());
            if(item_layout != null){
                items.add(item_layout);
            }
        }
        logger.info("[TransformWalmartPLUs] Lista de productos obtenida para tienda: {} y departamento: {}",storeNbr,deptNbr);
        return items;
    }

}
