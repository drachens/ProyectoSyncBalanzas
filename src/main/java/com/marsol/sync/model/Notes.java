package com.marsol.sync.model;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/*
	Esta clase se encarga de crear objetos Notes, estos objetos tienen la estructura
	de los campos de Notas que la balanza es capaz de cargar.
	Además tiene funciones que restringen los campos a los valores soportados por la balanza HPRT.

 */


public class Notes {

	private int LFCode;
	private String Value;

	//Constructor
	public Notes(int LFCode, String Value) {
		setLFCode(LFCode);
		setValue(Value);
	}
	@Override
	public String toString() {
		return LFCode + "\t" + Value;
	}

	//Set y Get LFCode
	public int getLFCode() {
		return LFCode;
	}
	public void setLFCode(int LFCode) {
		if(LFCode < 0 || LFCode > 999999) {
			throw new IllegalArgumentException("LFCode es obligatorio y debe estar entre 0 y 999999.");
		}
		this.LFCode = LFCode ;
	}
	//Set y Get Value
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		if(value.length() >= 1000) {
			Value = convertidorTexto(value.substring(0,950));
		}else{Value = convertidorTexto(value);}
		
		
	}
	
	//Funcion para eliminar acentos
	
	public String convertidorTexto(String text) {
		try{
			byte[] isoBytes = text.getBytes("ISO-8859-1");
			String textoUtf8 = new String(isoBytes, StandardCharsets.UTF_8);

			//Filtrar carecteres no validos UTF-8
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < textoUtf8.length(); i++) {
				char c = textoUtf8.charAt(i);
				if(Character.isDefined(c) && !Character.isISOControl(c)){
					sb.append(c);
				}
			}
			return sb.toString().replaceAll("\uFFFD","");
		}catch(UnsupportedEncodingException e) {
			return "error";
		}
	}

}
