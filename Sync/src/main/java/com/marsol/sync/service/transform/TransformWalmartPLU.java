package com.marsol.sync.service.transform;

import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marsol.sync.util.JSONtoString;
import com.marsol.sync.model.PLU;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.marsol.sync.model.Infonut;
import com.marsol.sync.model.Notes;

public class TransformWalmartPLU implements TransformationStrategy {
	
	public class Item{
		private int dept_nbr;
		private int item_nbr;
		private int plu_nbr;
		//UPC_NBR ERROR Expected an int but was 214487000000 at line 1 column 154 path $[0].upc_nbr
		private long upc_nbr;
		private String item1_desc;
		private String brand_name;
		private String item_status_code;
		private int sell_price;
		private int stock;
		private int codigoTipoEtiqueta;
		private String sell_uom_code;
	}
	
	@Override
	public void transformDataPLUs() {
		String filepath = "C:\\Users\\Drach\\Desktop\\MARSOL\\APIWalmart\\jsons\\json_panaderia_items";
		String infonutFilepath =  "C:\\Users\\Drach\\Desktop\\MARSOL\\APIWalmart\\jsons\\json_panaderia_infonut";
		LocalDateTime ahora = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss");
		String dateTime = ahora.format(formatter);
		String pluFileName = "C:\\Users\\Drach\\Desktop\\MARSOL\\APIWalmart\\jsons\\PLU_TEST.txt";
        String[] header = {
                "LFCode",
                "ItemCode",
                "Department",
                "Name1",
                "Name2",
                "Name3",
                "Label1",
                "Label2",
                "BarcodeType1",
                "BarcodeType2",
                "UnitPrice",
                "WeightUnit",
                "TareWeight",
                "ProducedDateTime",
                "PackageDate",
                "PackageTime",
                "ValidDays",
                "FreshDays",
                "ValidDateCountF",
                "ProducedDateF",
                "PackageDateF",
                "ValidDateF",
                "FreshDateF",
                "DiscountFlag",
                "DiscountUnitPrice",
                "DiscountStartDateTime",
                "DiscountEndDateTime"
            };
        //PLU
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(pluFileName))) {
        	writer.write(String.join("\t", header));
        	writer.newLine();
        	try {
    			String jsonString = JSONtoString.leerArchivoComoJson(filepath);    			
    			String infonutJsonString = JSONtoString.leerArchivoComoJson(infonutFilepath);
    			
    			Gson gson = new Gson();		
    			//Definir el tipo de dato usando TypeToken
    			Type itemListType = new TypeToken<List<Item>>() {}.getType();
    			List<Item> items = gson.fromJson(jsonString, itemListType);
    			//Tipo de dato para infonut
    			Type infonutListType = new TypeToken<List<Infonut>>() {}.getType();
    			List<Infonut> infonuts = gson.fromJson(infonutJsonString, infonutListType);
    			
    			//Mapa de búsqueda rapida
    			Map<Integer, Infonut> infonutMap = new HashMap<>();
    			for(Infonut infonut : infonuts) {
    				//Llave plu_nbr y valor el infonut
    				infonutMap.put(infonut.getPlu_nbr(), infonut);
    			}
    			
    			for(Item item : items) {
    				int lFCode = item.plu_nbr;
    				//String lfcode = String.valueOf(item.plu_nbr);
    				//itemCode será el upc_number -> etiqueta 2XEEEEEPPPPP
    				String itemCode = String.valueOf(item.upc_nbr).substring(0,7);
    				int department = item.dept_nbr;
    				String name1 = item.item1_desc;
    				String item_status_code = item.item_status_code;
    				int weightUnit;
    				if(item_status_code.equals("A")) { //Si item_status_code es A entonces es peso variable.
    					weightUnit = 0; //Unidad de peso Kg
    				} else if(item_status_code.equals("I")) { //Si item_status_code es I entonces es peso unitario.
    					weightUnit = 8; //Unidad de piezas
    				} else {
    					weightUnit = 0; //Si no cumple, medimos en kilogramos nomas
    				}
    				int unitPrice = item.sell_price;
    				//LABEL1 Debe depender del campo imagenSellos de infonut.
    				int label1;
    				//Buscar infonut por plu
    				Infonut infonut = infonutMap.get(lFCode);
    				if(infonut != null) {
    					String imagenSellos = infonut.getImagenSellos();
    					int imagenSellos_id = Integer.parseInt(imagenSellos);
    					switch(imagenSellos_id) {
    					
    						case 2:
    							label1 = 2;
    							break;
    						case 0:
    							label1 = 16;
    						    break;
    						case 6:
    							label1 = 6;
    							break;
    						case 13:
    							label1 = 13;
    							break;
    						default:
    							label1 = 16;
    							break;
    					}
    					
    				}else {
    					label1 = 32;
    				}
    				int tareWeight = 0;
    				PLU plu = new PLU(lFCode, itemCode, department,name1,"","",label1,0,104,0,unitPrice,weightUnit,tareWeight,dateTime,0,0,0,0,0,0,0,0,0,0,0,dateTime,dateTime);
    				writer.write(plu.toString());
    				writer.newLine();
    			}
    			System.out.println("Archivo PLU_TEST.txt generado correctamente.");
    			
    		} catch(IOException e) {
    			e.printStackTrace();
    		}
        }catch(IOException e) {
        	e.printStackTrace();
        }
	}
	@Override
	public void transformDataNotes() {
		String filepath = "C:\\Users\\Drach\\Desktop\\MARSOL\\APIWalmart\\jsons\\json_panaderia_infonut";
		String note1FileName = "C:\\Users\\Drach\\Desktop\\MARSOL\\APIWalmart\\jsons\\NOTE1_TEST.txt";
		
		String[] header = {
			"LFCode",
			"Value"
		};
		//NOTE1
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(note1FileName))){
        	writer.write(String.join("\t", header));
        	writer.newLine();
        	try {
				String jsonString = JSONtoString.leerArchivoComoJson(filepath);
				Gson gson = new Gson();
				Type itemListType = new TypeToken<List<Infonut>>() {}.getType();
				List<Infonut> infonuts = gson.fromJson(jsonString, itemListType);
				for(Infonut infonut : infonuts) {
					String razon_social = infonut.getResolucion();
					String texto_alternativo = infonut.getTexto_alternativo();
					String procedencia = infonut.getProcedencia();
					String texto_final = razon_social + " " + texto_alternativo + "\r" + procedencia;
					int plu_nbr = infonut.getPlu_nbr();
					Notes nota = new Notes(plu_nbr,texto_final);
					writer.write(nota.toString());
					writer.newLine();
				}
	        	System.out.println("Archivo NOTE1_TEST.txt generado correctamente.");
        	}catch(IOException e) {
        		e.printStackTrace();
        	}
		}catch(IOException e) {
			e.printStackTrace();
		}
		//NOTE2
		String note2Filename = "C:\\Users\\Drach\\Desktop\\MARSOL\\APIWalmart\\jsons\\NOTE2_TEST.txt";
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(note2Filename))){
			writer.write(String.join("\t", header));
			writer.newLine();
			try{
				String jsonString_2 = JSONtoString.leerArchivoComoJson(filepath);
				Gson gson_2 = new Gson();
				Type itemListType_2 = new TypeToken<List<Infonut>>() {}.getType();
				List<Infonut> infonuts_2 = gson_2.fromJson(jsonString_2,itemListType_2);
				for(Infonut infonut_2 : infonuts_2) {
					String ingredientes = infonut_2.getIngredientes();
					int plu_nbr = infonut_2.getPlu_nbr();
					if(ingredientes.length() > 0) {
						Notes nota_2 = new Notes(plu_nbr,ingredientes);
						writer.write(nota_2.toString());;
						writer.newLine();	
					}	
				}
				System.out.println("Archivo NOTE2_TEST.text generado correctamente.");
			}catch(IOException e) {
				e.printStackTrace();
			}
		}catch(IOException e) {
			
			e.printStackTrace();
		}
		//NOTE 3
		String note3Filename = "C:\\Users\\Drach\\Desktop\\MARSOL\\APIWalmart\\jsons\\NOTE3_TEST.txt";
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(note3Filename))){
			writer.write(String.join("\t",header));
			writer.newLine();
			
			try {
				String jsonString_3 = JSONtoString.leerArchivoComoJson(filepath);
				Gson gson_3 = new Gson();
				Type itemListType_3 = new TypeToken<List<Infonut>>() {}.getType();
				List<Infonut> infonuts_3 = gson_3.fromJson(jsonString_3, itemListType_3);
				for(Infonut infonut_3 : infonuts_3) {
					String tablaNutricional = infonut_3.getTablaNutricional();
					String porcion = infonut_3.getPorcion();
					String porcionesxEnvase = infonut_3.getPorcionesxEnvase();
					String subtituloTablaNut = infonut_3.getSubtituloTablaNut();
					//String textoEnergiaContingencia = infonut_3.getTextoEnergiaContigencia();
					String muestraEnergia = infonut_3.getMuestraEnergia();
					String textoEnergia = infonut_3.getTextoEnergia();
					//String muestraProteinas
				}
				
			}catch(IOException e) {
				e.printStackTrace();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	

	@Override
	public void transformDataLayouts() {
		// TODO Auto-generated method stub
		
	}
}
