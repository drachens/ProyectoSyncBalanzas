package com.marsol.sync.utils;

import com.google.gson.*;

import java.util.HashSet;
import java.util.Set;

public class ProductFilter {

    /**
     *  Funcion que
     * @param productJSON
     * @param infonutJSON
     * @return
     */

    public static String filtrar(JsonObject productJSON, JsonObject infonutJSON){
        Set<Integer> pluSet = new HashSet<Integer>();
        JsonArray filteredArray = new JsonArray();

        JsonArray arrayProducts = productJSON.getAsJsonArray("products");
        JsonArray arrayInfonuts = infonutJSON.getAsJsonArray("infonuts");

        // Agregar los plu_nbr de Infonut al Set para búsquedas rápidas
        for(JsonElement arrayInfonut : arrayInfonuts){
            JsonObject infonut = arrayInfonut.getAsJsonObject();
            int plu_nbr = infonut.get("plu_nbr").getAsInt();
            pluSet.add(plu_nbr);
        }
        // Filtrar productos que tienen plu_nbr en el Set
        for(JsonElement arrayProduct : arrayProducts){
            JsonObject product = arrayProduct.getAsJsonObject();
            int plu_nbr2 = product.get("plu_nbr").getAsInt();
            if(pluSet.contains(plu_nbr2)){
                filteredArray.add(product);
            }
        }
        Gson gson = new Gson();
        return gson.toJson(filteredArray);
    }

}
