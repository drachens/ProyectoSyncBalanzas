import com.marsol.sync.service.communication.SyncDataLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
public class SyncDataLoaderTest {


    public void testLoadCustomBarcode(){
        SyncDataLoader loader = new SyncDataLoader();
        String filename = "C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\CodigoBarras_QR.txt";
        if(loader.loadCustomBarcode(filename,"10.105.197.125")){
            System.out.println("Custom Barcode Loaded");
        }else{
            System.out.println("Custom Barcode Not Loaded");
        }
    }
    @Test
    public void testLoadLabel(){
        SyncDataLoader loader = new SyncDataLoader();
        List<String> formatos = new ArrayList<>();
        formatos.add("acuenta");
        formatos.add("express");
        formatos.add("lider_2");
        formatos.add("mayorista");
        System.out.println("Formato: "+formatos.get(2));
        String folderLabel = "C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\Balanza Lab\\Etiquetas\\"+formatos.get(2);
        File folder = new File(folderLabel);
        if(folder.exists() && folder.isDirectory()){
            File[] files = folder.listFiles();
            if(files != null){
                for(File file: files){
                    String filename = file.getAbsolutePath();
                    loader.loadLabel(filename,"192.168.5.102");
                }
            }
        }
    }
}
