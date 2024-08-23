package com.marsol.sync.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
	Esta clase incluye una función que permite
 */

public class JSONtoString {

	public static String leerArchivoComoJson(String filepath) throws IOException{
		StringBuilder contenido = new StringBuilder();
		
		try(BufferedReader br = new BufferedReader(new FileReader(filepath))) {
			String linea;
			while((linea = br.readLine()) != null) {
				contenido.append(linea);
			}
		}
		return contenido.toString();
	}
}
