package com.marsol.sync.service.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marsol.sync.app.ConfigLoader;
import com.marsol.sync.model.Scale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;


/*
	En esta clase se definirán los 4 tipos de consultas HTTP (GET, POST, DELETE, PUT) que se implementarán para
	la extracción, creación, eliminación y actualización de datos en los Servicios Walmart.
 */

@Service
public class ApiService<T>{
	private final ConfigLoader configLoader;
	private final RestTemplate restTemplate;
	private final AuthService authService;
	//private final String urlBase = "http://10.177.172.60:55001"; //Este debe ser una variable de entorno (.env)
	
	@Autowired
	public ApiService(RestTemplate restTemplate, AuthService authService, ConfigLoader configLoader) {
		this.restTemplate = restTemplate;
		this.authService = authService;
		this.configLoader = configLoader;
	}

	/*
		Esta función implementa el método HTTP GET.

		Los parámetros son:
			1- El endpoint de la API.
			2- El nombre de usuario para la generación del token de auntentificación.
			3- La clase respecto al tipo de objeto que se espera retornar en la consulta.

		MODIFICAR

		URL DEBE SER UN PARAMETRO CONFIGURABLE
		AGREGAR FUNCIONES Y CONSTRUCTURES CON DISTINTOS TIPOS DE AUTENTICACION
		SOLO DEBEN RETORNAR UN JSON, SIN CLASE LOS GET,POST,PUT,DELETE
	 */
	
	public <T> String getData(String endpoint, String user){
		String token = authService.getToken(user);
		if(token != null) {
			HttpHeaders headers = new HttpHeaders();
			 		//singletonList crea una lista inmutable de un elemento. 
					//El encabezado Accept le dice al servidor que el cliente (esta app) espera recibir una respuesta en Json. 
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			headers.setBearerAuth(token);
			//Crea la entidad de solicitud con los encabezados
			HttpEntity<String> request = new HttpEntity<>(headers);
			ResponseEntity<List<T>> response = restTemplate.exchange(
					endpoint,
	                HttpMethod.GET, 
	                request, 
	                new ParameterizedTypeReference<>() {}
	                );
			if(response.getStatusCode().is2xxSuccessful()) {
				ObjectMapper om = new ObjectMapper();
				try {
					return om.writeValueAsString(response.getBody());
				} catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
			} else {
				throw new RuntimeException("Error al obtener los datos: "+response.getStatusCodeValue());
			}
		} else {
			throw new RuntimeException("Error al obtener el token");
		}
	}

	/*
		Esta función implementa el método HTTP POST
	 */

	@SuppressWarnings("hiding")
	public void postData(String endpoint, String user, Object requestBody) {
		String urlBase = configLoader.getProperty("urlBase");
		String token = authService.getToken(user);
		if (token != null) {
			String apiURL = urlBase + "/apigateway/" + endpoint;
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.setBearerAuth(token);
			headers.set("Content-Type", "application/json");
			HttpEntity<Object> request = new HttpEntity<>(requestBody, headers);

			boolean success = false;
			int maxAttempts = 10;
			int attempt = 0;

			while (!success && attempt < maxAttempts) {
				try {
					attempt++;
					ResponseEntity<Void> response = restTemplate.exchange(
							apiURL,
							HttpMethod.POST,
							request,
							Void.class
					);
					if (response.getStatusCode().is2xxSuccessful()) {
						System.out.println("Operación exitosa: " + response.getStatusCode());
						success = true;
					} else {
						System.out.println("Error al obtener los datos: " + response.getStatusCodeValue());
					}
				} catch (ResourceAccessException e) {
					System.out.println("Error de conexión (intento " + attempt + " ): " + e.getMessage());
					if (attempt >= maxAttempts) {
						throw new RuntimeException("Error al conectar después de varios intentos.", e);
					}
				} catch (HttpClientErrorException e) {
					System.out.println("Error en la solicitud (intento " + attempt + " ): " + e.getMessage());
					if (attempt >= maxAttempts) {
						throw new RuntimeException("Error en la solicitud " + e.getMessage(), e);
					}
				} catch (Exception e) {
					System.out.println("Error inesperado (intento " + attempt + " ):" + e.getMessage());
					throw new RuntimeException("Error inesperado " + e.getMessage(), e);
				}
			}
		} else {
			throw new RuntimeException("Error al obtener el token");
		}
	}

	/*
		Esta función implementa el método HTTP DELETE
	 */

	@SuppressWarnings("hiding")
	public <T> T deleteData(String endpoint, String user, Class<T> responseType ){
		String urlBase = configLoader.getProperty("urlBase");
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

	/*
		Esta función implementa el método HTTP PUT
	 */

	@SuppressWarnings("hiding")
	public <T> T putData(String endpoint, String user, Class<T> responseType ) {
		String token = authService.getToken(user);
		if(token!=null) {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.setBearerAuth(token);
			//headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> request = new HttpEntity<>(headers);
			try{
				ResponseEntity<T> response = restTemplate.exchange(
						endpoint,
						HttpMethod.PUT,
						request,
						responseType
				);
				if(response.getStatusCode().is2xxSuccessful()) {
					return response.getBody();
				}else {
					throw new RuntimeException("Error al actualizar datos: "+response.getStatusCodeValue());
				}
			}catch (HttpClientErrorException | HttpServerErrorException e){
				System.out.println("Error al actualizar los datos: "+e.getMessage());
			}catch (ResourceAccessException e){
				System.out.println("Error al actualizar los datos: "+e.getMessage());
			}catch (Exception e){
				System.out.println("Error al actualizar los datos: "+e.getMessage());
			}

		}else {
			throw new RuntimeException("Error al obtener token.");
		}
        return null;
    }
}
