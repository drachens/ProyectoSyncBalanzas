package integration;

import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.domain.service.LabelsTransferService;
import com.marsol.sync.infraestructure.integration.SyncDataDownloader;
import com.marsol.sync.infraestructure.integration.SyncDataLoader;
import com.sun.jna.NativeLibrary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class test {

    @Value("${label.path}")
    private String labeldirectory;

    private SyncDataDownloader syncDataDownloader = new SyncDataDownloader();

    @Test
    public void test2() throws IOException {

        syncDataDownloader.downloadAdvancedBarcodes("C:\\Users\\sistemas\\Desktop\\prueba\\fiambreria\\advancedcode.txt","10.107.127.120");
    }

    @Test
    public void test3() throws IOException, InterruptedException {

        String archivo = "C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\Balanza HPRT\\Proyecto Walmart\\Codigos de barra avanzados\\27062025.txt";
        String ip = "192.168.3.153";
        SyncDataLoader syncDataLoader = new SyncDataLoader();
        syncDataLoader.loadAdvancedBarcodes(archivo,ip);
    }

    @Test
    public void test4() throws IOException, InterruptedException {

//        String archivo = "C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\Balanza HPRT\\Proyecto Walmart\\Default Configuration Balance\\DefaultParametersMarsol.txt";
//        String ip = "192.168.2.239";
//        SyncDataLoader syncDataLoader = new SyncDataLoader();
//        syncDataLoader.loadSystemParameters(archivo,ip,111);

        //syncDataDownloader.downloadSystemParameters("C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\Balanza HPRT\\Proyecto Walmart\\Default Configuration Balance\\DefaultParametersMarsolFromBalanza.txt","192.168.2.239");

        SyncDataLoader syncDataLoader = new SyncDataLoader();
        syncDataLoader.loadSystemParameters("C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\Balanza HPRT\\Proyecto Walmart\\Configuracion de balanzas por defecto\\ImagenesBuenos\\systemParameters..txt","192.168.3.153",111);

    }
    @Test
    public void test44() throws IOException, InterruptedException {

        String archivo = "C:\\Users\\sistemas\\Desktop\\systemparametersdownload2.txt";
        String ip = "10.107.127.109";
        SyncDataDownloader syncDataDownloader = new SyncDataDownloader();
        syncDataDownloader.downloadSystemParameters(archivo,ip);
    }

    @Test
    public void test5() throws IOException, InterruptedException {

                syncDataDownloader.downloadLabel("C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\Balanza HPRT\\Proyecto Walmart\\Default Configuration Balance\\test4.lbl","192.168.2.239");
    }

    @Test
    public void test6() throws IOException, InterruptedException {
        SyncDataLoader syncDataLoader = new SyncDataLoader();
        String path = "C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\Balanza HPRT\\Proyecto Walmart\\Configuracion de balanzas por defecto\\ImagenesBuenos\\systemParameters..txt";
        syncDataLoader.loadSystemParameters(path, "10.105.197.19", 111);

        //ESTO FUNCIONA PERO NO ME DESCARGA LOS MISMO PARAMETROS QUE EL SYNC TOOL
        //syncDataDownloader.downloadSystemParameters("C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\Balanza HPRT\\Proyecto Walmart\\Default Configuration Balance\\AvanceWalmart-18072025.txt", "192.168.2.239");

    }

    @Test
    public void test7() throws IOException, InterruptedException {

        syncDataDownloader.downloadPLU("C:\\Users\\sistemas\\Desktop\\prueba\\test.txt","192.168.2.239");
    }

    @Test
    public void test8() throws IOException, InterruptedException {

        for(int i=1;i<=4;i++){
            syncDataDownloader.downloadNote(i,"C:\\Users\\sistemas\\Desktop\\prueba\\note"+i+".txt","192.168.2.239");
        }
    }

    @Test
    public void test9() throws IOException, InterruptedException{
        SyncDataLoader syncDataLoader = new SyncDataLoader();
        for(int i=1;i<=4;i++){
            syncDataLoader.loadNotes("C:\\Users\\sistemas\\Desktop\\prueba\\note"+i+".txt","192.168.2.239",i);
        }
    }


    @Test
    public void test10() throws IOException, InterruptedException {
        syncDataDownloader.downloadAdvertisement("C:\\Users\\sistemas\\Desktop\\prueba\\publicidad.txt","192.168.2.239");
        //syncDataDownloader.downloadPLU("C:\\Users\\sistemas\\Desktop\\prueba\\PLU.txt","192.168.2.239");
    }


    @Test
    public void test11() throws IOException, InterruptedException {

        String archivo = "C:\\Users\\sistemas\\Desktop\\prueba\\advancedCode.txt";
        String ip = "10.105.197.19";
        SyncDataLoader syncDataLoader = new SyncDataLoader();
        syncDataLoader.loadAdvancedBarcodes(archivo,ip);
    }

    @Test
    public void test12() throws IOException, InterruptedException {

        String archivo = "C:\\Users\\sistemas\\Desktop\\prueba\\advancedCode.txt";
        String ip = "10.105.197.19";
        syncDataDownloader.downloadAdvancedBarcodes(archivo,ip);

    }

    //poubluicad



    //products

    @Test
    public void test13() throws IOException, InterruptedException {



        //syncDataDownloader.downloadPLU("C:\\Users\\sistemas\\Desktop\\prueba\\verdureria\\plu.txt","10.107.127.120");
        SyncDataLoader syncDataLoader = new SyncDataLoader();
        syncDataLoader.loadPLU("C:\\Users\\sistemas\\Desktop\\prueba\\verdureria\\plu.txt","10.105.197.19");

    }


    //downlaod and load plu
    @Test
    public void test14() throws IOException, InterruptedException {



        //syncDataDownloader.downloadPLU("C:\\Users\\sistemas\\Desktop\\prueba\\verdureria\\plu.txt","192.168.2.239");
        SyncDataLoader syncDataLoader = new SyncDataLoader();
        syncDataLoader.loadPLU("C:\\Users\\sistemas\\Desktop\\prueba\\verdureria\\plu.txt","192.168.2.239");

    }



    //LOAD BALANCE FROM ZERO


    @Test
    public void test15() throws IOException, InterruptedException{
        SyncDataDownloader syncDataDownloader = new SyncDataDownloader();
        //Path tempFile = Files.createTempFile("tempFilePlu", ".txt");

        // Muestra la ruta del archivo temporal
        //System.out.println("Archivo temporal creado en: " + tempFile.toString());

        syncDataDownloader.downloadPLU("C:\\Users\\sistemas\\Desktop\\prueba\\xde.txt","192.168.2.239");
    }

    @Test
    public void test16() throws IOException, InterruptedException{
        SyncDataLoader syncDataLoader = new SyncDataLoader();
        syncDataLoader.loadPLU("C:\\Users\\sistemas\\Desktop\\prueba\\xde.txt", "192.168.2.239");
    }

    @Test
    public void test17() throws IOException, InterruptedException{

        syncDataDownloader.downloadAdvancedBarcodes("C:\\Users\\sistemas\\Desktop\\prueba\\advancedcodetestnewsdk.txt", "192.168.2.239");
    }

    @Test
    public void test18() throws IOException, InterruptedException{

        syncDataDownloader.downloadNote(1,"C:\\Users\\sistemas\\Desktop\\prueba\\note1.txt", "192.168.2.239");
        syncDataDownloader.downloadNote(2,"C:\\Users\\sistemas\\Desktop\\prueba\\note1.txt", "192.168.2.239");
        syncDataDownloader.downloadNote(3,"C:\\Users\\sistemas\\Desktop\\prueba\\note1.txt", "192.168.2.239");
        syncDataDownloader.downloadNote(4,"C:\\Users\\sistemas\\Desktop\\prueba\\note1.txt", "192.168.2.239");

    }


    @Test
    public void test19(){

        LabelsTransferService labelsTransferService = new LabelsTransferService();
        List<String> labelsTransferFiles = labelsTransferService.getLabelsFiles("C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\Balanza HPRT\\Proyecto Walmart\\Etiquetas\\Etiquetas Completas 24_06_2025");
        System.out.println(labelsTransferFiles);

    }


    @Test
    public void testPLUDownload(){
        try{
            syncDataDownloader.downloadPLU("C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\Balanza HPRT\\Proyecto Walmart\\DeleteDirectory\\pluDelete_72_94_.txt","10.105.197.19");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testLabelUpload(){
        try{
            SyncDataLoader syncDataLoader = new SyncDataLoader();
            LabelsTransferService labelsTransferService = new LabelsTransferService();
            List<String> labelsTransferFiles =  labelsTransferService.getLabelsFiles("C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\Balanza HPRT\\Proyecto Cencosud\\Etiquetas");
            String Ip = "192.168.3.153";
            for(String label : labelsTransferFiles){
                syncDataLoader = new SyncDataLoader();
                syncDataLoader.loadFormatLabel(label,Ip,111);
                syncDataLoader.loadBackgroundLabel(label, Ip, 111);
                syncDataLoader.loadFileLabel(label,Ip,111);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }









}
