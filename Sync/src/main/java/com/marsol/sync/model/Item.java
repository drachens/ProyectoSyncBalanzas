package com.marsol.sync.model;

public class Item {

	private int id;
	private int store_nbr;
	private int dept_nbr;
	private long item_nbr;
	private long plu_nbr;
	private long upc_nbr;
	private String item1_desc;
	private String brand_name;
	private String item_status_code;
	private int sell_price;
	private int stock;
	private long codigoTipoEtiqueta;
	private String sell_uom_code;

	public String getSell_uom_code() {
		return sell_uom_code;
	}
	public void setSell_uom_code(String sell_uom_code) {
		this.sell_uom_code = sell_uom_code;
	}
	public long getCodigoTipoEtiqueta() {
		return codigoTipoEtiqueta;
	}
	public void setCodigoTipoEtiqueta(long codigoTipoEtiqueta) {
		this.codigoTipoEtiqueta = codigoTipoEtiqueta;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public int getSell_price() {
		return sell_price;
	}
	public void setSell_price(int sell_price) {
		this.sell_price = sell_price;
	}
	public String getItem_status_code() {
		return item_status_code;
	}
	public void setItem_status_code(String item_status_code) {
		this.item_status_code = item_status_code;
	}
	public String getBrand_name() {
		return brand_name;
	}
	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}
	public String getItem1_desc() {
		return item1_desc;
	}
	public void setItem1_desc(String item1_desc) {
		this.item1_desc = item1_desc;
	}
	public long getUpc_nbr() {
		return upc_nbr;
	}
	public void setUpc_nbr(long upc_nbr) {
		this.upc_nbr = upc_nbr;
	}
	public long getPlu_nbr() {
		return plu_nbr;
	}
	public void setPlu_nbr(long plu_nbr) {
		this.plu_nbr = plu_nbr;
	}
	public long getItem_nbr() {
		return item_nbr;
	}
	public void setItem_nbr(long item_nbr) {
		this.item_nbr = item_nbr;
	}
	public int getDept_nbr() {
		return dept_nbr;
	}
	public void setDept_nbr(int dept_nbr) {
		this.dept_nbr = dept_nbr;
	}
	public int getStore_nbr() {
		return store_nbr;
	}
	public void setStore_nbr(int store_nbr) {
		this.store_nbr = store_nbr;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
