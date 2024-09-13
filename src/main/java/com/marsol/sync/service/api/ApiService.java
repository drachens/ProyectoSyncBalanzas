package com.marsol.sync.service.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marsol.sync.MainClass;
import com.marsol.sync.model.Scale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	private final RestTemplate restTemplate;
	private final AuthService authService;
	private static final Logger logger = LoggerFactory.getLogger(ApiService.class);
	@Value("${maxAttempts}")
	private int maxAttempts;
	@Autowired
	public ApiService(RestTemplate restTemplate, AuthService authService) {
		this.restTemplate = restTemplate;
		this.authService = authService;
	}

	/*
		Esta función implementa el métdo HTTP GET.

		Los parámetros son:
			1- El endpoint de la API.
			2- El nombre de usuario para la generación del token de auntentificación.
			3- La clase respecto al tipo de objeto que se espera retornar en la consulta.

		MODIFICAR

		URL DEBE SER UN PARAMETRO CONFIGURABLE
		AGREGAR FUNCIONES Y CONSTRUCTURES CON DISTINTOS TIPOS DE AUTENTICACION
		SOLO DEBEN RETORNAR UN JSON, SIN CLASE LOS GET,POST,PUT,DELETE
	 */
	
	public <T> String getData(String endpoint, String authEndpoint, String user, String passw){
		String token = authService.getToken(authEndpoint,user,passw);
		if(token != null) {
            logger.info("[ApiService] Bearer Token obtenido para el endpoint: {}", token);
			HttpHeaders headers = new HttpHeaders();
			 		//singletonList crea una lista inmutable de un elemento. 
					//El encabezado Accept le dice al servidor que el cliente (esta app) espera recibir una respuesta en Json. 
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			headers.setBearerAuth(token);
			//Crea la entidad de solicitud con los encabezados
			HttpEntity<String> request = new HttpEntity<>(headers);
			boolean success = false;
			int attempt = 0;

			logger.debug("[ApiService] Consultando endpoint...");
			while(!success && attempt < maxAttempts) {
				try{
					attempt++;
					ResponseEntity<List<T>> response = restTemplate.exchange(
							endpoint,
							HttpMethod.GET,
							request,
							new ParameterizedTypeReference<>() {}
					);
					if(response.getStatusCode().is2xxSuccessful()) {
						ObjectMapper om = new ObjectMapper();
						try {
							logger.info("[ApiService] Operación exitosa para endpoint {} : {}",endpoint ,response.getStatusCode());
							return om.writeValueAsString(response.getBody());
						} catch (JsonProcessingException e) {
							logger.error("[ApiService] Error al procesar JSON {}", e.getMessage());
							return "Error al procesar JSON";
						}
					} else {
						logger.error("[ApiService] Error al obtener respuesta: {}", response.getStatusCode());
						return "Error al obtener respuesta";
					}
				}catch (ResourceAccessException e){
					logger.error("[ApiService] Error de conexión (intento {} ): {}", attempt, e.getMessage());
					if(attempt >= maxAttempts) {
						logger.error("[ApiService] Error al conectar después de varios intentos.");
						return "Error al conectar después de varios intentos.";
					}
				}catch (HttpClientErrorException e){
					logger.error("[ApiService] Error en la solicitud (intento {} ): {}", attempt, e.getMessage());
					if (attempt >= maxAttempts) {
						logger.error("[ApiService] Error en la solicitud ");
						return "Error en la solicitud ";
					}
				}catch (Exception e){
					logger.error("[ApiService] Error inesperado (intento {} ):{}", attempt, e.getMessage());
					return "Error inesperado ";
				}
			}
			return "Error desconocido";
		} else {
			logger.error("[ApiService] Error al obtener el token");
			return "Error al obtener el token";
		}
    }

	/*
		Esta función implementa el método HTTP POST
	 */

	@SuppressWarnings("hiding")
	public void postData(String endpoint,String authEndpoint ,String user, String passw, Object requestBody) {;
		String token = authService.getToken(authEndpoint, user, passw);
		if (token != null) {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.setBearerAuth(token);
			headers.set("Content-Type", "application/json");
			HttpEntity<Object> request = new HttpEntity<>(requestBody, headers);

			boolean success = false;
			int attempt = 0;

			logger.info("Consultando endpoint...");
			while (!success && attempt < maxAttempts) {
				try {
					attempt++;
					ResponseEntity<Void> response = restTemplate.exchange(
							endpoint,
							HttpMethod.POST,
							request,
							Void.class
					);
					if (response.getStatusCode().is2xxSuccessful()) {
                        logger.info("Operación exitosa: {}", response.getStatusCode());
						success = true;
					} else {
                        logger.error("Error al obtener los datos: {}", response.getStatusCode());
					}
				} catch (ResourceAccessException e) {
                    logger.error("Error de conexión (intento {} ): {}", attempt, e.getMessage());
					if (attempt >= maxAttempts) {
						logger.error("Error al conectar después de varios intentos.");
						throw new RuntimeException("Error al conectar después de varios intentos.", e);
					}
				} catch (HttpClientErrorException e) {
                    logger.error("Error en la solicitud (intento {} ): {}", attempt, e.getMessage());
					if (attempt >= maxAttempts) {
						logger.error("Error en la solicitud ");
						throw new RuntimeException("Error en la solicitud " + e.getMessage(), e);
					}
				} catch (Exception e) {
                    logger.error("Error inesperado (intento {} ):{}", attempt, e.getMessage());
					throw new RuntimeException("Error inesperado " + e.getMessage(), e);
				}
			}
		} else {
			logger.error("Error al obtener el token");
			throw new RuntimeException("Error al obtener el token");
		}
	}

	/*
		Esta función implementa el método HTTP DELETE
	 */

	@SuppressWarnings("hiding")
	public <T> T deleteData(String endpoint,String authEndpoint, String user, String passw, Class<T> responseType ){
		String token = authService.getToken(authEndpoint,user,passw);
		if(token!=null) {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.setBearerAuth(token);
			HttpEntity<String> request = new HttpEntity<>(headers);
			ResponseEntity<T> response = restTemplate.exchange(
					endpoint,
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
	public <T> T putData(String endpoint,String authEndpoint ,String user, String passw, Class<T> responseType ) {
		String token = authService.getToken(authEndpoint,user,passw);
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
