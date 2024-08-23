package com.marsol.sync.model;

/*
    Esta clase Extiende la clase de Layout para manejar la consulta de los WS respecto a
    layouts de una balanza Dual, ya que los Objetos del JSON retornado por la consulta
    contiene un campo más: El departamento.
 */

public class LayoutDual extends Layout{
    private int depto;
/*
    public LayoutDual(int id, String familia, String desc_familia, int plu, String description, String imagen, int depto) {
        super(id, familia, desc_familia, plu, description, imagen);
        this.depto = depto;
    }

 */
    public int getDepto() {return this.depto;}
    public void setDepto(int depto) {this.depto = depto;}
}
