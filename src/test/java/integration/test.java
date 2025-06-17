package integration;

import com.marsol.sync.infraestructure.integration.SyncDataLoader;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


public class test {

    private SyncDataLoader syncDataLoader = new SyncDataLoader();

    @Test
    public void test2(){
        syncDataLoader.loadPLU("C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\pendings\\plu_72_80.txt","192.168.2.117");
    }


}
