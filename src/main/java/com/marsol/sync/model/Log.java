package com.marsol.sync.model;

/*
    Esta clase establece un modelo para la creación de objetos tipo Log,
    los cuales respetan la estructura que debe tener un Log para ser creado
    en los Servicios Walmart.
 */

public class Log {
    private int id;
    private int store;
    private int departamento;
    private String accionBalanza;
    private int cantidadCambios;
    private String ipBalanza;
    private String fechaHora;
    private String resultado;

    public Log(int store, int departamento, String accionBalanza, int cantidadCambios, String ipBalanza, String fechaHora, String resultado) {
        this.store = store;
        this.departamento = departamento;
        this.accionBalanza = accionBalanza;
        this.cantidadCambios = cantidadCambios;
        this.ipBalanza = ipBalanza;
        this.fechaHora = fechaHora;
        this.resultado = resultado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStore() {
        return store;
    }

    public void setStore(int store) {
        this.store = store;
    }

    public int getDepartamento() {
        return departamento;
    }

    public void setDepartamento(int departamento) {
        this.departamento = departamento;
    }

    public String getAccionBalanza() {
        return accionBalanza;
    }

    public void setAccionBalanza(String accionBalanza) {
        this.accionBalanza = accionBalanza;
    }

    public int getCantidadCambios() {
        return cantidadCambios;
    }

    public void setCantidadCambios(int cantidadCambios) {
        this.cantidadCambios = cantidadCambios;
    }

    public String getIpBalanza() {
        return ipBalanza;
    }

    public void setIpBalanza(String ipBalanza) {
        this.ipBalanza = ipBalanza;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
