package com.marsol.sync.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class LibraryLoader {
    public static void loadLibrary(String libName) {
        try {
            // Obtener la biblioteca desde los recursos del JAR
            InputStream in = LibraryLoader.class.getResourceAsStream("/" + libName);
            if (in == null) {
                throw new RuntimeException("No se pudo encontrar la biblioteca: " + libName);
            }

            // Crear un archivo temporal para la biblioteca
            File tempFile = File.createTempFile(libName, "");
            tempFile.deleteOnExit();

            // Escribir el contenido de la biblioteca al archivo temporal
            try (OutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            // Cargar la biblioteca desde el archivo temporal
            System.load(tempFile.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar la biblioteca " + libName, e);
        }
    }
}

