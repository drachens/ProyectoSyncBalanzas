package com.marsol.sync.service.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	@Value("${wm.endpoint.product}")
	String wmEndpoint;
	@Value("${wm.endpoint.product.user}")
	String user;

	@Autowired
	public ProductService(ApiService<Item> apiService) {	
		this.apiService = apiService;
	}
	
	public String getItemsDept(int storeNbr, int deptNbr){
		String endpoint = wmEndpoint+storeNbr+"/"+deptNbr;
		return apiService.getData(endpoint, user);
	}
	
	public String getItemsStore(int storeNbr){
		String endpoint = wmEndpoint+storeNbr;
		return apiService.getData(endpoint, user);
	}

}
