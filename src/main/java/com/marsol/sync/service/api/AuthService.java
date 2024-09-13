package com.marsol.sync.service.api;

import com.marsol.sync.service.transform.TransformWalmartPLUs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	MODIFICAR

	AGREGAR AL MENOS 2 FORMAS DE AUTENTICACION APARTE DE BEARER TOKEN, O
	QUE SEA UN PARAMETRO CONFIGURABLE
 */

@Service
public class AuthService {
	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

	private final RestTemplate restTemplate;
	@Value("${wm.url.base}")
	private String baseUrl;
	
	@Autowired
	public AuthService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	public String getToken(String endpoint, String username, String password) {
		Map<String, String> credentials = new HashMap<>();
		credentials.put("Username", username);
		credentials.put("Password", password);

		//Convertir Map a JSON
		String json = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			json = objectMapper.writeValueAsString(credentials);
			logger.debug("[AuthService] JSON de credenciales para endpoint {} creado exitosamente.", endpoint);
			
		} catch (Exception e) {
			logger.error("[AuthService] Error al crear JSON de credenciales.");
		}	
		//Configurar Headers
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-type","application/json");
		//Crear entidad de la solicitud con los encabezados
		HttpEntity<String> request = new HttpEntity<>(json,headers);
		//Enviar solicitud
		ResponseEntity<Map> response = restTemplate.exchange(endpoint,HttpMethod.POST,request,Map.class);
		//ResponseEntity<Map> response = restTemplate.postForEntity(authUrl, json, Map.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			Map body = response.getBody();
			if (body != null) {
				logger.info("[AuthService] Token para endpoint {} obtenido exitosamente.", endpoint);
				return (String) body.get("token");
			} else {
				logger.error("[AuthService] Token obtenido vacío.");
				return null;
			}
		}
		else{
			logger.error("[AuthService] Error al obtener el token: {}.",response.getStatusCode());
			return null;
		}
	}
	
	
}
