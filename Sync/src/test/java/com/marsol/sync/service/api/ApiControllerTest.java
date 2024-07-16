package com.marsol.sync.service.api;

import com.marsol.sync.controller.ApiController;

//import org.springframework.test.context.junit4.SpringRunner;

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
	private ProductService productService;
	
	@InjectMocks
	private ApiController apiController;
	
	//private AuthService authService;
	//private ApiController apiController;
	//private RestTemplate restTemplate;
	@Before
	public void setUp () {
		MockitoAnnotations.openMocks(this);
		restTemplate = new RestTemplate();
		authService = new AuthService(restTemplate);
		productService = new ProductService(restTemplate,authService);
		apiController = new ApiController(productService);
	}
	
	@Test
	public void testGetItems() {
		int storeNbr = 76;
		int deptNbr = 98;
		
		//Mockito.when(productService.getItemsDept(storeNbr, deptNbr)).thenReturn(mockResponse);
		
		List<Item> responseEntity = apiController.getItemsDepts(storeNbr, deptNbr);
		for (Item item:responseEntity) {
			System.out.println("Id: "+item.getId());
			System.out.println("Desc: "+item.getItem1_desc());
			System.out.println("Precio: "+item.getSell_price());
			System.out.println("---------------------------------");
		}
		System.out.println(responseEntity);
	}
	

}
