package com.marsol.sync.domain.handler.label;


import com.marsol.sync.domain.model.PLU;
import com.marsol.sync.model.Infonut;
import com.marsol.sync.model.Item;

public class DepartmentHandler extends LabelHandler {

    @Override
    public void handleLabel1(Item item, Infonut infonut, PLU plu) {
        int dept = item.getDept_nbr();
        switch (dept){
            case 94:
                PLU.setLabel1(16);
                break;
            //Carniceria
            case 93:
                if(infonut.getDiasPerecibilidad() == 998){
                    //998 : Sin fechas
                    if(!infonut.isEs_etiqueta_propia()){
                        //incluye tabla nutricional
                        if(infonut.getIngredientes().length() < 10){
                            //No tiene ingerdientes válidos
                            PLU.setLabel1(17);
                        }else{
                            //Tiene ingredientes
                            PLU.setLabel1(18);
                        }
                    }else{
                        //No incluye tabla nutricional
                        if(infonut.getIngredientes().length() < 10){
                            //No incluye ingredientes
                            PLU.setLabel1(19);
                        }else {
                            PLU.setLabel1(20);
                        }
                    }
                }else{
                    //Incluye solo fecha envasado
                    if(!infonut.isEs_etiqueta_propia()){
                        //Incluye tabla
                        if(infonut.getIngredientes().length() < 10){
                            PLU.setLabel1(21);
                        }else{
                            PLU.setLabel1(22);
                        }
                    }else{
                        //No incluye tabla
                        if(infonut.getIngredientes().length() < 10){
                            PLU.setLabel1(23);
                        }else{
                            PLU.setLabel1(24);
                        }
                    }
                }
                break;
            default:
                //Tiene tabla nutricional
                if(!infonut.isEs_etiqueta_propia()){
                    //Tiene ingredientes
                    if(infonut.getIngredientes().length() < 10){
                        PLU.setLabel1(25);
                    }else {
                        PLU.setLabel1(26);
                    }
                }else {
                    if(infonut.getIngredientes().length() < 10){
                        PLU.setLabel1(27);
                    }else {
                        PLU.setLabel1(28);
                    }
                }
                break;
        }
    }

    @Override
    public void handleLabel2(Item item, Infonut infonut, PLU plu) {

    }
}
