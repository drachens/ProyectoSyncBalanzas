package com.marsol.sync.domain.handler.dates;

import com.marsol.sync.domain.model.PLU;
import com.marsol.sync.model.Infonut;

public class PerishabilityHandler extends DatesHandler {

    @Override
    public void handleDate(Infonut infonut, PLU plu) {
        int perishability = infonut.getDiasPerecibilidad();
        if(perishability == 998){
            plu.setProducedDateF(3);
            plu.setValidDateF(3);
        }
        else if(perishability == 999){
            plu.setValidDateF(3);
        }
    }
}
