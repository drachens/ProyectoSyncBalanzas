package com.marsol.sync.domain.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marsol.sync.application.DeleteProductsController;
import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.infraestructure.api.*;
import com.marsol.sync.model.Advertising;
import com.marsol.sync.model.Infonut;
import com.marsol.sync.model.Item;
import com.marsol.sync.model.Layout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.*;

@Service
public class DataExtractionService {

    private final ProductService productService;
    private final InfonutService infonutService;
    private final LayoutService layoutService;
    private final ScaleService scaleService;


    private static final Logger logger = LoggerFactory.getLogger(DataExtractionService.class);

    @Autowired
    public DataExtractionService(ProductService productService,
                                 InfonutService infonutService,
                                 LayoutService layoutService,
                                 ScaleService scaleService
                                 ) {
        this.productService = productService;
        this.infonutService = infonutService;
        this.layoutService = layoutService;
        this.scaleService = scaleService;

    }

    public List<Infonut> getInfonut(int storeNbr, int deptNbr){
        String infonutJson = infonutService.getInfonut(storeNbr, deptNbr);
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Infonut>>() {}.getType();
        return gson.fromJson(infonutJson, listType);
    }

    public List<Item> getItemsDept(int storeNbr, int deptNbr){
        String itemsJson = productService.getItemsDept(storeNbr, deptNbr);
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Item>>() {}.getType();

        List<Item> allItems = gson.fromJson(itemsJson, listType);


        return allItems;
    }

    public List<Layout> getLayout(int storeNbr, int deptNbr){
        String layoutJson = layoutService.getLayout(storeNbr, deptNbr);
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Layout>>() {}.getType();
        return gson.fromJson(layoutJson, listType);
    }

    public List<Scale> getScales(String marca){
        String scalesJson = scaleService.getScalesByMarca(marca);
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Scale>>() {}.getType();
        return gson.fromJson(scalesJson, listType);
    }

    public List<Item> getItems(int storeNbr, int deptNbr, boolean isAutoservicio){
        if(isAutoservicio){
            return getAutoservicioItemsDept(storeNbr, deptNbr);
        }else{
            return getItemsDept(storeNbr, deptNbr);
        }
    }

    public List<Item> getAutoservicioItemsDept(int storeNbr, int deptNbr){
        /*
        Esta funcion sirve para obtener la lista de productos segun el layout de la balanza de
        autoservicio.
        */
        List<Item> items = new ArrayList<>();
        try{
            Gson gson_layout = new Gson();
            Gson gson_productos = new Gson();
            Map<Integer,Item> productMap = new HashMap<>();
            String layoutJSON = layoutService.getLayout(storeNbr, deptNbr);
            String productJSON = productService.getItemsDept(storeNbr, deptNbr);
            List<Layout> layouts = gson_layout.fromJson(layoutJSON, new TypeToken<List<Layout>>(){}.getType());
            List<Item> products = gson_productos.fromJson(productJSON, new TypeToken<List<Item>>(){}.getType());
            for(Item product : products){
                productMap.put((int) product.getPlu_nbr(), product);
            }
            //Si el PLU de layout está en el mapa -> Agregar Item a List<Item>

            for(Layout layout : layouts){
                Item item_layout = productMap.get(layout.getPlu());
                if(item_layout != null){
                    items.add(item_layout);
                }

            }
        } catch (Exception e) {
            items = Collections.emptyList();
        }
        return items;
    }



}
