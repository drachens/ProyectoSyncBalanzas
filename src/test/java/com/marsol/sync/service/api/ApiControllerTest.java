package com.marsol.sync.service.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marsol.sync.app.ConfigLoader;
import com.marsol.sync.controller.ApiController;

//import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Type;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;
import com.marsol.sync.model.*;

@RunWith(MockitoJUnitRunner.class)
public class ApiControllerTest {
	
	@Mock
	private RestTemplate restTemplate;
	
	@Mock
	private AuthService authService;
	
	@Mock
	private ApiService<Item> apiService;
	private ConfigLoader configLoader;

	@InjectMocks
	private ProductService productService;
	
	//@InjectMocks
	//private ApiController apiController;
	
	//private AuthService authService;
	//private ApiController apiController;
	//private RestTemplate restTemplate;
	@Before
	public void setUp () {
		MockitoAnnotations.openMocks(this);
		restTemplate = new RestTemplate();
		configLoader = new ConfigLoader();
		authService = new AuthService(restTemplate);
		apiService = new ApiService<Item>(restTemplate, authService);
		productService = new ProductService(apiService);
	}
	
	@Test
	public void testGetItems() {
		int storeNbr = 76;
		int deptNbr = 98;
		
		//Mockito.when(productService.getItemsDept(storeNbr, deptNbr)).thenReturn(mockResponse);
		
		String responseEntity = productService.getItemsDept(storeNbr, deptNbr);
		Type type = new TypeToken<List<Item>>(){}.getType();
		Gson gson = new Gson();
		List<Item> items = gson.fromJson(responseEntity,type);
		for (Item item:items) {
			System.out.println("Id: "+item.getId());
			System.out.println("Desc: "+item.getItem1_desc());
			System.out.println("Precio: "+item.getSell_price());
			System.out.println("---------------------------------");
		}
		System.out.println(responseEntity);
	}
	

}
