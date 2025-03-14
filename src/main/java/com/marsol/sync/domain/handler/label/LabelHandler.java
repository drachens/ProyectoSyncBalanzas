package com.marsol.sync.domain.handler.label;

import com.marsol.sync.domain.model.PLU;
import com.marsol.sync.model.Infonut;
import com.marsol.sync.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LabelHandler {
    protected static final Logger logger = LoggerFactory.getLogger(LabelHandler.class);
    protected LabelHandler nextHandler;

    public LabelHandler setNext(LabelHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    public abstract void handleLabel1(Item item, Infonut infonut, PLU plu);
    public abstract void handleLabel2(Item item, Infonut infonut, PLU plu);
}
