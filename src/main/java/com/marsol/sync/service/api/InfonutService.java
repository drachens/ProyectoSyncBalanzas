package com.marsol.sync.service.api;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.marsol.sync.model.Infonut;


/*
	Esta clase se encarga de generar las distintas consultas
	respecto a Información nutricional (Infonut), implementando
	la clase ApiService para realizarlas.

 */
@Service
public class InfonutService {

	private final ApiService apiService;
	@Value("${wm.endpoint.infonut.auth}")
	String authEndpoint;
	@Value("${wm.endpoint.infonut}")
	String wmEndpoint;
	@Value("${wm.infonut.credential.usr}")
	String user;
	@Value("${wm.infonut.credential.pssw}")
	String pssw;
	
	@Autowired
	public InfonutService(ApiService apiService) {
		this.apiService = apiService;
	}
	
	public String getInfonut(int storeNbr, int deptNbr){
		String endpoint = wmEndpoint+storeNbr+"/"+deptNbr;
		return apiService.getData(endpoint,authEndpoint,user,pssw);
	}
	
}
