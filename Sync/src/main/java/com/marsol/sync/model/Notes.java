package com.marsol.sync.model;

public class Notes {

	private int LFCode;
	private String Value;

	//Constructor
	public Notes(int LFCode, String value) {
		setLFCode(LFCode);
		Value = value;
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
		Value = value;
	}

}
