package com.marsol.sync.domain.service;

import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.infraestructure.integration.SyncDataDownloader;
import com.marsol.sync.utils.FileReaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para obtener datos existentes en la balanza
 */

@Service
public class ScaleDataReaderService {
    @Value("${directory.uploads}")
    private String path;
    private final SyncDataDownloader syncDataDownloader;
    private static final Logger logger = LoggerFactory.getLogger(ScaleDataReaderService.class);

    public ScaleDataReaderService(){
        this.syncDataDownloader = new SyncDataDownloader();
    }

    /**
     * Función para obtener una lista de LFCodes presentes en la balanza.
     * @param scale
     * @return
     */
    public List<Integer> getProductFromScale(Scale scale) throws Exception {
        String filename = String.join("_",
                "PLU",
                "DELETE",
                String.valueOf(scale.getStore()),
                String.valueOf(scale.getDepartamento()));
        String file_path = String.join(File.separator, path, filename);
        String scale_ip = scale.getIp_Balanza();
        File file_to_delete = new File(file_path);
        List<Integer> scale_products = new ArrayList<>();
        if(scale_ip == null || scale_ip.isEmpty()){
            throw new Exception("scale.getIp_Balanza() no puede ser nulo");
        }

        boolean success = syncDataDownloader.downloadPLU(file_path, scale_ip);
        if(!success){
            logger.error("Error durante la obtención de PLUs de la balanza -> {}", scale_ip);
            throw new RuntimeException("Error durante la obtención de PLUs de la balanza -> "+ scale_ip);
        }
        try{
            scale_products = FileReaderUtil.readFileAndMap(file_path, values -> Integer.parseInt(values[0]));
            if(!scale_products.isEmpty()){
                logger.info("Se obtuvo una lista de {} productos cargados en la balanza -> {}",scale_products.size(),scale_ip);
                System.out.println("PLUs decargados -> "+scale_products.size());
            }
            //Eliminamos o no eliminamos el archivo? no lo sabemos señores.
            //BLOQUE PARA ELIMINAR EL ARCHIVO.-.
            return scale_products;
        } catch (Exception e) {
            logger.error("Error en la lectura del archivo: {}",file_path);
            throw new RuntimeException(e);
        }
    }
}
