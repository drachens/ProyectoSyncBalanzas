package com.marsol.sync.service.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marsol.sync.model.Scale;

@Service
public class ScaleService {

	private final ApiService<Scale> apiService;
	
	@Autowired
	public ScaleService(ApiService<Scale> apiService){
		this.apiService = apiService;
	}
	
	public List<Scale> getAllScales(){
		return apiService.getData("scales/All", "scales", Scale.class);
	}
	
	public List<Scale> getScaleById(int Id){
		return apiService.getData("scales/"+Id, "scales", Scale.class);
	}
	
	public List<Scale> createScale(Scale scale) {
		ObjectMapper objectMapper = new ObjectMapper();
		String scaleJSON = null;
		try {
			scaleJSON = objectMapper.writeValueAsString(scale);
		} catch(Exception e) {
			e.getStackTrace();
		}
		return apiService.postData("scales/Create", "scales", scaleJSON, Scale.class);
	}
}
