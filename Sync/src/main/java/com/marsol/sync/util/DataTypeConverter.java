package com.marsol.sync.util;

import java.util.HashMap;
import java.util.Map;


public class DataTypeConverter {

    private static final Map<String, String> dataTypeMap = initializeDataTypeMap();

    private static Map<String, String> initializeDataTypeMap() {
        Map<String, String> map = new HashMap<>();
        map.put("SKU", "0x0012");
        map.put("Dept.", "0x0001");
        map.put("Unit", "0x0002");
        map.put("Hotkey", "0x0003");
        map.put("Custom Barcode", "0x0004");
        map.put("Information 1", "0x0005");
        map.put("Information 2", "0x0006");
        map.put("Information 3", "0x0007");
        map.put("Information 4", "0x0008");
        map.put("System parameter", "0x000C");
        map.put("Time", "0x000D");
        map.put("Label element", "0x2000");
        map.put("Label Backprint", "0x2001");
        map.put("Label file", "0x2002");
        return map;
    	}
    
    public static String convertDataType(String dataTypeName) {
    	String hexValue = dataTypeMap.get(dataTypeName);
    	if(hexValue == null) {
    		throw new IllegalArgumentException("Tipo de dato desconocido: "+dataTypeName);
    	}
    	return hexValue;
    }
    
}