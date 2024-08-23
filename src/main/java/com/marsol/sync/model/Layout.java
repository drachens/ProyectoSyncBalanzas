package com.marsol.sync.model;

/*
    Esta clase define un modelo para la creación de objetos tipo Layout, los cuales
    corresponden a la estructura de los Objetos del JSON obtenido de los SW cuando se
    consulta Layouts de Panaderia o Vegetales.
 */

import com.fasterxml.jackson.annotation.JsonCreator;

public class Layout {
    private int id;
    private String familia;
    private String desc_Familia;
    private int plu;
    private String description;
    private String imagen;

    public Layout() {

    }
    /*
    public Layout(int id, String familia, String desc_Familia, int plu, String description, String imagen) {
        this.id = id;
        this.familia = familia;
        this.desc_Familia = desc_Familia;
        this.plu = plu;
        this.description = description;
        this.imagen = imagen;
    }

     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getDesc_familia() {
        return desc_Familia;
    }

    public void setDesc_familia(String desc_familia) {
        this.desc_Familia = desc_familia;
    }

    public int getPlu() {
        return plu;
    }

    public void setPlu(int plu) {
        this.plu = plu;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
