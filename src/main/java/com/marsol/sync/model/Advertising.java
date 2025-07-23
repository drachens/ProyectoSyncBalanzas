package com.marsol.sync.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Advertising {
    private int idPromo;
    private int formato;
    private int store_nbr;
    private int depto_nbr;
    private String fechaInicio;
    private String fechaTermino;
    private String imagenPromo;
}


