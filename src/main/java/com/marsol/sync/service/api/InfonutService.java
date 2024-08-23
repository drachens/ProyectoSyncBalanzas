package com.marsol.sync.service.api;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	private final ApiService<Infonut> apiService;
	
	@Autowired
	public InfonutService(ApiService<Infonut> apiService) {
		this.apiService = apiService;
	}
	
	public String getInfonut(int storeNbr, int deptNbr){
		String endpoint = "infonut/"+storeNbr+"/"+deptNbr;
		String user = "infonut";
		return apiService.getData(endpoint, user, Infonut.class);
	}
	
}
