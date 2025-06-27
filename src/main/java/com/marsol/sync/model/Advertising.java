package com.marsol.sync.model;

public class Advertising {
    private int idPromo;
    private int formato;
    private int store_nbr;
    private int depto_nbr;
    private String fechaInicio;
    private String fechaTermino;
    private String imagenPromo;

    public Advertising(){

    }

    public int getIdPromo() {
        return idPromo;
    }

    public void setIdPromo(int idPromo) {
        this.idPromo = idPromo;
    }

    public int getFormato() {
        return formato;
    }

    public void setFormato(int formato) {
        this.formato = formato;
    }

    public int getStore_nbr() {
        return store_nbr;
    }

    public void setStore_nbr(int store_nbr) {
        this.store_nbr = store_nbr;
    }

    public int getDepto_nbr() {
        return depto_nbr;
    }

    public void setDepto_nbr(int depto_nbr) {
        this.depto_nbr = depto_nbr;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaTermino() {
        return fechaTermino;
    }

    public void setFechaTermino(String fechaTermino) {
        this.fechaTermino = fechaTermino;
    }

    public String getImagenPromo() {
        return imagenPromo;
    }

    public void setImagenPromo(String imagenPromo) {
        this.imagenPromo = imagenPromo;
    }
}
