package com.marsol.sync.utils;

import java.util.HashMap;
import java.util.Map;

public class OperationTypeConverter {

	private static final Map<String, Integer> typeOperationMap = initializeTypeOperationMap();
	
	private static Map<String, Integer> initializeTypeOperationMap(){
	    Map<String, Integer> map = new HashMap<>();
	    map.put("Download", 0);
	    map.put("Upload", 1);
	    map.put("Delete", 2);
	    return map;
	}
	
	public static int convertOperationType(String operationTypeName) {
		Integer intValue = typeOperationMap.get(operationTypeName);
		if(intValue == null) {
			throw new IllegalArgumentException("Nombre de operacion inválida: "+ operationTypeName);
		}
		return intValue;
	}

	public static String removeAccents(String input) {
		// Expresión regular que busca vocales acentuadas
		String accents = "áéíóúÁÉÍÓÚüÜñÑ";
		String replacements = "aeiouAEIOUuUnN";

		// Mapeo de caracteres acentuados a sus reemplazos
		StringBuilder output = new StringBuilder(input.length());

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			int index = accents.indexOf(c);
			if (index != -1) {
				// Reemplazar el carácter acentuado por su equivalente sin acento
				output.append(replacements.charAt(index));
			} else {
				// Mantener el carácter original si no está en la lista de acentos
				output.append(c);
			}
		}
		return output.toString();
	}
}
