package com.marsol.sync.service.api;

import com.google.gson.Gson;
import com.marsol.sync.model.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LogService {
    private final ApiService<Log> apiService;
    private final Gson gson;
    @Value("${wm.endpoint.logs}")
    private String wmEndpoint;
    @Value("${wm.endpoint.logs.user}")
    private String user;
    @Autowired
    public LogService(ApiService<Log> apiService){
        this.apiService = apiService;
        this.gson = new Gson();
    }

    //Funcion que crea un log en los servicios de wm
    public void createLog(Log log){
        String logJson = null;
        String endpoint = wmEndpoint + "/Create";
        try{
            logJson = gson.toJson(log);
            apiService.postData(endpoint,user,logJson);
            System.out.println("Se ha creado el log de la balanza: " + log.getIpBalanza());
        }catch(Exception e){
            System.out.println("Error creando log: " + e.getMessage());
        }
    }
}
