package com.marsol.sync.service.transform;

import com.marsol.sync.app.ConfigLoader;
import com.marsol.sync.model.Infonut;
import com.marsol.sync.model.Scale;
import com.marsol.sync.service.api.InfonutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransformWalmartNotes implements TransformationStrategy <Infonut> {
    private InfonutService infonutService;
    private ConfigLoader configLoader;
    //private final ProductService productService;

    public TransformWalmartNotes() {
        configLoader = new ConfigLoader();
    }

    @Autowired
    public void setInfonutService(InfonutService infonutService) {
        this.infonutService = infonutService;
        //this.productService = productService;
    }

    @Override
    public void transformDataPLUs(Scale scale) {

    }

    @Override
    public void transformDataLayouts() {

    }

    @Override
    public void transformDataNotes(Scale scale) {

    }
}


