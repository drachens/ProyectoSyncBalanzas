package com.marsol.sync.service.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marsol.sync.model.Item;

import java.util.List;

/*
	Esta clase se encarga de generar las distintas consultas
	a los WS respecto a la información de productos, para este caso
	las solicitudes son 2:
		1- Obtener los productos de una tienda/departamento
		2- Obtener los productos de una tienda y todos los departamentos.
 */

@Service
public class ProductService {
	
	private final ApiService<Item> apiService;
	
	@Autowired
	public ProductService(ApiService<Item> apiService) {	
		this.apiService = apiService;
	}
	
	public String getItemsDept(int storeNbr, int deptNbr){
		String endpoint = "product/"+storeNbr+"/"+deptNbr;
		String user = "product";
		return apiService.getData(endpoint, user, Item.class);
	}
	
	public String getItemsStore(int storeNbr){
		String endpoint = "product/"+storeNbr;
		String user = "product";
		return apiService.getData(endpoint, user, Item.class);
	}
	
	/*
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
	*/
}
