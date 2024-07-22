package com.marsol.sync.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@JsonPropertyOrder({"id", "store", "formato", "nombre", "departamento","iP_Balanza","marca","modelo","esAutoservicio","cargaMaestra","cargaLayout","esDual","status","lastUpdate","userUpdate"})
public class Scale {

	@JsonProperty("id")
	private Integer id;
	@JsonProperty("store")
	private int store;
	@JsonProperty("formato")
	private String formato;
	@JsonProperty("nombre")
	private String nombre;
	@JsonProperty("departamento")
	private int departamento;
	@JsonProperty("iP_Balanza")
	private String iP_Balanza;
	@JsonProperty("marca")
	private String marca;
	@JsonProperty("modelo")
	private String modelo;
	@JsonProperty("esAutoservicio")
	private boolean esAutoservicio;
	@JsonProperty("cargaMaestra")
	private boolean cargaMaestra;
	@JsonProperty("cargaLayout")
	private boolean cargaLayout;
	@JsonProperty("esDual")
	private boolean esDual;
	@JsonProperty("status")
	private String status;
	@JsonProperty("lastUpdate")
	private String lastUpdate;
	@JsonProperty("userUpdate")
	private String userUpdate;


	//Constructor
	public Scale(Integer id, int store, String formato, String nombre, int departamento, String iP_Balanza, String marca, String modelo, boolean esAutoservicio, boolean cargaMaestra, boolean cargaLayout, boolean esDual, String status, String lastUpdate, String userUpdate) {
		this.id = null;
		this.store = store;
		this.formato = formato;
		this.nombre = nombre;
		this.departamento = departamento;
		this.iP_Balanza = iP_Balanza;
		this.marca = marca;
		this.modelo = modelo;
		this.esAutoservicio = esAutoservicio;
		this.cargaMaestra = cargaMaestra;
		this.cargaLayout = cargaLayout;
		this.esDual = esDual;
		this.status = "1";
		this.lastUpdate = lastUpdate;
		this.userUpdate = userUpdate;
	}
	
	@Override
	public String toString() {
		return id+" | "+store+" | "+formato+" | "+nombre+" | "+departamento+" | "+iP_Balanza+" | "+marca+" | "+modelo+" | "+esAutoservicio+" | "+cargaMaestra+" | "+cargaLayout+" | "+esDual+" | "+status+" | "+lastUpdate+" | "+userUpdate;
	}

	public Integer getId() {
		return id;
	}

	public int getStore() {
		return store;
	}

	public String getFormato() {
		return formato;
	}

	public String getNombre() {
		return nombre;
	}

	public String getIp_Balanza() {
		return iP_Balanza;
	}

	public int getDepartamento() {
		return departamento;
	}

	public String getModelo() {
		return modelo;
	}

	public String getMarca() {
		return marca;
	}

	public boolean getIsEsAutoservicio() {
		return esAutoservicio;
	}

	public boolean getIsCargaMaestra() {
		return cargaMaestra;
	}

	public boolean getIsCargaLayout() {
		return cargaLayout;
	}

	public boolean getIsEsDual() {
		return esDual;
	}

	public String getStatus() {
		return status;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public String getUserUpdate() {
		return userUpdate;
	}
}
