package com.marsol.sync.domain.service;

import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductComparisonService {
    public static final Logger logger = LoggerFactory.getLogger(ProductComparisonService.class);

    public List<Integer> compareProducts(List<Item> serverProducts, List<Integer> scaleProducts, Scale scale){
        try{
            if(serverProducts == null || scaleProducts == null){
                throw new IllegalArgumentException("Las listas serverProducts | scaleProducts no pueden ser nulas.");
            }
            logger.info("Existen {} productos para balanza -> {}", serverProducts.size(),scale.getiP_Balanza());
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
            logger.error("Error al obtener productos a eliminar: {}",e.getMessage());
            throw new RuntimeException("Error al obtener productos a eliminar: " + e.getMessage());
        }
    }
}
