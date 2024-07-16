package com.marsol.sync.service.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Collections;
import java.util.List;

@Service
public class ApiService<T> {

	private final RestTemplate restTemplate;
	private final AuthService authService;
	private final String urlBase = "http://10.177.172.60:55001";
	
	@Autowired
	public ApiService(RestTemplate restTemplate, AuthService authService) {
		this.restTemplate = restTemplate;
		this.authService = authService;
	}
	
	public List<T> getData(String endpoint, String user, Class<T> responseType){
		String token = authService.getToken(user);
		if(token != null) {
			String apiUrl = urlBase+"/apigateway/"+endpoint;
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.set("Authorization","Bearer "+token);
			HttpEntity<String> request = new HttpEntity<>(headers);
			ResponseEntity<List<T>> response = restTemplate.exchange(
	                apiUrl, HttpMethod.GET, request, new ParameterizedTypeReference<List<T>>() {});
			if(response.getStatusCode().is2xxSuccessful()) {
				return response.getBody();
			} else {
				throw new RuntimeException("Error al obtener los datos: "+response.getStatusCodeValue());
			}
		} else {
			throw new RuntimeException("Error al obtener el token");
		}
	}
	
	@SuppressWarnings("hiding")
	public <T> List<T> postData(String endpoint, String user, Object requestBody ,Class<T> responseType){
		String token = authService.getToken(user);
		if(token != null) {
			String apiURL = urlBase+"/apigateway/"+endpoint;
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.set("Authorization","Bearer "+token);
			headers.set("Content-Type","application/json");
			HttpEntity<Object> request = new HttpEntity<>(requestBody,headers);
			ResponseEntity<List<T>> response = restTemplate.exchange(
					apiURL, HttpMethod.POST, request, new ParameterizedTypeReference<List<T>>() {});
			if(response.getStatusCode().is2xxSuccessful()) {
				return response.getBody();
			} else {
				throw new RuntimeException("Error al obtener los datos:"+response.getStatusCodeValue());
			}
			
		} else {
			throw new RuntimeException("Error al conseguir el Token.");
		}
	}
}
