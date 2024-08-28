package com.marsol.sync.service.transform;

import com.marsol.sync.app.ConfigLoader;
import com.marsol.sync.model.Log;
import com.marsol.sync.service.api.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.marsol.sync.model.Infonut;
import com.marsol.sync.model.Item;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TestTransformWalmartPLU {

	@Mock
	private RestTemplate restTemplate;
	
	@Mock
	private AuthService authService;
	
	@Mock
	private ApiService<Infonut> apiServiceInfonut;
	private ApiService<Item> apiServiceItem;
	
	@Mock
	private InfonutService infonutService;
	private ProductService productService;
	private ConfigLoader configLoader;

	@InjectMocks
	private TransformWalmartNotes transformWalmartarNotes;
	private TransformWalmartPLUs transformWalmartPLUs;
	
	@Before
	public void setUP() {
		MockitoAnnotations.openMocks(this);
		restTemplate = new RestTemplate();
		configLoader = new ConfigLoader();
		authService = new AuthService(restTemplate, configLoader);
		apiServiceInfonut = new ApiService<>(restTemplate, authService,configLoader);
		apiServiceItem = new ApiService<>(restTemplate, authService,configLoader);
		infonutService = new InfonutService(apiServiceInfonut);
		productService = new ProductService(apiServiceItem);
		LogService logService = new LogService(new ApiService<Log>(restTemplate,authService,configLoader));
		transformWalmartarNotes = new TransformWalmartNotes();
		transformWalmartPLUs = new TransformWalmartPLUs(logService);
	}
	/*
	@Test
	public void testTransformDataPLUs() {
		List<Item> items = productService.getItemsDept(674,98);
		transformWalmartPLUs.transformDataPLUs(items);
	}


	//@Test
	public void transformDataNotes() {
		List<Infonut> infonuts = infonutService.getInfonut(674,98);
		transformWalmartarNotes.transformDataNotes(infonuts);
	}
	/*
	public void testTransformDataLayouts(){
		transformWalmartPLU.transformDataLayouts();
	}
	*/

}
