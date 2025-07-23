package com.marsol.sync.domain.service;

import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.infraestructure.api.LayoutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class AdvertisingTransferService {

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(LayoutService.class);

    @Value("${directory.images.advertising}")
    private String directorioImagenes;



    public AdvertisingTransferService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void uploadImage(String imagePath, String nuevoNombre, Scale scale) {

        String ipBalanza = scale.getiP_Balanza();
        String urlServer = "http://"+ipBalanza+":5000";
        String path = directorioImagenes + File.separator + imagePath;
        File imageFile = new File(path);
        String uploadEndpoint = urlServer+"/uploadAdvertising";
        String nombreArchivo = imageFile.getName();
        String boundary = "*******Marsol*******";
        int maxBufferSize = 4*1204;
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        String twoHyphens = "--";
        String lineEnd = "\r\n";
        int reintento = 0;
        int maxReintentos = 3;
        boolean success = false;
        int timeout = 2000;

        while(reintento < maxReintentos && !success) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(uploadEndpoint).openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary);
                //Crear flujo de datos y enviarlo
                try(DataOutputStream dos = new DataOutputStream(connection.getOutputStream())){
                    //Mandar el nuevo nombre
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"newFileName\"" +lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(nuevoNombre+lineEnd);
                    //Mandar archivo
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + imageFile.getName()+"\"" +lineEnd);
                    dos.writeBytes("Content-Type: image/jpeg"+lineEnd);
                    dos.writeBytes("Content-Transfer-Encoding: binary"+lineEnd);
                    dos.writeBytes(lineEnd);

                    FileInputStream fis = new FileInputStream(imageFile);
                    bytesAvailable = fis.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];
                    bytesRead = fis.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fis.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fis.read(buffer, 0, bufferSize);
                    }
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    //dos.flush();
                    dos.close();
                    fis.close();
                } catch (IOException e) {
                    reintento = 3;
                    logger.error("Error al enviar la imagen {} : {}",imagePath,e.getMessage());
                }
                int responseCode = connection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK) {
                    success = true;
                    logger.info("Imagen de publicidad {} subida correctamente a balanza {}.",nombreArchivo,urlServer);
                }
            } catch (IOException e) {
                reintento++;
                logger.error("Error al subir la imagen de publicidad {} , error: {} , reintento: {}",nombreArchivo, e.getMessage(), reintento);
                //Esperar un tiempo antes de volver a reintentar
                try{
                    Thread.sleep((long) timeout *reintento);

                } catch (InterruptedException ex) {
                    logger.error("Error durante el tiempo de espera, error: {}",ex.getMessage());
                }
            }
        }
        if(!success) {
            logger.error("Error en la subida de la imagen de publicidad {} luego de {} intentos.", nombreArchivo, maxReintentos);
        }
    }
}
