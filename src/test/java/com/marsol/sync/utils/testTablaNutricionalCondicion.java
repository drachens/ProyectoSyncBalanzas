package com.marsol.sync.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import com.marsol.sync.utils.TablaNutricionalCondition;

@RunWith(MockitoJUnitRunner.class)
public class testTablaNutricionalCondicion {
	
	@Test
	public void testCondicional() {
		String lol = TablaNutricionalCondition.formateoLineaALinea("Proteinas(g)|3.7   |4.8");
		System.out.println(lol);
	}
}
