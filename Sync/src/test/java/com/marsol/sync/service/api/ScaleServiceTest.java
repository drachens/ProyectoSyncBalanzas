package com.marsol.sync.service.api;



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
public class ScaleServiceTest {

	@Mock
	private RestTemplate restTemplate;
	
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
		authService = new AuthService(restTemplate);
		apiService = new ApiService<Scale>(restTemplate,authService);
		scaleService = new ScaleService(apiService);
	}
	@Test
	public void testGetAllScales() {
		List<Scale> scales = scaleService.getAllScales();
		System.out.println(scales);
	}
}
