package com.marsol.sync.domain.handler.label;

import com.marsol.sync.domain.model.PLU;
import com.marsol.sync.model.Infonut;
import com.marsol.sync.model.Item;

public class GraphicsHandler extends LabelHandler{
    @Override
    public void handleLabel1(Item item, Infonut infonut, PLU plu) {
        try {
            int idSellos = Integer.parseInt(infonut.getImagenSellos());
            if(!infonut.isEs_etiqueta_propia()){
                if (idSellos >= 1 && idSellos <= 15) {
                    PLU.setLabel1(idSellos);
                    logger.debug("Asignando label1={} para plu={}", idSellos, item.getPlu_nbr());
                } else if(idSellos == 0){
                    PLU.setLabel1(16);
                }
            }else{
                PLU.setLabel1(16);
            }
        }catch(Exception e) {
            logger.error("Error durante la asignacion de label1 para plu={}:{}",item.getPlu_nbr(),e.getMessage());
        }
        if (nextHandler != null) {
            nextHandler.handleLabel1(item, infonut, plu);
        }
    }

    @Override
    public void handleLabel2(Item item, Infonut infonut, PLU plu) {

    }
}
