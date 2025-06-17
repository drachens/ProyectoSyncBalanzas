package com.marsol.sync.domain.handler;

import com.marsol.sync.domain.model.PLU;
import com.marsol.sync.model.Infonut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DatesHandler {
    protected static final Logger logger = LoggerFactory.getLogger(DatesHandler.class);
    protected DatesHandler nextHandler;

    public DatesHandler setNext(DatesHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    public abstract void handleDate(Infonut infonut, PLU plu);

}
