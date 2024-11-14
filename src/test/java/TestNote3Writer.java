import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.marsol.sync.model.Infonut;
import com.marsol.sync.utils.NoteWriter;
import com.marsol.sync.utils.NotesForWalmart;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestNote3Writer {


    @Test
    public void testTablaNut(){
        JsonArray jsonObject = new JsonArray();
        File file = new File("C:\\Users\\sistemas\\Desktop\\MARSOL\\APIWalmart\\jsons_674\\json_infonut_674_98\\json_infonut_674_98");
        try(FileReader reader = new FileReader(file)){
            Gson gson = new Gson();
            jsonObject = gson.fromJson(reader, JsonArray.class);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Infonut> listInfonut = new ArrayList<>();
        Map<Integer,String> infonutMap = new HashMap<>();
        //Crear lista de infonuts
        for(JsonElement item : jsonObject){
            Infonut infonut = new Infonut();
            infonut.setPlu_nbr(item.getAsJsonObject().get("plu_nbr").getAsInt());
            infonut.setStore_nbr(item.getAsJsonObject().get("store_nbr").getAsInt());
            infonut.setDept_nbr(item.getAsJsonObject().get("dept_nbr").getAsInt());
            infonut.setItem_nbr(item.getAsJsonObject().get("item_nbr").getAsInt());
            infonut.setItem_status_code(item.getAsJsonObject().get("item_status_code").getAsString());
            infonut.setEs_etiqueta_propia(item.getAsJsonObject().get("es_etiqueta_propia").getAsBoolean());
            infonut.setEs_texto_Alternativo(item.getAsJsonObject().get("es_texto_Alternativo").getAsBoolean());
            infonut.setTexto_alternativo(item.getAsJsonObject().get("texto_alternativo").getAsString());
            infonut.setProcedencia(item.getAsJsonObject().get("procedencia").getAsString());
            infonut.setAlergernos(item.getAsJsonObject().get("alergenos").getAsString());
            infonut.setResolucion(item.getAsJsonObject().get("resolucion").getAsString());
            infonut.setTablaNutricional(item.getAsJsonObject().get("tablaNutricional").getAsString());
            infonut.setPorcion(item.getAsJsonObject().get("porcion").getAsString());
            infonut.setPorcionesxEnvase(item.getAsJsonObject().get("porcionesxEnvase").getAsString());
            infonut.setSubtituloTablaNut(item.getAsJsonObject().get("subtituloTablaNut").getAsString());
            infonut.setTextoEnergiaContigencia(item.getAsJsonObject().get("textoEnergiaContigencia").getAsString());
            infonut.setMuestraEnergia(item.getAsJsonObject().get("muestraEnergia").getAsString());
            infonut.setTextoEnergia(item.getAsJsonObject().get("textoEnergia").getAsString());
            infonut.setMuestraProteinas(item.getAsJsonObject().get("muestraProteinas").getAsString());
            infonut.setTextoProteinas(item.getAsJsonObject().get("textoProteinas").getAsString());
            infonut.setMuestraGrasaTotal(item.getAsJsonObject().get("muestraGrasaTotal").getAsString());
            infonut.setTextoGrasaTotal(item.getAsJsonObject().get("textoGrasaTotal").getAsString());
            infonut.setMuestraGrasaSat(item.getAsJsonObject().get("muestraGrasaSat").getAsString());
            infonut.setTextoGrasaSat(item.getAsJsonObject().get("textoGrasaSat").getAsString());
            infonut.setMuestraGPoliin(item.getAsJsonObject().get("muestraGPoliin").getAsString());
            infonut.setTextoGPoliin(item.getAsJsonObject().get("textoGPoliin").getAsString());
            infonut.setMuestraAcGrasosTrans(item.getAsJsonObject().get("muestraAcGrasosTrans").getAsString());
            infonut.setTextoAcGrasosTrans(item.getAsJsonObject().get("textoAcGrasosTrans").getAsString());
            infonut.setMuestraColesterol(item.getAsJsonObject().get("muestraColesterol").getAsString());
            infonut.setTextoColesterol(item.getAsJsonObject().get("textoColesterol").getAsString());
            infonut.setMuestraHdeCdisp(item.getAsJsonObject().get("muestraHdeCdisp").getAsString());
            infonut.setTextoHdeCdisp(item.getAsJsonObject().get("textoHdeCdisp").getAsString());
            infonut.setMuestraAzucaresTot(item.getAsJsonObject().get("muestraAzucaresTot").getAsString());
            infonut.setTextoAzucaresTot(item.getAsJsonObject().get("textoAzucaresTot").getAsString());
            infonut.setMuestraSodio(item.getAsJsonObject().get("muestraSodio").getAsString());
            infonut.setTextoSodio(item.getAsJsonObject().get("textoSodio").getAsString());
            infonut.setTaraNutricional(item.getAsJsonObject().get("taraNutricional").getAsInt());
            infonut.setTaraPorcentual(item.getAsJsonObject().get("taraPorcentual").getAsInt());
            infonut.setDiasPerecibilidad(item.getAsJsonObject().get("diasPerecibilidad").getAsInt());
            infonut.setCondicionMantencion(item.getAsJsonObject().get("condicionMantencion").getAsString());
            infonut.setIngredientes(item.getAsJsonObject().get("ingredientes").getAsString());
            infonut.setRefresh(item.getAsJsonObject().get("refresh").getAsBoolean());
            infonut.setTextoRefresh(item.getAsJsonObject().get("textoRefresh").getAsString());
            infonut.setProductoCongelado(item.getAsJsonObject().get("productoCongelado").getAsBoolean());
            infonut.setTextoCongelado(item.getAsJsonObject().get("textoCongelado").getAsString());
            infonut.setImportado(item.getAsJsonObject().get("importado").getAsBoolean());
            infonut.setTextoImportado(item.getAsJsonObject().get("textoImportado").getAsString());
            infonut.setLlevaInfoLocal(item.getAsJsonObject().get("llevaInfoLocal").getAsBoolean());
            infonut.setImagenSellos(item.getAsJsonObject().get("imagenSellos").getAsString());

            listInfonut.add(infonut);
        }

        for(Infonut info : listInfonut){
            int pluNbr = info.getPlu_nbr();
            String value = NotesForWalmart.tablaNutricional(info);
            if(!value.isEmpty()){
                infonutMap.put(pluNbr,value);
            }
        }
        NoteWriter.writeNote("C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\nota3_tabla_nut_test.txt",infonutMap);
    }


}
