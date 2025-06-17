package com.marsol.sync.domain.service;

import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.infraestructure.integration.SyncDataDownloader;
import com.marsol.sync.utils.FileReaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final SyncDataDownloader syncDataDownloader;
    private static final Logger logger = LoggerFactory.getLogger(ScaleDataReaderService.class);

    @Value("${directory.uploads}")
    private String path;

    @Autowired
    public ScaleDataReaderService(SyncDataDownloader syncDataDownloader) {
        this.syncDataDownloader = syncDataDownloader;
    }

    /**
     * Función para obtener una lista de LFCodes presentes en la balanza.
     * @param scale
     * @return
     */
    public List<Integer> getProductFromScale(Scale scale) throws Exception {
        String filename = String.join("_",
                "plu",
                "delete",
                String.valueOf(scale.getStore()),
                String.valueOf(scale.getDepartamento()));
        String file_path = String.join(File.separator, path, filename);
        String scale_ip = scale.getiP_Balanza();
        File file_to_delete = new File(file_path);
        List<Integer> scale_products = new ArrayList<>();
        if(scale_ip == null || scale_ip.isEmpty()){
            throw new Exception("scale.getiP_Balanza() no puede ser nulo");
        }

        boolean success = syncDataDownloader.downloadPLU(file_path, scale_ip);
        if(!success){
            throw new RuntimeException("Error durante la obtención de PLUs de la balanza -> "+ scale_ip);
        }
        try{
            scale_products = FileReaderUtil.readFileAndMap(file_path, values -> Integer.parseInt(values[0]));
            if(!scale_products.isEmpty()){
                logger.info("Se obtuvo una lista de {} productos cargados en la balanza -> {}",scale_products.size(),scale_ip);
                return scale_products;
            }else{
                logger.info("No existen productos cargados en la balanza {}",scale_ip);
                throw new Exception("No existen productos cargados en la balanza "+scale_ip);
            }
            //Eliminamos o no eliminamos el archivo? no lo sabemos señores.
            //BLOQUE PARA ELIMINAR EL ARCHIVO.-.
        } catch (Exception e) {
            throw new RuntimeException("Error en la lectura del archivo: "+file_path);
        }
    }
}
