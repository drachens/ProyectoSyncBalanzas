package com.marsol.sync.controller;

import com.marsol.sync.app.ConfigLoader;
import com.marsol.sync.model.Infonut;
import com.marsol.sync.model.Item;
import com.marsol.sync.model.Log;
import com.marsol.sync.model.Scale;
import com.marsol.sync.service.api.*;
import com.marsol.sync.service.transform.TransformWalmartPLUs;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestNetworkScalesController {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private AuthService authService;
    @Mock
    private ApiService<Scale> apiService;
    @Mock
    private ConfigLoader configLoader;
    @Mock
    private ScaleService scaleService;
    @Mock
    private InfonutService infonutService;
    @Mock
    private ProductService productService;
    @Mock
    private TransformWalmartPLUs transformWalmartPLUs;

    @InjectMocks
    private ScalesNetworkController scalesNetworkController;

    @InjectMocks
    private SendPluInfoController sendPluInfoController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        restTemplate = new RestTemplate();
        configLoader = new ConfigLoader();
        authService = new AuthService(restTemplate, configLoader);
        apiService = new ApiService<>(restTemplate,authService,configLoader);
        infonutService = new InfonutService(new ApiService<Infonut>(restTemplate,authService,configLoader));
        productService = new ProductService(new ApiService<Item>(restTemplate,authService,configLoader));
        LogService logService = new LogService(new ApiService<Log>(restTemplate,authService,configLoader));
        transformWalmartPLUs = new TransformWalmartPLUs(logService);
        scaleService = new ScaleService(apiService,restTemplate);
        scalesNetworkController = new ScalesNetworkController(restTemplate,authService,apiService,scaleService,configLoader);
        sendPluInfoController = new SendPluInfoController(restTemplate,authService,infonutService,productService,configLoader,transformWalmartPLUs);
    }

    @Test
    public void getScales() throws InterruptedException {
        String marca = "hprt";
        scalesNetworkController.scheduleTask();
        Thread.sleep(2000);
        //sendPluInfoController.evaluateScales();
    }
}
