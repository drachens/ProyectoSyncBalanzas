package com.marsol.sync.service.images;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.marsol.sync.model.Layout;
import com.marsol.sync.model.Scale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class Transfer {
    private static final Logger logger = LoggerFactory.getLogger(Transfer.class);
    @Value("${directory.images}")
    private static String directorioImagenes;

    public static void uploadImage(String server, String imagePath, String nuevoNombre) {
        File imageFile = new File(imagePath);
        String uploadEndpoint = server+"/upload";
        String nombreArchivo = imageFile.getName();
        String boundary = "*******Marsol*******";
        int maxBufferSize = 4*1204;
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        String twoHyphens = "--";
        String lineEnd = "\r\n";
        int reintento = 0;
        int maxReintentos = 5;
        boolean success = false;
        int timeout = 1000;

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
                    reintento++;
                    logger.error("Error al enviar la data: {}",e.getMessage());
                }
                int responseCode = connection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK) {
                    success = true;
                    logger.info("Imagen {} subida correctamente.",nombreArchivo);
                } else{
                    reintento++;
                    logger.error("Error al subir la imagen {} , error: {} ",nombreArchivo, responseCode);
                }
            } catch (IOException e) {
               reintento++;
               logger.error("Error al subir la imagen {} , error: {} , reintento: {}",nombreArchivo, e.getMessage(), reintento);
               //Esperar un tiempo antes de volver a reintentar
               try{
                   Thread.sleep((long) timeout *reintento);

               } catch (InterruptedException ex) {
                   logger.error("Error durante el tiempo de espera, error: {}",ex.getMessage());
               }
            }
        }
        if(!success) {
            logger.error("Fallo la subida de la imagen {} luego de {} intentos.", nombreArchivo, maxReintentos);
        }
    }
                /*
                try {
                    int responseCode = connection.getResponseCode();
                    //System.out.println(responseCode);
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        // Imprimir la respuesta
                        logger.info("");
                        System.out.println("Respuesta del servidor: " + response);
                    } else {
                        System.out.println("Error en la conexión: " + responseCode);
                    }

                } catch (IOException e) {
                    System.out.println("Error respondeCode "+e.getMessage());
                }
                 */

    public static void cargarLayout(Scale scale, List<Layout> layouts){
        String ipBalanza = scale.getIpBalanza();
        String urlServer = "http://"+ipBalanza+":5000";
        String nombreImagenOriginal;
        String rutaImagen;
        String nuevoNombreImagen;

        List<Integer> listaPluBalanza = listarImagenes(urlServer);

        for(Layout layout : layouts){
            int pluCode = layout.getPlu();
            if(!listaPluBalanza.contains(pluCode)){
                nuevoNombreImagen = layout.getPlu()+".jpg";
                nombreImagenOriginal = layout.getImagen();
                rutaImagen = "C:\\Users\\sistemas\\Desktop\\imagenes\\" + nombreImagenOriginal;
                try{
                    uploadImage(urlServer, rutaImagen, nuevoNombreImagen);
                } catch (Exception e){
                    logger.error("Error al cargar la imagen {} a la balanza {}, error: {}",nombreImagenOriginal,ipBalanza,e.getMessage());
                }
            }
        }
    }

    public static List<Integer> listarImagenes(String server){
        String listImagesEndpoint = server+"/listImages";
        int intento = 0;
        int maxIntentos = 5;
        boolean success = false;
        int timeout = 1000;
        List<Integer> listaImagenes = new ArrayList<>();
        while(intento < maxIntentos && !success) {
            try{
                URL url = new URL(listImagesEndpoint);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response =  new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                int responseCode = connection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK) {
                    success = true;
                    String responseJsonString = response.toString();
                    JsonObject responseJsonObject = JsonParser.parseString(responseJsonString).getAsJsonObject();
                    JsonArray imagenesArray = responseJsonObject.get("imagenes").getAsJsonArray();
                    for(JsonElement elemento : imagenesArray){
                        int elementoFiltrado = eliminarExtension(elemento.getAsString());
                        if(elementoFiltrado != 0){
                            listaImagenes.add(elementoFiltrado);
                        }
                    }
                    logger.info("Lista de imagenes obtenida de la balanza. Contiene {} imagenes.", listaImagenes.size());
                } else {
                    logger.error("Error al intentar obtener la lista de imagenes de la balanza, error: {}", responseCode);
                }
            }catch (IOException e) {
                logger.error("Error: {}", e.getMessage());
                intento++;
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException ex) {
                    logger.error("Error durante el tiempo de espera, error: {}", ex.getMessage());
                }
            }
        }
        if (!success) {
            logger.error("Fallo al intentar obtener lista de imagenes luego de {} intentos.", maxIntentos);
            return listaImagenes;
        }
        return listaImagenes;
    }

    public static int eliminarExtension(String filename){
        int lastIndexPoint = filename.lastIndexOf(".");
        String filenameSinExtension = filename.substring(0,lastIndexPoint);
        try{
            return Integer.parseInt(filenameSinExtension);
        } catch (Exception e){
            return 0;
        }

    }
}
