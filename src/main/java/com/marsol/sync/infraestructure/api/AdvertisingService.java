package com.marsol.sync.infraestructure.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marsol.sync.model.Advertising;
import com.marsol.sync.model.Layout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class AdvertisingService {
    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(LayoutService.class);
    @Value("${wm.endpoint.advertising}")
    private String urlBase;

    @Autowired
    public AdvertisingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getAdvertising(int storeNbr, int deptNbr) {
        String apiUrl = urlBase+deptNbr+"/"+storeNbr;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<List<Advertising>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<List<Advertising>>(){}
        );
        if(response.getStatusCode().is2xxSuccessful()){
            ObjectMapper om = new ObjectMapper();
            try{
                logger.info("[AdvertisingService] Advertising (publicidad) obtenido para tienda {} y departamento {}.", storeNbr, deptNbr);
                return om.writeValueAsString(response.getBody());
            }catch(JsonProcessingException e){
                logger.error("[AdvertisingService] Error: {}", e.getMessage());
                return null;
            }
        }else{
            logger.error("[AdvertisingService] No se puede obtener Advertising (publicidad). Error: {}", response.getStatusCode());
            return null;
        }
    }



}
