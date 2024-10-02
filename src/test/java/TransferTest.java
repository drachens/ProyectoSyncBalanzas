import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.marsol.sync.model.Item;
import com.marsol.sync.model.Layout;
import com.marsol.sync.model.Scale;
import com.marsol.sync.service.images.Transfer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
/*
@RunWith(SpringRunner.class)
public class TransferTest {


    public void testTransfer() {
        String targetUrl = "http://192.168.2.33:5000/upload";
        String imagePath = "C:\\Users\\sistemas\\Desktop\\imagenes\\PLU_94_00003.jpg";
        Transfer.uploadImage(targetUrl,imagePath,"00003.jpg");
    }

    public static void listImages() {
        String targetUrl = "http://192.168.2.33:5000";
        List<Integer> listaImagenes = Transfer.listarImagenes(targetUrl);
        System.out.println(listaImagenes);
    }
    @Test
    public void cargarLayout() throws IOException {
        int id = 0, store = 0, departamento = 1; // Aquí inicializas todas las variables
        String formato = "valorFormato",
                nombre = "valorNombre",
                marca = "valorMarca",
                modelo = "valorModelo",
                status = "valorStatus",
                lastUpdate = "valorLastUpdate",
                userUpdate = "sas"; // Solo userUpdate se inicializa con "sas"        boolean esAutoservicio = true;
        boolean cargaMaestra = true;
        boolean cargaLayout = true;
        boolean esDual = true;
        boolean isCargaLayout = true;
        boolean isEsDual = true;
        boolean isCargaMaestra = true;
        boolean isEsAutoservicio = true;
        boolean esAutoservicio = true;
        String ip_Balanza = "192.168.2.33"; // Inicializa ip_Balanza
        String iP_Balanza = "192.168.2.33"; // Inicializa iP_Balanza
        Scale scale = new Scale(id,store,formato,nombre,departamento,iP_Balanza,marca,modelo,esAutoservicio,cargaMaestra,cargaLayout,esDual,status,lastUpdate,userUpdate,isCargaLayout,isEsDual,isCargaMaestra,ip_Balanza,isEsAutoservicio);

        //Layout
        Gson gson_items = new Gson();
        String filepath="C:\\Users\\sistemas\\Desktop\\MARSOL\\APIWalmart\\jsons_57\\json_flashkeys_vegetales_57\\json_flashkeys_vegetales_57";
        String jsonString = new String(Files.readAllBytes(Paths.get(filepath)));
        Type type = new TypeToken<List<Layout>>(){}.getType();
        List<Layout> layouts = gson_items.fromJson(jsonString, type);
        Transfer.cargarLayout(scale,layouts);

    }

}


 */