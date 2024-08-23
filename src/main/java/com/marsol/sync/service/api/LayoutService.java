package com.marsol.sync.service.api;

import com.marsol.sync.app.ConfigLoader;
import com.marsol.sync.model.Layout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class LayoutService {
    private ConfigLoader configLoader;
    private final RestTemplate restTemplate;

    @Autowired
    public LayoutService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        configLoader = new ConfigLoader();
    }

    public List<Layout> getLayoutsPanaderia(int storeNbr){
        String urlBase = configLoader.getProperty("urlAutoservicio");
        String apiUrl = urlBase+"/98/"+storeNbr;
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
            return response.getBody();
        } else{
            throw new RuntimeException("Error al obtener los datos: "+response.getStatusCodeValue());
        }
    }

    public List<Layout> getLayoutsVegetales(int storeNbr){
        String urlBase = configLoader.getProperty("urlAutoservicio");
        String apiUrl = urlBase+"/94/"+storeNbr;
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
            return response.getBody();
        } else{
            throw new RuntimeException("Error al obtener los datos: "+response.getStatusCodeValue());
        }
    }

}
