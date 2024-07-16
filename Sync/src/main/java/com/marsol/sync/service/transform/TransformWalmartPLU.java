package com.marsol.sync.service.transform;

import java.io.BufferedWriter;
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
import java.util.List;

public class TransformWalmartPLU implements TransformationStrategy {
	
	public class Item{
		private int dept_nbr;
		private int item_nbr;
		private int plu_nbr;
		//UPC_NBR ERROR Expected an int but was 214487000000 at line 1 column 154 path $[0].upc_nbr
		private long upc_nbr;
		private String item1_desc;
		private String brand_name;
		private int sell_price;
		private int stock;
		private int codigoTipoEtiqueta;
		private String sell_uom_code;
	}
	
	@Override
	public void transformDataPLUs() {
		String filepath = "C:\\Users\\Drach\\Desktop\\MARSOL\\APIWalmart\\jsons\\json_items";
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
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(pluFileName))) {
        	writer.write(String.join("\t", header));
        	writer.newLine();
        	try {
    			String jsonString = JSONtoString.leerArchivoComoJson(filepath);
    			Gson gson = new Gson();
    			//Definir el tipo de dato usando TypeToken
    			//Item item = gson.fromJson(jsonString, Item.class);
    			Type itemListType = new TypeToken<List<Item>>() {}.getType();
    			List<Item> items = gson.fromJson(jsonString, itemListType);
    			for(Item item : items) {
    				int lFCode = item.plu_nbr;
    				//DUDAS este numero es el que se imprime en el codigo de barras?
    				String itemCode = String.valueOf(item.item_nbr);
    				int department = item.dept_nbr;
    				String name1 = item.item1_desc;
    				//long barcodeType1 = item.upc_nbr;
    				int unitPrice = item.sell_price;
    				int label1 = 1;
    				int weightUnit = 0;
    				int tareWeight = 0;
    				PLU plu = new PLU(lFCode, itemCode, department,name1,"","",label1,0,6,0,unitPrice,weightUnit,tareWeight,dateTime,0,0,0,0,0,0,0,0,0,0,0,dateTime,dateTime);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void transformDataLayouts() {
		// TODO Auto-generated method stub
		
	}
}
