package com.marsol.sync.service.api;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marsol.sync.app.ConfigLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.http.*;

import com.google.gson.Gson;
import com.marsol.sync.model.Scale;
import org.springframework.web.client.RestTemplate;

/*
	Esta clase se encarga de generar las consultas respecto a las Balanzas
	en los WS. En este caso las funciones son:
		1- POST -> Crear una balanza en los servicios de Walmart.
		2- GET -> Obtener todas las balanzas segun su Marca.
		3- PUT -> Actualizar los valores de cargaMaestra y cargaLayout según corresponda.
		4- DELETE -> Eliminar una balanza según su id
 */

@Service
public class ScaleService {

	private final ApiService<Scale> apiService;
	private final Gson gson;
	private final RestTemplate restTemplate;
	private final ConfigLoader configLoader;
	
	@Autowired
	public ScaleService(ApiService<Scale> apiService, RestTemplate restTemplate, ConfigLoader configLoader){
		this.apiService = apiService;
		this.gson = new Gson();
		this.restTemplate = restTemplate;
		this.configLoader = configLoader;
	}
	
	public String getAllScales(){
		return apiService.getData("scales/All", "scales", Scale.class);
	}
	
	public String getScaleById(int Id){
		return apiService.getData("scales/"+Id, "scales", Scale.class);
	}
	
	public void createScale(Scale scale) {
		String scaleJSON = null;
		try{
			scaleJSON = gson.toJson(scale);
			System.out.println(scaleJSON);
			apiService.postData("scales/Create", "scales", scaleJSON);
			System.out.println("Balanza creada exitosamente");
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error convirtiendo Scale en JSON");
		}
	}

	public Scale deleteScale(int id) {
		return apiService.deleteData("scales/"+id, "scales", Scale.class);
	}

	public String getScalesByMarca(String marca){
		String Url = configLoader.getProperty("urlAutoservicioScaleMarca")+marca;
		try{
            org.springframework.http.HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			HttpEntity<String> request = new HttpEntity<>(headers);
			ResponseEntity<List<Scale>> response = restTemplate.exchange(Url, HttpMethod.GET, request, new ParameterizedTypeReference<List<Scale>>() {});
			if(response.getStatusCode() == HttpStatus.OK){
				Gson gson = new Gson();
				try {
					return gson.toJson(response.getBody());
				}catch (Exception e){e.printStackTrace();}
			}
        }catch (Exception e){e.printStackTrace();}

        return "";
    }
}
