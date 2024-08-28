package com.marsol.sync.service.api;



//import org.springframework.test.context.junit4.SpringRunner;

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
import com.marsol.sync.model.*;

@RunWith(MockitoJUnitRunner.class)
public class ScaleServiceTest {

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private ConfigLoader configLoader;
	
	@Mock 
	private AuthService authService;
	
	@Mock
	private ApiService<Scale> apiService;
	
	@InjectMocks
	private ScaleService scaleService;
	
	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		restTemplate = new RestTemplate();
		configLoader = new ConfigLoader();
		authService = new AuthService(restTemplate, configLoader);
		apiService = new ApiService<Scale>(restTemplate,authService,configLoader);
		scaleService = new ScaleService(apiService,restTemplate);
	}
	
	//@Test
	public void testCreateScale() {
		Scale scale = new Scale(0,72, "Test", "Test", 98,
				"1.1.1.1", "HPRT", "testModel", true,
				true, true, true, "1",
				"string", "string",false,false,
				false,"ip",false);
		System.out.println("Balanza para crear: "+scale.toString());
		scaleService.createScale(scale);
		
	}
	
	//@Test
	public void testDelteScale() {
		int id=1357;
		Scale scale = scaleService.deleteScale(id);
		System.out.println(scale);
	}
	
	//@Test
	public void testGetScaleById() {
		int id=10987654;
		String scales = scaleService.getScaleById(id);
		System.out.println(scales);
	}
	
	//@Test
	public void testGetAllScales() {
		String scales = scaleService.getAllScales();
		System.out.println(scales);
	}

	@Test
	public void testGetScaleByMarca(){
		String marca = "Hobart";
		String scales = scaleService.getScalesByMarca(marca);
		System.out.println(scales);
	}
	
}
