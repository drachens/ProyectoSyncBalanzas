package com.marsol.sync.utils;

import com.marsol.sync.model.Infonut;

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
        String finalText = resol+textoAlternativo+procedencia;
        return finalText;
    }

    public static String ingredientes(Infonut infonut){
        String ingredientes = infonut.getIngredientes();
        return ingredientes;
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
