package com.marsol.sync.service.api;

import java.util.List;

import com.marsol.sync.app.ConfigLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import com.marsol.sync.model.Infonut;

@RunWith(MockitoJUnitRunner.class)
public class getInfonutTest {

	@Mock
	private RestTemplate restTemplate;
	
	@Mock
	private AuthService authService;
	
	@Mock
	private ApiService<Infonut> apiService;
	private ConfigLoader configLoader;
	
	@InjectMocks
	private InfonutService infonutService;
	
	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		restTemplate = new RestTemplate();
		configLoader = new ConfigLoader();
		authService = new AuthService(restTemplate,configLoader);
		apiService = new ApiService<Infonut>(restTemplate, authService,configLoader);
		infonutService = new InfonutService(apiService);
	}
	/*
	@Test
	public void testGetInfonut() {
		List<Infonut> infonuts = infonutService.getInfonut(674, 97);
		System.out.println(infonuts);
	}

	 */
}
