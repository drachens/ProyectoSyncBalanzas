package com.marsol.sync.domain.service;

import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.infraestructure.integration.SyncDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class DeleteScaleProductService {
    private final static Logger logger = LoggerFactory.getLogger(DeleteScaleProductService.class);
    private final SyncDataLoader syncDataLoader;

    @Value("${directory.pendings}")
    private String pendings;

    @Autowired
    public DeleteScaleProductService(SyncDataLoader syncDataLoader) {
        this.syncDataLoader = syncDataLoader;
    }

    public void deleteFromScale(Scale scale) throws Exception {
        String filename = String.join("_","pluDelete",String.valueOf(scale.getStore()),String.valueOf(scale.getDepartamento()));
        String file_path = pendings+filename;
        Path path = Paths.get(file_path);
        boolean file_exists = Files.exists(path);
        String ip = scale.getIp_Balanza();
        long count = contarLineasSinHeader(path);

        if(!file_exists){
            logger.error("No existe el archivo: {}",file_path);
            throw new FileNotFoundException("No existe el archivo: "+file_path);
        }
        if(ip == null){
            throw new FileNotFoundException("IP nula, campo invalido");
        }
        logger.info("Se eliminaran {} productos en la balanza -> {}",count,ip);
        boolean result = syncDataLoader.deletePLU(file_path,ip);
        if(!result){
            logger.error("Error durante la eliminación de productos en balanza -> {}",ip);
            throw new Exception("Error durante la eliminación de productos en balanza -> "+ip);
        }
        logger.info("Productos eliminados en la balanza -> {}",ip);

        try{
            Files.delete(path);
            logger.info("Archivo eliminado: {}",file_path);
        }catch(IOException e){
            logger.warn("Error al eliminar el archivo: "+file_path);
        }
    }

    public long contarLineasSinHeader(Path path) {
        try (Stream<String> lines = Files.lines(path)) {
            return lines.skip(1).count();
        } catch (IOException | SecurityException e) {
            logger.error("Error al contar las lineas del archivo: {}", path);
            return -1L;
        }
    }
}
