package com.marsol.sync.domain.handler.label;


import com.marsol.sync.domain.model.PLU;
import com.marsol.sync.model.Infonut;
import com.marsol.sync.model.Item;

public class NotesInfoHandler extends LabelHandler {

    @Override
    public void handleLabel1(Item item, Infonut infonut, PLU plu) {
        //Tiene tabla nutricional
        if(!infonut.isEs_etiqueta_propia()){
            //No tiene ingredientes
            if(infonut.getIngredientes().length() < 20){
                PLU.setLabel1(18);
            }else {
                PLU.setLabel1(19);
            }
        }else {
            //No tiene ingredientes
            if(infonut.getIngredientes().length() < 20){
                PLU.setLabel1(16);
            }else {
                PLU.setLabel1(17);
            }
        }

        if (nextHandler != null) {
            nextHandler.handleLabel1(item, infonut, plu);
        }
    }

    @Override
    public void handleLabel2(Item item, Infonut infonut, PLU plu) {

    }
}
