package com.marsol.sync.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

//RestTemplate es una clase proporionada por spring que facilita las llamadas HTTP.
//Se necesita que spring pueda inyectar esta clase en los servicios que lo necessiten.

@Configuration
public class AppConfig {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
