package com.marsol.sync.application;

import com.marsol.sync.infraestructure.integration.SyncDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SystemParametersController {

    private final SyncDataLoader syncDataLoader;

    @Value("${sysparameters.path}")
    private String directory;

    private static final Logger logger = LoggerFactory.getLogger(SystemParametersController.class);

    @Autowired
    public SystemParametersController(SyncDataLoader syncDataLoader) {
        this.syncDataLoader = syncDataLoader;
    }

    public void loadSystemParameters(String ip) {
        try {
            syncDataLoader.loadSystemParameters(directory, ip, 111);
            logger.info("Carga de parámetros del sistema para IP {} completada.", ip);
        } catch (Exception e) {
            logger.error("Error cargando parámetros del sistema para IP " + ip, e);
            throw new RuntimeException(e);
        }
    }

}
