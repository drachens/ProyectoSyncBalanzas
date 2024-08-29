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
import com.marsol.sync.model.Item;

@RunWith(MockitoJUnitRunner.class)
public class ProducServiceTests {
	
	@Mock
	private RestTemplate restTemplate;
	
	@Mock
	private AuthService authService;
	
	@Mock
	private ApiService<Item> apiService;
	private ConfigLoader configLoader;
	
	@InjectMocks
	private ProductService productService;
	
	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		restTemplate = new RestTemplate();
		configLoader = new ConfigLoader();
		authService = new AuthService(restTemplate);

		apiService = new ApiService<Item>(restTemplate, authService,configLoader);
		productService = new ProductService(apiService);
	}
	/*
	@Test
	public void testGetItemsDept() {
		List<Item> items = productService.getItemsDept(674, 97);
		System.out.println(items);
	}
	
	@Test
	public void testGetItemsStore() {
		List<Item> items = productService.getItemsStore(674);
		System.out.println(items);
	}


	 */
}
