package integration;

import com.marsol.sync.infraestructure.integration.SyncDataDownloader;
import com.marsol.sync.infraestructure.integration.SyncDataLoader;
import com.sun.jna.NativeLibrary;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class test {

    private SyncDataDownloader syncDataDownloader = new SyncDataDownloader();

    @Test
    public void test2() throws IOException {

        syncDataDownloader.downloadAdvancedBarcodes("C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\Balanza HPRT\\Proyecto Walmart\\Codigos de barra avanzados\\24062025.txt","192.168.2.239");
    }

    @Test
    public void test3() throws IOException, InterruptedException {

        String archivo = "C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\Balanza HPRT\\Proyecto Walmart\\Codigos de barra avanzados\\27062025.txt";
        String ip = "192.168.2.239";
        SyncDataLoader syncDataLoader = new SyncDataLoader();
        syncDataLoader.loadAdvancedBarcodes(archivo,ip);
    }

    @Test
    public void test4() throws IOException, InterruptedException {

        String archivo = "C:\\Users\\sistemas\\Desktop\\prueba\\systemparametersdownload.txt";
        String ip = "10.105.197.19";
        SyncDataLoader syncDataLoader = new SyncDataLoader();
        syncDataLoader.loadSystemParameters(archivo,ip,111);

    }
    @Test
    public void test44() throws IOException, InterruptedException {

        String archivo = "C:\\Users\\sistemas\\Desktop\\prueba\\systemparametersdownload.txt";
        String ip = "10.105.197.19";
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
        syncDataLoader.loadSystemParameters("C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\Balanza HPRT\\Proyecto Walmart\\Default Configuration Balance\\DefaultParameters.txt", "10.107.127.120", 111);

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











}
