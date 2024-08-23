package com.marsol.sync.service.transform;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jcraft.jsch.IO;
import com.marsol.sync.app.ConfigLoader;
import com.marsol.sync.model.Infonut;
import com.marsol.sync.model.Item;
import com.marsol.sync.model.Notes;
import com.marsol.sync.model.Scale;
import com.marsol.sync.service.api.InfonutService;
import com.marsol.sync.service.io.NoteWriter;
import com.marsol.sync.utils.NotesForWalmart;
import com.marsol.sync.utils.TablaNutricionalCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


