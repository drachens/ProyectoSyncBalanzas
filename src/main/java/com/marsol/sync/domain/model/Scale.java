package com.marsol.sync.domain.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marsol.sync.utils.DateTimeUtils;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
	Esta clase se encarga de crear los objetos Scale, los cuales respetan la estructura de los
	Objetos contenidos en el JSON retornado por los WS al consultar por la información de balanzas.
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Scale implements Comparable<Scale>{

	private Integer id;
	private int store;
	private String formato;
	private String nombre;
	private int departamento;
	private String IP_Balanza;
	private String marca;
	private String modelo;
	private boolean esDual;
	private String status;
	private String lastUpdate;
	private String userUpdate;
	private boolean isCargaLayout;
	private boolean isEsDual;
	private boolean isCargaMaestra;
	private boolean isEsAutoservicio;

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "{}";
		}
	}

	@Override
	public int compareTo(Scale o) {
		return getLastUpdateDateTime().compareTo(o.getLastUpdateDateTime());
	}

	public boolean isEsAutoservicio() {
		return isEsAutoservicio;
	}

	public boolean isCargaMaestra() {
		return isCargaMaestra;
	}

	public boolean isEsDual() {
		return isEsDual;
	}

	public boolean isCargaLayout() {
		return isCargaLayout;
	}

	public void setCargaLayout(boolean cargaLayout) {
		isCargaLayout = cargaLayout;
	}

	public void setEsDual(boolean esDual) {
		isEsDual = esDual;
	}

	public void setCargaMaestra(boolean cargaMaestra) {
		isCargaMaestra = cargaMaestra;
	}

	public void setEsAutoservicio(boolean esAutoservicio) {
		isEsAutoservicio = esAutoservicio;
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


}
