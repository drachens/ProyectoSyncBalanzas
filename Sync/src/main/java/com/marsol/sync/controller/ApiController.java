package com.marsol.sync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.marsol.sync.service.api.ProductService;
import com.marsol.sync.model.Item;

import java.util.List;

@RestController
public class ApiController {

	private final ProductService productService;
	
	@Autowired
	public ApiController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping("/")
	public List<Item> getItemsDepts(@RequestParam int storeNbr, @RequestParam int deptNbr) {
		List<Item> items = productService.getItemsDept(storeNbr, deptNbr);
		return items;
	}
}
