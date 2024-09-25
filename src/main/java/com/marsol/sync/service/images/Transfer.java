package com.marsol.sync.service.images;


import com.marsol.sync.service.communication.SyncDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class Transfer {

    private static final Logger logger = LoggerFactory.getLogger(Transfer.class);

    public static void uploadImage(String targetUrl, String imagePath) {
        File imageFile = new File(imagePath);
        System.out.println("Archivo con nombre: " + imageFile.getName());
        System.out.println(imageFile.length());
        String boundary = "**********";
        int maxBufferSize = 4*1204;
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        String twoHyphens = "--";
        String lineEnd = "\r\n";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(targetUrl).openConnection();
            //System.out.println(connection.getResponseCode());
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary);
            //Crear flujo de datos y enviarlo
            try(DataOutputStream dos = new DataOutputStream(connection.getOutputStream())){
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
                //fis.close();
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                dos.flush();
                dos.close();
                fis.close();
            } catch (IOException e) {
                System.out.println("Error writing data "+e.getMessage());
            }
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
                    System.out.println("Respuesta de PHP: " + response);
                } else {
                    System.out.println("Error en la conexión: " + responseCode);
                }

            } catch (IOException e) {
                System.out.println("Error respondeCode "+e.getMessage());
            }

        } catch (IOException e) {
            System.out.println("Error openConnection "+e.getMessage());
            logger.error(e.getMessage());
        }
    }
}
