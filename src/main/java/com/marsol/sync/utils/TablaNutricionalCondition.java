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
			//String textoEnergiaContingencia = infonut_3.getTextoEnergiaContigencia();
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
		/*
			List<String[]> table = new ArrayList<>();
			table.add(despedazarString(subtituloTablaNut));
			table.add(despedazarString(textoEnergia));
			table.add(despedazarString(textoProteinas));
			table.add(despedazarString(textoGrasaTotal));
			table.add(despedazarString(textoGrasaSat));
			table.add(despedazarString(textoGPoliin));
			table.add(despedazarString(textoAcGrasosTrans));
			table.add(despedazarString(textoColesterol));
			table.add(despedazarString(textoHdeCdisp));
			table.add(despedazarString(textoAzucaresTot));
			table.add(despedazarString(textoSodio));

			String formattedTable = formatTable(table);
			System.out.println("Formatted table: \n");
			System.out.println(formattedTable);


			 */

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
			return tablaCompleta;

		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return null;
		}

	}


	public static String formateoLineaALinea(String linea) {
		//int separatorSize = 3;
		int field1Size = 30;
		int field2Size = 15;
		int field3Size = 15;
		//linea.replace(" ", "");
		String regex = "([^|]+)\\|([^|]+)\\|([^|]+)";
		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(linea);

		if (matcher.matches()) {
			String field1 = matcher.group(1);
			String field2 = matcher.group(2);
			String field3 = matcher.group(3);


			String fixedLenghtField1 = String.format("%-" + field1Size + "s", field1);
			String fixedLenghtField2 = String.format("%" + field2Size + "s", field2);
			String fixedLenghtField3 = String.format("%" + field3Size + "s", field3);
			//System.out.println(field1+field2+field3);
			return fixedLenghtField1 + fixedLenghtField2 + fixedLenghtField3;

		} else {
			System.out.println("No match encontrado");
			return null;
		}

	}
	/*
	public static String[] despedazarString(String linea) {
		List<String[]> table = new ArrayList<>();
		String regex = "([^|]+)\\|([^|]+)\\|([^|]+)";
		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(linea);

		if (matcher.matches()) {
			String field1 = matcher.group(1);
			String field2 = matcher.group(2);
			String field3 = matcher.group(3);

			String[] data = {field1,field2,field3};
			return data;
		}
		return null;
	}

	public static String formatTable(List<String[]> table){
		//Determinar el largo máximo de cada columna
		int[] maxWidths = new int[table.get(0).length];
		for(String[] row : table){
			for(int i=0; i<row.length; i++){
				if(row[i].length() > maxWidths[i]){
					maxWidths[i] = row[i].length();
				}
			}
		}
		//Crear String formateado
		StringBuilder formatBuilder = new StringBuilder();
		for(int maxWidth : maxWidths){
			formatBuilder.append("%-").append(maxWidth+2).append("s");
		}
		formatBuilder.append("%n");
		String format = formatBuilder.toString();

		//Por cada fila
		StringBuilder formattedTable = new StringBuilder();
		for(String[] row : table){
			formattedTable.append(String.format(format,(Object[]) row));
		}
		return formattedTable.toString();
	}

	 */
}