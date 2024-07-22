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
			 		//singletonList crea una lista inmutable de un elemento. 
					//El encabezado Accept le dice al servidor que el cliente (esta app) espera recibir una respuesta en Json. 
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.setBearerAuth(token);
			//headers.set("Authorization","Bearer "+token);
			//Crea la entidad de solicitud con los encabezados
			HttpEntity<String> request = new HttpEntity<>(headers);
			ResponseEntity<List<T>> response = restTemplate.exchange(
	                apiUrl, 
	                HttpMethod.GET, 
	                request, 
	                new ParameterizedTypeReference<List<T>>() {}
	                );
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
	public void postData(String endpoint, String user, Object requestBody){
		String token = authService.getToken(user);
		if(token != null) {
			String apiURL = urlBase+"/apigateway/"+endpoint;
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.setBearerAuth(token);
			//headers.set("Authorization","Bearer "+token);
			headers.set("Content-Type","application/json");
			HttpEntity<Object> request = new HttpEntity<>(requestBody,headers);
			ResponseEntity<Void> response = restTemplate.exchange(
					apiURL,
					HttpMethod.POST, 
					request, 
					Void.class
					);
			if(response.getStatusCode().is2xxSuccessful()) {
				System.out.println("Operación exitosa: "+response.getStatusCode());
			} else {
				throw new RuntimeException("Error al obtener los datos:"+response.getStatusCodeValue());
			}
			
		} else {
			throw new RuntimeException("Error al conseguir el Token.");
		}
	}
	
	@SuppressWarnings("hiding")
	public <T> T deleteData(String endpoint, String user, Class<T> responseType ){
		String token = authService.getToken(user);
		if(token!=null) {
			String apiUrl = urlBase+"/apigateway/"+endpoint;
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.setBearerAuth(token);
			HttpEntity<String> request = new HttpEntity<>(headers);
			ResponseEntity<T> response = restTemplate.exchange(
					apiUrl,
					HttpMethod.DELETE,
					request,
					responseType
					);
			if(response.getStatusCode().is2xxSuccessful()) {
				return response.getBody();
			}else {
				throw new RuntimeException("Error al eliminar los datos: "+response.getStatusCodeValue());
			}
		}else {
			throw new RuntimeException("Error al obtener el token.");
		}
	}
	
	@SuppressWarnings("hiding")
	public <T> T putData(String endpoint, String user, Object requestBody, Class<T> responseType) {
		String token = authService.getToken(user);
		if(token!=null) {
			String apiUrl = urlBase+"/apigateway/"+endpoint;
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.setBearerAuth(token);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Object> request = new HttpEntity<>(requestBody, headers);
			ResponseEntity<T> response = restTemplate.exchange(
					apiUrl,
					HttpMethod.PUT,
					request,
					responseType
					);
			if(response.getStatusCode().is2xxSuccessful()) {
				return response.getBody();
			}else {
				throw new RuntimeException("Error al actualizar datos: "+response.getStatusCodeValue());
			}
		}else {
			throw new RuntimeException("Error al obtener token.");
		}
	}
}
