package com.marsol.sync.service.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.marsol.sync.model.Log;
import com.marsol.sync.model.Scale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class LogService {
    private static final Logger logger = LoggerFactory.getLogger(LogService.class);
    private final RestTemplate restTemplate;
    private final Gson gson;
    @Value("${wm.endpoint.updateStatus}")
    private String updateStatusEndpoint;
    @Value("${wm.endpoint.logs.autoservicioBackend}")
    private String wmEndpointBackend;
    @Value("${wm.logs.credential.usr}")
    private String user;
    @Value("${wm.logs.credential.pssw}")
    private String pssw;
    @Autowired
    public LogService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.gson = new Gson();
    }

    //Funcion que crea un log en los servicios de wm
    public void createLog(Log log){
        try{
            String endpoint = wmEndpointBackend + "BalanzaLogs/Create";
            String logJsonString = gson.toJson(log);
            logger.warn("JSON creado: {}, para balanza ip: {}",logJsonString,log.getIpBalanza());
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON ));
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(logJsonString,headers);
            logger.info("SAS {}",request.getBody());
            ResponseEntity<Void> response = restTemplate.exchange(
                    endpoint,
                    HttpMethod.POST,
                    request,
                    Void.class
            );
            if(response.getStatusCode() == HttpStatus.CREATED){
                logger.info("Se ha creado el log de la balanza: {}", log.getResultado());
                System.out.println("Log de la balanza: " + log.getResultado());
            }else{
                logger.warn("No se pudo crear el log. Estado: {}", response.getStatusCode());
            }

        }catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("Error en la solicitud: {}", e.getMessage());
        } catch (HttpMessageNotReadableException e) {
            logger.error("Error en la deserialización de la respuesta: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado: {}", e.getMessage());
        }
    }

    public void updateStatus(Log log){
        String ip = log.getIpBalanza();
        String update = log.getFechaHora();
        String user = "Marsol";
        String status = "1";

       String endpoint = String.format("%s?IP_Balanza=%s&Status=%s&LastUpdate=%s&UserUpdate=%s",
               updateStatusEndpoint,
               ip,
               status,
               update,
               user);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON ));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>("",headers);

        try{
            ResponseEntity<Void> response = restTemplate.exchange(
              endpoint,
              HttpMethod.PUT,
              request,
              Void.class
            );
            if(response.getStatusCode() == HttpStatus.OK){
                logger.info("Se ha modificado el log.");
            }else {
                logger.error("Error al actualizar la ultima fecha y hora.");
            }
        } catch (RestClientException e) {
            logger.error("Error: {}",e.getMessage());
        }
    }
}
