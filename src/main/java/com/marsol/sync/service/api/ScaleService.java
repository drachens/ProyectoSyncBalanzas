package com.marsol.sync.service.api;

import java.util.Collections;
import java.util.List;

import com.marsol.sync.app.ConfigLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	private final RestTemplate restTemplate;
	private final Gson gson;
	@Value("${wm.endpoint.scales}")
	private String wmEndpoint;
	@Value("${wm.endpoint.autoservicio}")
	private String wmEndpointAuto;
	@Value("${wm.endpoint.scales.user}")
	private String user;
	@Autowired
	public ScaleService(ApiService<Scale> apiService, RestTemplate restTemplate){
		this.apiService = apiService;
		this.restTemplate = restTemplate;
		this.gson = new Gson();
	}
	
	public String getAllScales(){
		String endpoint = wmEndpoint + "/All";
		return apiService.getData(endpoint, user);
	}
	
	public String getScaleById(int Id){
		String endpoint = wmEndpoint + Id;
		return apiService.getData(endpoint, user);
	}

	public Scale solicitaCargaLayout(Scale scale){
		String ip = scale.getIp_Balanza();
		String endpoint = wmEndpoint+"/SolicitaCargaLayout?IP_Balanza="+ip;
		return apiService.putData(endpoint,user,Scale.class);
	}

	public Scale solicitaCargaMaestra(Scale scale){
		String ip = scale.getIp_Balanza();
		String endpoint = wmEndpoint+"/SolicitaCargaMaestra?IP_Balanza="+ip;
		return apiService.putData(endpoint,user,Scale.class);
	}

	public Scale updateCargaLayout(Scale scale){
		String ip = scale.getIp_Balanza();
		String endpoint = wmEndpoint+"/UpdateCargaLayout?IP_Balanza="+ip;
		return apiService.putData(endpoint,user,Scale.class);
	}

	public Scale updateCargaMaestra(Scale scale){
		String ip = scale.getIp_Balanza();
		String endpoint = wmEndpoint+"/UpdateCargaMaestra?IP_Balanza="+ip;
		return apiService.putData(endpoint,user,Scale.class);
	}
	
	public void createScale(Scale scale) {
		String scaleJSON = null;
		String endpoint = wmEndpoint+"/Create";
		try{
			scaleJSON = gson.toJson(scale);
			System.out.println(scaleJSON);
			apiService.postData(endpoint, user, scaleJSON);
			System.out.println("Balanza creada exitosamente");
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error convirtiendo Scale en JSON");
		}
	}

	public Scale deleteScale(int id) {
		String endpoint = wmEndpoint+id;
		return apiService.deleteData(endpoint, user, Scale.class);
	}

	public String getScalesByMarca(String marca){
		String Url = wmEndpointAuto+"/IPs?marca="+marca;
		try{
            org.springframework.http.HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			HttpEntity<String> request = new HttpEntity<>(headers);
			ResponseEntity<List<Scale>> response = restTemplate.exchange(Url,
					HttpMethod.GET,
					request,
					new ParameterizedTypeReference<List<Scale>>() {});
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
