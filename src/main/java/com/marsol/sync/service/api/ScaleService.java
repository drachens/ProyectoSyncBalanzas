package com.marsol.sync.service.api;

import java.util.Collections;
import java.util.List;

import com.marsol.sync.app.ConfigLoader;
import com.marsol.sync.controller.ScalesNetworkController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger logger = LoggerFactory.getLogger(ScaleService.class);
	private final Gson gson;
	@Value("${wm.endpoint.scales}")
	private String wmEndpoint;
	@Value("${wm.endpoint.scales.autoservicio}")
	private String wmEndpointAutoservicio;
	@Value("${wm.endpoint.scales.auth}")
	String authEndpoint;
	@Value("${wm.scales.credential.usr}")
	private String user;
	@Value("${wm.scales.credential.pssw}")
	private String pssw;
	@Autowired
	public ScaleService(ApiService<Scale> apiService, RestTemplate restTemplate){
		this.apiService = apiService;
		this.restTemplate = restTemplate;
		this.gson = new Gson();

	}
	
	public String getAllScales(){
		String endpoint = wmEndpoint + "/All";
		return apiService.getData(endpoint,authEndpoint,user,pssw);
	}
	
	public String getScaleById(int Id){
		String endpoint = wmEndpoint + Id;
		return apiService.getData(endpoint,authEndpoint,user,pssw);
	}

	public Scale solicitaCargaLayout(Scale scale){
		String ip = scale.getIp_Balanza();
		String endpoint = wmEndpoint+"/SolicitaCargaLayout?IP_Balanza="+ip;
		return apiService.putData(endpoint,authEndpoint,user,pssw,Scale.class);
	}

	public Scale solicitaCargaMaestra(Scale scale){
		String ip = scale.getIp_Balanza();
		String endpoint = wmEndpoint+"/SolicitaCargaMaestra?IP_Balanza="+ip;
		return apiService.putData(endpoint,authEndpoint,user,pssw,Scale.class);
	}

	public Scale updateCargaLayout(Scale scale){
		String ip = scale.getIp_Balanza();
		String endpoint = wmEndpoint+"/UpdateCargaLayout?IP_Balanza="+ip;
		return apiService.putData(endpoint,authEndpoint,user,pssw,Scale.class);
	}

	public Scale updateCargaMaestra(Scale scale){
		String ip = scale.getIp_Balanza();
		String endpoint = wmEndpoint+"/UpdateCargaMaestra?IP_Balanza="+ip;
		return apiService.putData(endpoint,authEndpoint,user,pssw,Scale.class);
	}
	
	public void createScale(Scale scale) {
		String scaleJSON = null;
		String endpoint = wmEndpoint+"/Create";
		try{
			scaleJSON = gson.toJson(scale);
			System.out.println(scaleJSON);
			apiService.postData(endpoint,authEndpoint,user, pssw, scaleJSON);
			logger.info("Balanza creada exitosamente");
		}catch(Exception e) {
			logger.error("Error convirtiendo Scale en JSON");
		}
	}

	public Scale deleteScale(int id) {
		String endpoint = wmEndpoint+id;
		return apiService.deleteData(endpoint, authEndpoint,user,pssw, Scale.class);
	}

	public String getScalesByMarca(String marca){
		String Url = wmEndpointAutoservicio+"/IPs?marca="+marca;
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
					logger.info("[ScaleService] Solicitud de balanzas por Marca '" + marca + "' realizada exitosamente.");
					return gson.toJson(response.getBody());
				}catch (Exception e){
					logger.error("[ScaleService] Error al convertir el cuerpo de la respuesta a JSON para la marca '" + marca);
				}
			}else{
				logger.error("[ScaleService] Error al solicitar balanzas por Marca '" + marca + "'. Código de estado: " + response.getStatusCode());
			}
        }catch (Exception e){
			logger.error("[ScaleService] Error al realizar la solicitud HTTP para obtener balanzas por Marca '" + marca+"'");
		}

        return "";
    }
}
