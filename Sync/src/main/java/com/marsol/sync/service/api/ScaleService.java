package com.marsol.sync.service.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.marsol.sync.model.Scale;

@Service
public class ScaleService {

	private final ApiService<Scale> apiService;
	private final Gson gson;
	
	@Autowired
	public ScaleService(ApiService<Scale> apiService){
		this.apiService = apiService;
		this.gson = new Gson();
	}
	
	public List<Scale> getAllScales(){
		return apiService.getData("scales/All", "scales", Scale.class);
	}
	
	public List<Scale> getScaleById(int Id){
		return apiService.getData("scales/"+Id, "scales", Scale.class);
	}
	
	public void createScale(Scale scale) {
		String scaleJSON = null;
		try{
			scaleJSON = gson.toJson(scale);
			System.out.println(scaleJSON);
			apiService.postData("scales/Create", "scales", scaleJSON);
			System.out.println("Balanza creada exitosamente");
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error convirtiendo Scale en JSON");
		}

	}
	
	public Scale deleteScale(int id) {
		return apiService.deleteData("scales/"+id, "scales", Scale.class);
	}
}
