package com.marsol.sync.utils;

import com.marsol.sync.model.Infonut;

import java.util.ArrayList;
import java.util.List;

/*
    AGREGAR CONDICION DE MANTENCIÓN
 */

public class NotesForWalmart {

    public static String resolucion(Infonut infonut){
        String resol = infonut.getResolucion()+"{$0A}";
        boolean isTextoAlternativo = infonut.isEs_texto_Alternativo();
        String procedencia = infonut.getProcedencia();
        String textoAlternativo = "";
        if(isTextoAlternativo){
            textoAlternativo = infonut.getTexto_alternativo()+"{$0A}";
        }
        String textoResolucion = resol+textoAlternativo+procedencia;
        if(!infonut.getCondicionMantencion().isEmpty()){
            String condicion = "{$0A}"+infonut.getCondicionMantencion();
            textoResolucion = textoResolucion+condicion;
            return textoResolucion;
        }
        return textoResolucion;
    }

    public static String ingredientes(Infonut infonut){
        String ingredientes = infonut.getIngredientes();
        return ingredientes;
    }

    public static List<String> ingredientes2(Infonut infonut){
        List<String> ingredientesArray = new ArrayList<>();
        String ingredientes = infonut.getIngredientes();
        if(ingredientes.length()>1000){
            ingredientesArray.add(ingredientes.substring(0,999));
            ingredientesArray.add(ingredientes.substring(1000));
        }else{
            ingredientesArray.add(ingredientes);
        }
        return ingredientesArray;
    }

    public static String tablaNutricional(Infonut infonut){
        boolean esEtiquetaPropia = infonut.isEs_etiqueta_propia();
        String tabla_Nuricional;
        if(!esEtiquetaPropia){
            tabla_Nuricional = TablaNutricionalCondition.checkCondition(infonut);
        } else {tabla_Nuricional = "";}
        return tabla_Nuricional;
    }
}
