package com.marsol.sync.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.marsol.sync.model.Infonut;
/*
	Esta clase contiene la función que se encarga de recibir un objeto de Infonut y retornar un String
	parseado de la tabla nutricional.
 */

public class TablaNutricionalCondition {

	public static String checkCondition(Infonut infonut) {

		try {
			int count = 0;
			String tablaNutricional = infonut.getTablaNutricional();
			String porcion = infonut.getPorcion();
			String porcionesxEnvase = infonut.getPorcionesxEnvase();

			//Aqui parte la tabla nutricional
			String subtituloTablaNut = " " + "|" + infonut.getSubtituloTablaNut(); //Se arregla el formato para usar el regex
			String textoEnergia = infonut.getTextoEnergia();
			String textoProteinas = infonut.getTextoProteinas();
			String textoGrasaTotal = infonut.getTextoGrasaTotal();
			String textoGrasaSat = infonut.getTextoGrasaSat();
			String textoGPoliin = infonut.getTextoGPoliin();
			String textoAcGrasosTrans = infonut.getTextoAcGrasosTrans();
			String textoColesterol = infonut.getTextoColesterol();
			String textoHdeCdisp = infonut.getTextoHdeCdisp();
			String textoAzucaresTot = infonut.getTextoAzucaresTot();
			String textoSodio = infonut.getTextoSodio();

			if ("N".equalsIgnoreCase(infonut.getMuestraEnergia())) {
				textoEnergia = "";
				count += 1;
			} else {
				textoEnergia = formateoLineaALinea(textoEnergia) + "{$0A}";
			}
			if ("N".equalsIgnoreCase(infonut.getMuestraProteinas())) {
				textoProteinas = "";
				count += 1;
			} else {
				textoProteinas = formateoLineaALinea(textoProteinas) + "{$0A}";
			}
			if ("N".equalsIgnoreCase(infonut.getMuestraGrasaTotal())) {
				textoGrasaTotal = "";
				count += 1;
			} else {
				textoGrasaTotal = formateoLineaALinea(textoGrasaTotal) + "{$0A}";
			}
			if ("N".equalsIgnoreCase(infonut.getMuestraGrasaSat())) {
				textoGrasaSat = "";
				count += 1;
			} else {
				textoGrasaSat = formateoLineaALinea(textoGrasaSat) + "{$0A}";
			}
			if ("N".equalsIgnoreCase(infonut.getMuestraGPoliin())) {
				textoGPoliin = "";
				count += 1;
			} else {
				textoGPoliin = formateoLineaALinea(textoGPoliin) + "{$0A}";
			}
			if ("N".equalsIgnoreCase(infonut.getMuestraAcGrasosTrans())) {
				textoAcGrasosTrans = "";
				count += 1;
			} else {
				textoAcGrasosTrans = formateoLineaALinea(textoAcGrasosTrans) + "{$0A}";
			}
			if ("N".equalsIgnoreCase(infonut.getMuestraColesterol())) {
				textoColesterol = "";
				count += 1;
			} else {
				textoColesterol = formateoLineaALinea(textoColesterol) + "{$0A}";
			}
			if ("N".equalsIgnoreCase(infonut.getMuestraHdeCdisp())) {
				textoHdeCdisp = "";
				count += 1;
			} else {
				textoHdeCdisp = formateoLineaALinea(textoHdeCdisp) + "{$0A}";
			}
			if ("N".equalsIgnoreCase(infonut.getMuestraAzucaresTot())) {
				textoAzucaresTot = "";
				count += 1;
			} else {
				textoAzucaresTot = formateoLineaALinea(textoAzucaresTot) + "{$0A}";
			}
			if ("N".equalsIgnoreCase(infonut.getMuestraSodio())) {
				textoSodio = "";
				count += 1;
			} else {
				textoSodio = formateoLineaALinea(textoSodio) + "{$0A}";
			}
			if (count == 10) {
				subtituloTablaNut = "";
			} else {
				subtituloTablaNut = formateoLineaALinea(subtituloTablaNut) + "{$0A}";
			}
			String textoPorciones = tablaNutricional + "{$0A}" +
					porcion + "{$0A}" +
					porcionesxEnvase + "{$0A}" +
					"{$0A}";

			String tablaNut = subtituloTablaNut +
					textoEnergia +
					textoProteinas +
					textoHdeCdisp +
					textoGrasaSat +
					textoColesterol +
					textoAcGrasosTrans +
					textoGPoliin +
					textoSodio +
					textoAzucaresTot +
					textoGrasaTotal + "{$0A}";


			String tablaCompleta = textoPorciones + tablaNut;
			if(count == 10){
				return "";
			}
			return tablaCompleta;

		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return null;
		}

	}


	public static String formateoLineaALinea(String linea) {
		int field1Size = 26;
		int field2Size = 9;
		int field3Size = 9;
		String regex = "([^|]+)\\|([^|]+)\\|([^|]+)";
		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(linea);

		if (matcher.matches()) {
			String field1 = matcher.group(1);
			String field2 = matcher.group(2);
			String field3 = matcher.group(3);


			String fixedLenghtField1 = String.format("%-" + field1Size + "s", field1);
			String fixedLenghtField2 = String.format("%-" + field2Size + "s", field2);
			String fixedLenghtField3 = String.format("%-" + field3Size + "s", field3);
			return fixedLenghtField1 + fixedLenghtField2 + fixedLenghtField3;

		} else {
			System.out.println("No match encontrado");
			return null;
		}

	}
}