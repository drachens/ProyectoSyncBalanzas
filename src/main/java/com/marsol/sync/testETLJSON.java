package com.marsol.sync;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.marsol.sync.app.ConfigLoader;
import com.marsol.sync.model.Infonut;
import com.marsol.sync.model.Item;
import com.marsol.sync.model.Layout;
import com.marsol.sync.model.Scale;
import com.marsol.sync.service.SFTP.CargarImagenesAPI;
import com.marsol.sync.service.SFTP.CargarImagenesJSON;
import com.marsol.sync.service.SFTP.ClientSFTP;
import com.marsol.sync.service.api.*;
import com.marsol.sync.service.communication.SyncSDKDefine;
import com.marsol.sync.service.communication.SyncSDKImpl;
import com.marsol.sync.service.communication.TSDKOnProgressEvent;
import com.marsol.sync.service.transform.TransformWalmartNotes;
import com.marsol.sync.service.transform.TransformWalmartPLUs;
import org.springframework.web.client.RestTemplate;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class testETLJSON {
    String rutaBase = "C:\\Users\\Drach\\Desktop\\MARSOL\\APIWalmart";
    int storeNbr;
    int deptNbr;
    RestTemplate restTemplate = new RestTemplate();
    ConfigLoader configLoader = new ConfigLoader();
    AuthService authService = new AuthService(restTemplate, configLoader);
    InfonutService infonutService = new InfonutService(new ApiService<>(restTemplate,authService,configLoader));
    ScaleService scaleService = new ScaleService(new ApiService<Scale>(restTemplate,authService,configLoader), restTemplate,configLoader);
    TransformWalmartPLUs walmartPLU = new TransformWalmartPLUs();
    TransformWalmartNotes walmartNotes = new TransformWalmartNotes();
    CargarImagenesJSON cargarImagenesJSON = new CargarImagenesJSON();
    CargarImagenesAPI cargarImagenesAPI = new CargarImagenesAPI();

    //Extracción de data (JSON)
    public List<String> createRoute(int storeNbr, int deptNbr){
        String routeInfonut = "json_infonut_"+storeNbr+"_"+deptNbr;
        String routeItems = "json_items_"+storeNbr+"_"+deptNbr;
        String jsonItemsPath = rutaBase+"\\jsons_"+storeNbr+"\\"+routeItems+"\\"+routeItems;
        String jsonInfonutPath = rutaBase+"\\jsons_"+storeNbr+"\\"+routeInfonut+"\\"+routeInfonut;
        List<String> paths = new ArrayList<String>();
        paths.add(jsonInfonutPath);
        paths.add(jsonItemsPath);
        return paths;
    }
    public void createScaleWalmart(Scale scale){
        System.out.println("Creando balanza en servicios walmart...\n");
        try{
            scaleService.createScale(scale);
            System.out.println("Balanza creado exitosamente");
        } catch (Exception e){System.out.println("Error al crear la balanza.");}
    }

    public String getScalesByMarca(String marca){
        System.out.println("Obteniendo lista de balanzas marca "+marca+"...\n");
        String scales = scaleService.getScalesByMarca(marca);
        System.out.println("Lista de balanzas marca "+marca+" obtenida.\n");
        //ObjectMapper mapper = new ObjectMapper();

        return scales;
    }

    //Transform data desde API
    public void transformDataAPI(Scale scale){
        RestTemplate restTemplate = new RestTemplate();
        AuthService authService = new AuthService(restTemplate, configLoader);
        ApiService apiService = new ApiService(restTemplate,authService, configLoader);
        ProductService productService = new ProductService(apiService);
        InfonutService infonutService = new InfonutService(apiService);
        LayoutService layoutService = new LayoutService(restTemplate);

        //List<Item> items = productService.getItemsDept(storeNbr, deptNbr);
        //List<Infonut> infonuts = infonutService.getInfonut(storeNbr, deptNbr);
        walmartPLU.setInfonutService(infonutService);
        walmartPLU.setProductService(productService);
        walmartNotes.setInfonutService(infonutService);
        walmartNotes.transformDataNotes(scale);
        walmartPLU.transformDataPLUs(scale);
        //walmartNotes.transformDataNotes(infonuts);



        //walmartPLU.transformDataPLUs(items);
    }

    //Transformación de data
    public void transformData(List<String> paths){
        String itemPath = paths.get(1);
        String infonutPath = paths.get(0);

        Gson gson2 = new Gson();
        try(FileReader reader = new FileReader(infonutPath)){
            Type infonutListType = new TypeToken<List<Infonut>>(){}.getType();
            List<Infonut> infonuts = gson2.fromJson(reader,infonutListType);
            //walmartNotes.transformDataNotes(infonuts);
            Gson gson = new Gson();
            try(FileReader reader2 = new FileReader(itemPath)){
                Type itemListType = new TypeToken<List<Item>>(){}.getType();
                List<Item> items = gson.fromJson(reader2,itemListType);

                try{
                    String username = configLoader.getProperty("SFTP_username");
                    String password = configLoader.getProperty("SFTP_password");
                    String host = configLoader.getProperty("SFTP_host");
                    int port = configLoader.getIntProperty("SFTP_port");
                    cargarImagenesJSON.setClientSFTP(username, password, host, port);
                    List<Layout> layouts = cargarImagenesJSON.getLayoutJSON(674,98);
                    //System.out.println("Layouts: "+layouts);
                    cargarImagenesJSON.cargarImagen(layouts);
                } catch (SftpException e) {
                    throw new RuntimeException(e);
                } catch (JSchException e) {
                    throw new RuntimeException(e);
                }
                walmartPLU.setInfonut(infonuts);
                //walmartPLU.transformDataPLUs(items);
            }catch (IOException e){e.printStackTrace();}
        }catch(IOException e){e.printStackTrace();}{}

    }

    //Carga de Data
    public void cargarData(){
        String pluFile = "PLU_scale.txt";
        String note1File = "nota1_scale.txt";
        String note2File = "nota2_scale.txt";
        String note3File = "nota3_scale.txt";
        System.out.println("\nCargando...");
        SyncSDKImpl sync = new SyncSDKImpl();
        SyncSDKImpl sync2 = new SyncSDKImpl();
        sync.INSTANCE.SDK_Initialize();
        int ip = SyncSDKDefine.ipToLong(configLoader.getProperty("SFTP_host"));
        String ruta_base = configLoader.getProperty("directory_pendings");

        TSDKOnProgressEvent onProgress = new TSDKOnProgressEvent() {
            @Override
            public void callback(int var1, int var2, int var3, int var4) {
                System.out.println("ErrorCode:" + var1  + " nIndex:" +var2 + " nTotal:" + var3 + " nUserDataCode:" + var4 );
            }
        };
        try{
            System.out.println("\nCargando PLU's");
            long result = sync.INSTANCE.SDK_ExecTaskA(ip, 0, 0, ruta_base+pluFile,onProgress,111);
            sync.INSTANCE.SDK_WaitForTask(result);
            System.out.println("\nPLUS Cargados.");

            //Thread.sleep(5000);

            System.out.println("\nCargando Notas1");
            long result2 = sync.INSTANCE.SDK_ExecTaskA(ip, 0, 5, ruta_base+note1File,onProgress,111);
            sync.INSTANCE.SDK_WaitForTask(result2);
            System.out.println("\nNotas1 Cargadas.");

            //Thread.sleep(10000);

            System.out.println("\nCargando Notas2");
            long result3 = sync2.INSTANCE.SDK_ExecTaskA(ip, 0, 6, ruta_base+note2File,onProgress,111);
            sync2.INSTANCE.SDK_WaitForTask(result3);
            System.out.println("\nNotas2 Cargadas.");

            //Thread.sleep(3000);

            System.out.println("\nCargando Notas3");
            long result4 = sync2.INSTANCE.SDK_ExecTaskA(ip, 0, 7, ruta_base+note3File,onProgress,112);
            sync2.INSTANCE.SDK_WaitForTask(result4);
            System.out.println("\nNotas3 Cargadas.");

        }catch(Exception e){e.printStackTrace();}

    }
    public void deleteNote(String ipString, int tipoNota){
        System.out.println("\nIniciando proceso de borrado de nota "+tipoNota+"");
        TSDKOnProgressEvent onProgress = new TSDKOnProgressEvent() {
            @Override
            public void callback(int var1, int var2, int var3, int var4) {
                System.out.println("ErrorCode:" + var1 + " nIndex:" + var2 + " nTotal:" + var3 + " nUserDataCode:" + var4);
            }
        };
        int ip = SyncSDKDefine.ipToLong(ipString);
        int nota;
        switch (tipoNota){
            case 1:
                nota = 5;
                break;
            case 2:
                nota = 6;
                break;
            case 3:
                nota = 7;
                break;
            case 4:
                nota = 8;
                break;
            default:
                nota = 8;
                break;
        }
        SyncSDKImpl sync = new SyncSDKImpl();
        sync.SDK_Initialize();
        sync.SDK_ExecTaskA(ip,2,nota,"",onProgress,111);
        sync.SDK_Finalize();
        System.out.println("\nNota "+tipoNota+" eliminada.");
    }

    public void deleteAllPLUs(String ipString) {

        System.out.println("\nIniciando proceso de borrado de PLUs...");

        SyncSDKImpl sync = new SyncSDKImpl();
        TSDKOnProgressEvent onProgress = new TSDKOnProgressEvent() {
            @Override
            public void callback(int var1, int var2, int var3, int var4) {
                System.out.println("ErrorCode:" + var1 + " nIndex:" + var2 + " nTotal:" + var3 + " nUserDataCode:" + var4);
            }
        };
        int ip = SyncSDKDefine.ipToLong(ipString);

        sync.INSTANCE.SDK_Initialize();
        long result = sync.INSTANCE.SDK_ExecTaskA(ip, 2, 0, "", onProgress, 112);
        sync.INSTANCE.SDK_WaitForTask(result);
        sync.INSTANCE.SDK_Finalize();
        System.out.println("\nProceso de eliminación realizado");


    }


    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        testETLJSON test = new testETLJSON();

        String ipString = "10.105.197.16";
        test.deleteAllPLUs(ipString);
        test.deleteNote(ipString,1);
        test.deleteNote(ipString,2);
        test.deleteNote(ipString,3);
        Scale scale = new Scale(0,72,"Laboratorio",
                "bTest",94,ipString,"HPRT",
                "C-L9AI",true,true,
                true, false, "test","now","italo",
                false,false,false,ipString,false);

        test.transformDataAPI(scale);
        test.cargarData();
        long endTime = System.currentTimeMillis();
        System.out.println("Elapsed time: " + (endTime - startTime));
        /*
        long startTime = System.nanoTime();
        String ipString = "10.105.197.16";
        testETLJSON obj = new testETLJSON();
        boolean crearBalanza = false;
        boolean usingWS = true;

        //Si estamos trabajando de la red de walmart
        if(usingWS){
            Scale scale = new Scale(0,72,"Laboratorio",
                    "bTest",98,ipString,"HPRT",
                    "C-L9AI",true,true,
                    true, false, "test","now","italo");
            if(crearBalanza){
                obj.createScaleWalmart(scale);
            }
            List<Scale> scales = obj.getScalesByMarca("HPRT");
            for(Scale s : scales){
                int storeNbr = s.getStore();
                int deptNbr = s.getDepartamento();
                String ipBalanza = s.getIp_Balanza();
                boolean esAutoservicio = s.getIsEsAutoservicio();
            }
        }



         */
        /*
        obj.deleteAllPLUs(ipString);
        obj.deleteNote(ipString,1);
        obj.deleteNote(ipString,2);
        obj.deleteNote(ipString,3);

        //List<String> paths = obj.createRoute(674,98);
        obj.transformDataAPI(674,94);
        obj.cargarData();
         */
        //long endTime = System.nanoTime();
        //long executionTime = (endTime - startTime)/1_000_000;
        //System.out.println("Tiempo de ejecución: "+executionTime+" (ms)");

    }
}
