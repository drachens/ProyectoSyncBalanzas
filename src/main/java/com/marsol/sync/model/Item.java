package com.marsol.sync.model;
import com.marsol.sync.utils.OperationTypeConverter;
import lombok.Builder;
import lombok.Setter;

/*
	Esta clase define el modelo para la creación de objetos tipo Item, correspondiente a
	los Objetos contenidos en el JSON retornado por WS cuando se consultan los productos.
 */
@Setter
@Builder
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

    public long getCodigoTipoEtiqueta() {
		return codigoTipoEtiqueta;
	}

    public int getStock() {
		return stock;
	}

    public int getSell_price() {
		return sell_price;
	}

    public String getItem_status_code() {
		return item_status_code;
	}

    public String getBrand_name() {
		return brand_name;
	}

    public String getItem1_desc() {
		String name = OperationTypeConverter.removeAccents(item1_desc);
		return name;
	}

    public long getUpc_nbr() {
		return upc_nbr;
	}

    public long getPlu_nbr() {
		return plu_nbr;
	}

    public long getItem_nbr() {
		return item_nbr;
	}

    public int getDept_nbr() {
		return dept_nbr;
	}

    public int getStore_nbr() {
		return store_nbr;
	}

    public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(this);
	}
}
