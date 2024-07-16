package com.marsol.sync.service.transform;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestTransformWalmartPLU {

	@Mock
	private TransformationStrategy transformationStrategy;
	@InjectMocks
	private TransformWalmartPLU transformWalmartPLU;
	
	@Before
	public void setUP() {
		MockitoAnnotations.openMocks(this);
		transformWalmartPLU = new TransformWalmartPLU();
	}
	
	@Test
	public void testTransformDataPLU() {
		transformWalmartPLU.transformDataPLUs();
	}
}
