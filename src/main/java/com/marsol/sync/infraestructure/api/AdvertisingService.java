package com.marsol.sync.infraestructure.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.domain.service.AdvertisingTransferService;
import com.marsol.sync.model.Advertising;
import com.marsol.sync.model.Layout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.util.Collections;
import java.util.List;

@Service
public class AdvertisingService {

    private static final Logger logger = LoggerFactory.getLogger(LayoutService.class);
    private final AdvertisingTransferService advertisingTransferService;
    @Value("${wm.endpoint.advertising}")
    private String urlBase;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    public AdvertisingService(RestTemplate restTemplate, AdvertisingTransferService advertisingTransferService) {
        this.restTemplate = restTemplate;
        this.advertisingTransferService = advertisingTransferService;
    }

    public List<Advertising> getAdvertising(int storeNbr, int deptNbr) {
        String url = urlBase + storeNbr + "/" + deptNbr;
        logger.debug("[getAdvertising] Consultando URL: {}", url);

        try {
            ResponseEntity<List<Advertising>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Advertising>>() {}
            );

            List<Advertising> publicidades = response.getBody();
            logger.debug("[getAdvertising] Respuesta obtenida: {}", publicidades);
            return publicidades;
        } catch (Exception e) {
            logger.error("[getAdvertising] Error al obtener publicidad desde la URL: {} - Error: {}", url, e.getMessage());
            return null;
        }
    }



    public void cargaPublicidad(Scale scale) {
        logger.debug("[cargaPublicidad] Iniciando carga de publicidad para tienda {} y departamento {}",
                scale.getStore(), scale.getDepartamento());

        List<Advertising> publicidades = getAdvertising(scale.getStore(), scale.getDepartamento());

        if (publicidades != null && !publicidades.isEmpty()) {
            logger.info("[cargaPublicidad] Subiendo publicidad para tienda {} y departamento {}.",
                    scale.getStore(), scale.getDepartamento());

            for (Advertising publicidad : publicidades) {
                String path = publicidad.getImagenPromo(); // Ej: PantallaBalanza_6miclub.png
                String fechaTermino = publicidad.getFechaTermino(); // Ej: 20250722

                // Extraer el nombre base sin extensión
                int dotIndex = path.lastIndexOf(".");
                String baseName = (dotIndex != -1) ? path.substring(0, dotIndex) : path;

                // Agregar la fecha y la hora 2200, y luego la extensión
                String newName = baseName + "-" + fechaTermino + "2200.png";

                // Resultado: PantallaBalanza_6miclub-202507222200.png
                System.out.println(newName);


                logger.debug("[cargaPublicidad] Subiendo imagen: path={} newName={}", path, newName);
                advertisingTransferService.uploadImage(path, newName, scale);
            }

            logger.info("[cargaPublicidad] Publicidad cargada exitosamente.");
        } else {
            logger.warn("[cargaPublicidad] No se encontró publicidad para tienda {} y departamento {}.",
                    scale.getStore(), scale.getDepartamento());
        }
    }




}
