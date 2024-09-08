package com.marsol.sync.service.api;

import com.google.gson.Gson;
import com.marsol.sync.model.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LogService {
    private static final Logger logger = LoggerFactory.getLogger(LogService.class);
    private final ApiService<Log> apiService;
    private final Gson gson;
    @Value("${wm.endpoint.logs}")
    private String wmEndpoint;
    @Value("${wm.endpoint.logs.auth}")
    private String authEndpoint;
    @Value("${wm.logs.credential.usr}")
    private String user;
    @Value("${wm.logs.credential.pssw}")
    private String pssw;
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
            apiService.postData(endpoint,authEndpoint,user,pssw,logJson);
            logger.info("Se ha creado el log de la balanza: {}", log.getIpBalanza());
        }catch(Exception e){
            logger.error("Error creando log: {}", e.getMessage());
        }
    }
}
