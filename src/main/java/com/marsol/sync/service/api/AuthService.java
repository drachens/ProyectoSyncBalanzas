package com.marsol.sync.service.api;

import com.marsol.sync.app.ConfigLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/*
	Esta clase se encarga de la generación del token de autentificación
	para las distintas consultas realizadas a los Servicios Walmart.

	La función getToken retorna un token de autentificación.
 */

@Service
public class AuthService {

	private final ConfigLoader configLoader;
	private final RestTemplate restTemplate;

	
	@Autowired
	public AuthService(RestTemplate restTemplate, ConfigLoader configLoader) {
		this.restTemplate = restTemplate;
		this.configLoader = configLoader;
	}
	
	public String getToken(String endpoint) {
		String urlBase = configLoader.getProperty("urlBase");
		System.out.println("getToken Init");
		String authUrl = urlBase + "/auth/" + endpoint;
		Map<String, String> credentials = new HashMap<>();
		credentials.put("Username", endpoint+"_user");
		credentials.put("Password", "123");

		//Convertir Map a JSON
		String json = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			json = objectMapper.writeValueAsString(credentials);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		//Configurar Headers
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-type","application/json");
		//Crear entidad de la solicitud con los encabezados
		HttpEntity<String> request = new HttpEntity<>(json,headers);
		//Enviar solicitud
		ResponseEntity<Map> response = restTemplate.exchange(authUrl,HttpMethod.POST,request,Map.class);
		//ResponseEntity<Map> response = restTemplate.postForEntity(authUrl, json, Map.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			Map body = response.getBody();
			if (body != null) {
				return (String) body.get("token");
			} else {
				throw new RuntimeException("Error 0");
			}
		}
		else{
			throw new RuntimeException("ERROR 1");
		}
	
	}
	
	
}
