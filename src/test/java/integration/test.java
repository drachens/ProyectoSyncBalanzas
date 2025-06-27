package integration;

import com.marsol.sync.infraestructure.integration.SyncDataDownloader;
import com.marsol.sync.infraestructure.integration.SyncDataLoader;
import com.sun.jna.NativeLibrary;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;


public class test {

    private SyncDataDownloader syncDataDownloader = new SyncDataDownloader();
    private SyncDataLoader syncDataLoader = new SyncDataLoader();
    @Test
    public void test2() throws IOException {
        syncDataDownloader.downloadAdvancedBarcodes("C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\Balanza HPRT\\Proyecto Walmart\\Codigos de barra avanzados\\24062025.txt","10.107.127.120");
    }

    @Test
    public void test3() throws IOException {
        syncDataLoader.loadAdvancedBarcodes("C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\Balanza HPRT\\Proyecto Walmart\\Codigos de barra avanzados\\27062025.txt","10.105.197.19");
    }



}
