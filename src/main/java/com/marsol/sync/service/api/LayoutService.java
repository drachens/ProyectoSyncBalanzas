package com.marsol.sync.service.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marsol.sync.MainClass;
import com.marsol.sync.app.ConfigLoader;
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
public class LayoutService {
    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(LayoutService.class);
    @Value("${wm.endpoint.autoservicio}")
    private String urlBase;

    @Autowired
    public LayoutService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getLayout(int storeNbr, int deptNbr){
        if(deptNbr!=94 && deptNbr!=98){
            logger.warn("Departamento {} no tiene layouts.", deptNbr);
            return null;
        }
        String apiUrl = urlBase+deptNbr+"/"+storeNbr;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<List<Layout>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<List<Layout>>(){}
        );
        if(response.getStatusCode().is2xxSuccessful()){
            ObjectMapper om = new ObjectMapper();
            try{
                return om.writeValueAsString(response.getBody());
            }catch(JsonProcessingException e){
                logger.error("Error: {}", e.getMessage());
                return null;
            }
        }else{
            logger.error("No se puede obtener el layout");
            return null;
        }
    }
}
