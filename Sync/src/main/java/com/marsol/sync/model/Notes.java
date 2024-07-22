package com.marsol.sync.model;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class Notes {

	private int LFCode;
	private String Value;

	//Constructor
	public Notes(int LFCode, String value) {
		setLFCode(LFCode);
		setValue(value);
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
		
		if(value.length() <= 1000) {
			Value = convertidorTexto(value);
		}else {
			Value = convertidorTexto(value).substring(0,1000);
		}
		
		
	}
	
	//Funcion para eliminar acentos
	
	public String convertidorTexto(String text) {

		try{
			byte[] isoBytes = text.getBytes("ISO-8859-1");
			String textoUtf8 = new String(isoBytes, StandardCharsets.UTF_8);
			return textoUtf8;
		}catch(UnsupportedEncodingException e) {
			return "error";
		}

		
	}

}
