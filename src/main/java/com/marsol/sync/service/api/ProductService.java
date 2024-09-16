package com.marsol.sync.service.api;

import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.marsol.sync.model.Item;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private final InfonutService infonutService;
	@Value("${wm.endpoint.product.auth}")
	public String authEndpoint;
	@Value("${wm.endpoint.product}")
	public String wmEndpoint;
	@Value("${wm.product.credential.usr}")
	public String user;
	@Value("${wm.product.credential.pssw}")
	public String pssw;

	@Autowired
	public ProductService(ApiService<Item> apiService , InfonutService infonutService) {

		this.apiService = apiService;
		this.infonutService = infonutService;
	}
	
	public String getItemsDept(int storeNbr, int deptNbr){
		String endpoint = wmEndpoint+storeNbr+"/"+deptNbr;
		String productJson =apiService.getData(endpoint,authEndpoint,user,pssw);
		String infonutJson = infonutService.getInfonut(storeNbr,deptNbr);
		return filtrarProductos(productJson,infonutJson);
	}
	
	public String getItemsStore(int storeNbr){
		String endpoint = wmEndpoint+storeNbr;
		return apiService.getData(endpoint,authEndpoint,user,pssw);
	}
	public String filtrarProductos(String productJSON,String infonutJSON){
		JsonArray filteredArray = new JsonArray();
		Map<Integer, Void> mapReal = new HashMap<>();
		JsonArray jsonArrayProducts = JsonParser.parseString(productJSON).getAsJsonArray();
		JsonArray jsonArrayInfonuts = JsonParser.parseString(infonutJSON).getAsJsonArray();
		for(JsonElement jsonElement : jsonArrayInfonuts){
			JsonObject infonut = jsonElement.getAsJsonObject();
			int plu_nbr = infonut.get("plu_nbr").getAsInt();
			mapReal.put(plu_nbr, null);
		}
		for(JsonElement jsonElement : jsonArrayProducts){
			JsonObject product = jsonElement.getAsJsonObject();
			int plu_nbr = product.get("plu_nbr").getAsInt();
			if(mapReal.containsKey(plu_nbr)){
				filteredArray.add(product);
			}
		}
		Gson gson = new Gson();
		String filteredGson = gson.toJson(filteredArray);
		return filteredGson;
	}
}
