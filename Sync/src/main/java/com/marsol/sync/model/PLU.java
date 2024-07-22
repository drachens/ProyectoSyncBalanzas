package com.marsol.sync.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PLU {
	private int LFCode;
	private String ItemCode;
	private int Department;
	private String Name1;
	private String Name2;
	private String Name3;
	private int Label1;
	private int Label2;
	private int BarcodeType1;
	private int BarcodeType2;
	private int UnitPrice;
	private int WeightUnit;
	private int TareWeight;
	private String ProducedDateTime;
	private int PackageDate;
	private int PackageTime;
	private int ValidDays;
	private int FreshDays;
	private int ValidDateCountF;
	private int ProducedDateF;
	private int PackageDateF;
	private int ValidDateF;
	private int FreshDateF;
	private int DiscountFlag;
	private float DiscountUnitPrice;
	private String DiscountStartDateTime;
	private String DiscountEndDateTime;

	//Constructor
	public PLU(int lFCode, String itemCode, int department, String name1, String name2, String name3, int label1,
			int label2, int barcodeType1, int barcodeType2, int unitPrice, int weightUnit, int tareWeight,
			String producedDateTime, int packageDate, int packageTime, int validDays, int freshDays,
			int validDateCountF, int producedDateF, int packageDateF, int validDateF, int freshDateF, int discountFlag,
			float discountUnitPrice, String discountStartDateTime, String discountEndDateTime) {
		super();
		String format = "dd-MM-yy HH:mm:ss"; //Formato fecha.
		setLFCode(lFCode);
		setItemCode(itemCode);
		setDepartment(department);
		setName1(name1);
		setName2(name2);
		setName3(name3);
		setLabel1(label1);
		setLabel2(label2);
		setBarcodeType1(barcodeType1);
		setBarcodeType2(barcodeType2);
		setUnitPrice(unitPrice);
		setWeightUnit(weightUnit);
		setTareWeight(tareWeight);
		setProducedDateTime(producedDateTime,format);
		setPackageDate(packageDate);
		setPackageTime(packageTime);
		setValidDays(validDays);
		setFreshDays(freshDays);
		setValidDateCountF(validDateCountF);
		setProducedDateF(producedDateF);
		setPackageDateF(packageDateF);
		setValidDateF(validDateF);
		setFreshDateF(freshDateF);
		setDiscountFlag(discountFlag);
		setDiscountUnitPrice(discountUnitPrice);
		setDiscountStartDateTime(discountStartDateTime,format);
		setDiscountEndDateTime(discountEndDateTime,format);
	}

	//Mostrar el PLU como linea de texto separada por espacios
	@Override
	public String toString() {
		return LFCode + "\t" + ItemCode + "\t" + Department + "\t" + Name1 + "\t" + Name2 + "\t" + Name3 + "\t" + Label1 + "\t" + Label2
				+ "\t" + BarcodeType1 + "\t" + BarcodeType2 + "\t" + UnitPrice + "\t" + WeightUnit + "\t" + TareWeight + "\t" + ProducedDateTime
				+ "\t" + PackageDate + "\t" + PackageTime + "\t" + ValidDays + "\t" + FreshDays + "\t" + ValidDateCountF + "\t" + ProducedDateF
				+ "\t" + PackageDateF + "\t" + ValidDateF + "\t" + FreshDateF + "\t" + DiscountFlag + "\t" + DiscountUnitPrice + "\t" + DiscountStartDateTime
				+ "\t" + DiscountEndDateTime;
	}


	//Set y Get LFCode
	public int getLFCode() {
		return LFCode;
	}
	public void setLFCode(int LFCode)throws IllegalArgumentException {
		if(LFCode < 0 || LFCode > 999999) {
			throw new IllegalArgumentException("LFCode es obligatorio y debe estar entre 0 y 999999.");
		}
		this.LFCode = LFCode ;
	}
	//Set y Get ItemCode
	public String getItemCode() {
		return ItemCode;
	}
	public void setItemCode(String ItemCode) {
		if(ItemCode == null || ItemCode.isEmpty()) {
			throw new IllegalArgumentException("ItemCode es obligatorio.");
		} else if(ItemCode.length() > 16){
			throw new IllegalArgumentException("ItemCode debe tener un máximo de 16 caracteres.");
		}
		
		this.ItemCode = ItemCode;
	}
	//Set y Get Department
	public int getDepartment() {
		return Department;
	}
	public void setDepartment(int department) {
		if(department < 1 || department > 99) {
			throw new IllegalArgumentException("Department es obligatorio y debe estar entre 1 y 99.");
		}
		this.Department = department;
	}
	//Set y Get Name1
	public String getName1() {
		return Name1;
	}
	public void setName1(String name1) {
		if(name1 == null || name1.isEmpty()) {
			throw new IllegalArgumentException("Name1 es obligatorio.");
		}else if(name1.length() > 40) {
			throw new IllegalArgumentException("Name1 debe tener un máximo de 40 caracteres.");
		}
		this.Name1 = name1;
	}
	//Set y Get Name2
	public String getName2() {
		return Name2;
	}
	public void setName2(String name2) {
		if(name2.length() > 40) {
			throw new IllegalArgumentException("Name2 debe tener un máximo de 40 caracteres.");
		}
		this.Name2 = name2;
	}
	//Set y Get Name3
	public String getName3() {
		return Name3;
	}
	public void setName3(String name3) {
		if(name3.length() > 40) {
			throw new IllegalArgumentException("Name3 debe tener un máximo de 40 caracteres.");
		}
		this.Name3 = name3;
	}
	//Set y Get Label1
	public int getLabel1() {
		return Label1;
	}
	public void setLabel1(int label1) {
		if(label1 < 0 || label1 > 32) {
			throw new IllegalArgumentException("Label1 es obligatorio y debe estar entre 0 y 32");
		}
		this.Label1 = label1;
	}
	//Set y Get Label2
	public int getLabel2() {
		return Label2;
	}
	public void setLabel2(int label2) {
		if(label2 < 0 || label2 > 32) {
			throw new IllegalArgumentException("Label2 debe estar entre 0 y 32");
		}
		this.Label2 = label2;
	}
	//Set y Get BarcodeType1
	public int getBarcodeType1() {
		return BarcodeType1;
	}
	public void setBarcodeType1(int barcodeType1) {
		if(barcodeType1 < 0 || barcodeType1 > 150) {
			throw new IllegalArgumentException("BarcodeType1 es obligatorio y debe tener un valor entre 0 y 150.");
		}
		this.BarcodeType1 = barcodeType1;
	}
	//Set y Get BarcodeType2
	public int getBarcodeType2() {
		return BarcodeType2;
	}
	public void setBarcodeType2(int barcodeType2) {
		if(barcodeType2 < 0 || barcodeType2 > 150) {
			throw new IllegalArgumentException("BarcodeType2 debe tener un valor entre 0 y 150.");
		}
		this.BarcodeType2 = barcodeType2;
	}
	//Set y Get UnitPrice
	public int getUnitPrice() {
		return UnitPrice;
	}
	public void setUnitPrice(int unitPrice) {
		if(unitPrice <= 0) {
			throw new IllegalArgumentException("UnitPrice es obligatorio y debe ser mayor que 0");
		}
		this.UnitPrice = unitPrice;
	}
	//Set y Get WeightUnit
	public int getWeightUnit() {
		return WeightUnit;
	}
	public void setWeightUnit(int weightUnit) {
		if(weightUnit < 0 || weightUnit > 8) {
			throw new IllegalArgumentException("WeightUnit es obligatorio y debe tener un valor entre 0 y 8");
		}
		this.WeightUnit = weightUnit;
	}
	//Set y Get TareWeight
	public int getTareWeight() {
		return TareWeight;
	}
	public void setTareWeight(int tareWeight) {
		this.TareWeight = tareWeight;
	}
	//Set y Get ProducedDateTime
	public String getProducedDateTime() {
		return ProducedDateTime;
	}
	public void setProducedDateTime(String producedDateTime, String format) {
		//Validar formato
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setLenient(false); //Para que el formato sea escrito

		try {
			Date parsedData = sdf.parse(producedDateTime);
			this.ProducedDateTime = sdf.format(parsedData); //Almacenar en formato deseado
		}catch(ParseException e) {
			throw new IllegalArgumentException("Formato de fecha inválido.");
		}
	}
	//Set y Get PackageDate
	public int getPackageDate() {
		return PackageDate;
	}
	public void setPackageDate(int packageDate) {
		if(packageDate < 0 || packageDate > 99) {
			throw new IllegalArgumentException("PackageDate debe tener un valor entre 0 y 99.");
		}
		this.PackageDate = packageDate;
	}
	//Set y Get PackageTime
	public int getPackageTime() {
		return PackageTime;
	}
	public void setPackageTime(int packageTime) {
		if(packageTime < 0 || packageTime > 99) {
			throw new IllegalArgumentException("PackageTime debe tener un valor entre 0 y 99.");
		}
		this.PackageTime = packageTime;
	}
	//Set y Get ValidDays
	public int getValidDays() {
		return ValidDays;
	}
	public void setValidDays(int validDays) {
		if(validDays < 0 || validDays > 999) {
			throw new IllegalArgumentException("ValidDays es obligatorio y debe tener un valor entre 0 y 999.");
		}
		this.ValidDays = validDays;
	}
	//Set y Get FreshDays
	public int getFreshDays() {
		return FreshDays;
	}
	public void setFreshDays(int freshDays) {
		if(freshDays < 0 || freshDays > 999) {
			throw new IllegalArgumentException("FreshDays debe tener un valor entre 0 y 999.");
		}
		this.FreshDays = freshDays;
	}
	//Set y Get ValidDateCountF
	public int getValidDateCountF() {
		return ValidDateCountF;
	}
	public void setValidDateCountF(int validDateCountF) {
		if(validDateCountF < 0 || validDateCountF > 1) {
			throw new IllegalArgumentException("ValidDateCountF debe tener un valor entre 0 y 1.");
		}
		this.ValidDateCountF = validDateCountF;
	}
	//Set y Get ProducedDateF
	public int getProducedDateF() {
		return ProducedDateF;
	}
	public void setProducedDateF(int producedDateF) {
		if(producedDateF < 0 || producedDateF > 1) {
			throw new IllegalArgumentException("ProducedDateF debe tener un valor entre 0 y 1.");
		}
		this.ProducedDateF = producedDateF;
	}
	//Set y Get PackageDateF
	public int getPackageDateF() {
		return PackageDateF;
	}
	public void setPackageDateF(int packageDateF) {
		if(packageDateF < 0 || packageDateF > 1) {
			throw new IllegalArgumentException("PackageDateF debe tener un valor entre 0 y 1.");
		}
		this.PackageDateF = packageDateF;
	}
	//Set y Get ValidDateF
	public int getValidDateF() {
		return ValidDateF;
	}
	public void setValidDateF(int validDateF) {
		if(validDateF < 0 || validDateF > 3) {
			throw new IllegalArgumentException("ValidDateF debe tener un valor entre 0 y 3");
		}
		this.ValidDateF = validDateF;
	}
	//Set y Get FreshDateF
	public int getFreshDateF() {
		return FreshDateF;
	}
	public void setFreshDateF(int freshDateF) {
		if(freshDateF < 0 || freshDateF > 3) {
			throw new IllegalArgumentException("FreshDateF debe tener un valor enter 0 y 3.");
		}
		this.FreshDateF = freshDateF;
	}
	//Set y Get DiscountFlag
	public int getDiscountFlag() {
		return DiscountFlag;
	}
	public void setDiscountFlag(int discountFlag) {
		if(discountFlag < 0 || discountFlag > 2) {
			throw new IllegalArgumentException("DiscountFlag debe tener un valor entre 0 y 2");
		}
		this.DiscountFlag = discountFlag;
	}
	//Set y Get DiscountUnitPrice
	public float getDiscountUnitPrice() {
		return DiscountUnitPrice;
	}
	public void setDiscountUnitPrice(float discountUnitPrice) {
		if(discountUnitPrice < 0) {
			throw new IllegalArgumentException("DiscountUnitPrice no puede ser negativo.");
		}
		this.DiscountUnitPrice = discountUnitPrice;
	}
	//Set y Get DiscountStartDateTime
	public String getDiscountStartDateTime() {
		return DiscountStartDateTime;
	}
	public void setDiscountStartDateTime(String discountStartDateTime, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setLenient(false);
		try {
			Date parsedDate = sdf.parse(discountStartDateTime);
			this.DiscountStartDateTime = sdf.format(parsedDate);
		}catch(ParseException e) {
			throw new IllegalArgumentException("Formato de fecha inválido.");
		}
	}
	//Set y Get DiscountEndDateTime
	public String getDiscountEndDateTime() {
		return DiscountEndDateTime;
	}
	public void setDiscountEndDateTime(String discountEndDateTime, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setLenient(false);
		try {
			Date parsedDate = sdf.parse(discountEndDateTime);
			this.DiscountEndDateTime = sdf.format(parsedDate);
		}catch(ParseException e) {
			throw new IllegalArgumentException("Formato de fecha inválido.");
		}
	}

}

