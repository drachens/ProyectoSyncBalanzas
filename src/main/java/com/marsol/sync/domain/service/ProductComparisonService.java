package com.marsol.sync.domain.service;

import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.model.Item;
import com.marsol.sync.model.structures.HeadersFilesHPRT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductComparisonService {
    public static final Logger logger = LoggerFactory.getLogger(ProductComparisonService.class);

    public List<Integer> compareProducts(List<Item> serverProducts, List<Integer> scaleProducts){
        try{
            if(serverProducts == null || scaleProducts == null){
                throw new IllegalArgumentException("Las listas no pueden ser nulas.");
            }
            Set<Integer> serverPLU_codes = serverProducts.stream()
                    .map(item -> {
                        if (item == null) {
                            throw new IllegalArgumentException("Un elemento de serverProducts es null");
                        }
                        return (int) item.getPlu_nbr();
                    })
                    .collect(Collectors.toSet());
            return scaleProducts.stream()
                    .filter(LFCode -> !serverPLU_codes.contains(LFCode))
                    .toList();

        } catch (Exception e) {
            //System.err.println("Error al obtener productos a eliminar: "+e.getMessage());
            logger.error("Error al obtener productos a eliminar: {}",e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
