package com.marsol.sync.util;

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
	

}
