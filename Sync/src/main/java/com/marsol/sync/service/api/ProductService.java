package com.marsol.sync.service.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.marsol.sync.model.Item;


import java.util.Collections;
import java.util.List;

@Service
public class ProductService {

	private final RestTemplate restTemplate;
	private final AuthService authService;
	private final String urlBase = "http://10.177.172.60:55001";
	
	@Autowired
	public ProductService(RestTemplate restTemplate, AuthService authService) {	
		this.restTemplate = restTemplate;
		this.authService = authService;
	}
	
	
	public List<Item> getItemsDept(int storeNbr, int deptNbr) {
		String token = authService.getToken("product");
		if(token != null) {
			String apiUrl = urlBase + "/apigateway/product/"+storeNbr+"/"+deptNbr;
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.set("Authorization", "Bearer "+token);
			//Creamos una solicitud de encabezados
			HttpEntity<String> request = new HttpEntity<>(headers);
			ResponseEntity<List<Item>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, request, new ParameterizedTypeReference<List<Item>>() {});
			//CODIGO 200
			if(response.getStatusCode().is2xxSuccessful()) {	
				List<Item> responseBody = response.getBody();
				return responseBody;
			} else {
				throw new RuntimeException("Error al obtener los items: "+response.getStatusCodeValue());
			}
		} else {
			throw new RuntimeException("Error al obtener el token");
		}
	}
	
	
	public List<Item> getItemsStore(int storeNbr){
		String token = authService.getToken("product");
		if(token != null) {
			String apiUrl = urlBase + "/apigateway/product/"+storeNbr;
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.set("Authorization", "Bearer "+token);
			//Creamos una solicitud de encabezados
			HttpEntity<String> request = new HttpEntity<>(headers);
			ResponseEntity<List<Item>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, request, new ParameterizedTypeReference<List<Item>>() {});
			//CODIGO 200
			if(response.getStatusCode().is2xxSuccessful()) {	
				System.out.println("Code 200!");
				List<Item> responseBody = response.getBody();
				return responseBody;
			} else {
				throw new RuntimeException("Error al obtener los items: "+response.getStatusCodeValue());
			}
		}else {
			System.out.println("Error al obtener el token");
			throw new RuntimeException("Error al obtener el token");
		}
	}
}
