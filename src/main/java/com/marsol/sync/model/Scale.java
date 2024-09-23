package com.marsol.sync.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.marsol.sync.controller.ScalesNetworkController;
import com.marsol.sync.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
	Esta clase se encarga de crear los objetos Scale, los cuales respetan la estructura de los
	Objetos contenidos en el JSON retornado por los WS al consultar por la información de balanzas.
 */
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
	@JsonProperty("isCargaLayout")
	private boolean isCargaLayout;
	@JsonProperty("isEsDual")
	private boolean isEsDual;
	@JsonProperty("isCargaMaestra")
	private boolean isCargaMaestra;
	@JsonProperty("ip_Balanza")
	private String ip_Balanza;
	@JsonProperty("isEsAutoservicio")
	private boolean isEsAutoservicio;
	private static final Logger logger = LoggerFactory.getLogger(Scale.class);


	//Constructor
	public Scale(){

	}
	public Scale(Integer id, int store, String formato,
				 String nombre, int departamento, String iP_Balanza,
				 String marca, String modelo, boolean esAutoservicio,
				 boolean cargaMaestra, boolean cargaLayout, boolean esDual,
				 String status, String lastUpdate, String userUpdate,
				 boolean isCargaLayout, boolean isEsDual, boolean isCargaMaestra,
				 String ip_Balanza, boolean isEsAutoservicio) {
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
		this.isCargaLayout = isCargaLayout;
		this.isEsDual = isEsDual;
		this.isCargaMaestra = isCargaMaestra;
		this.ip_Balanza = ip_Balanza;
		this.isEsAutoservicio = isEsAutoservicio;
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


	public LocalDateTime getLastUpdateDateTime(){
		/*
		Si el valor de LastUpdate de la balanza es null, se debe gestionar, puesto que
		LocalDateTime no permite valores null.

		LastUpdate es null cuando la balanza es creada en los WS.
		 */
		DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss");
		LocalDateTime dateLastUpdate;
		if(lastUpdate == null || lastUpdate.isEmpty()){
			//Si  LastUpdate es null, se asigna LastUpdate como la hora actual -2 horas.
			String formattedDate = LocalDateTime.now().minusHours(2).format(FORMATTER);
			dateLastUpdate = DateTimeUtils.stringToDateTime(formattedDate);
			return dateLastUpdate;
		}else{
			dateLastUpdate = DateTimeUtils.stringToDateTime(lastUpdate);
			return dateLastUpdate;
		}
	}

	public String getUserUpdate() {
		return userUpdate;
	}

	public boolean isCargaLayout() {
		return isCargaLayout;
	}

	public boolean isEsDual() {
		return isEsDual;
	}

	public boolean isCargaMaestra() {
		return isCargaMaestra;
	}

	public String getIpBalanza() {
		return ip_Balanza;
	}

	public boolean isEsAutoservicio() {
		return isEsAutoservicio;
	}
}
