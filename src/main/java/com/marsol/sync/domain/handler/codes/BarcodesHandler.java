package com.marsol.sync.domain.handler.codes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BarcodesHandler {
    protected static final Logger logger = LoggerFactory.getLogger(BarcodesHandler.class);
    protected BarcodesHandler nextHandler;

    public BarcodesHandler setNext(BarcodesHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    public abstract void handleBarcode1(BarcodesHandler barcodesHandler);
    public abstract void handleBarcode2();
}
