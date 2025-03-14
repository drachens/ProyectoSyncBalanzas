package com.marsol.sync;

import com.marsol.sync.utils.LibraryLoader;
import com.sun.jna.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MainClass implements CommandLineRunner {

    @Value("${library.path:}") // Maneja valores nulos en tests
    private String libraryPath;

    static {
        if (!isRunningTest()) { // Solo carga la librería si no estamos en un test
            String libraryName = Platform.isWindows() ? "SyncSDK.dll" : "libSyncSDK.so";
            LibraryLoader.loadLibrary(libraryName);
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(MainClass.class);

    public static void main(String[] args) {
        SpringApplication.run(MainClass.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (!isRunningTest()) {
            try {
                System.setProperty("java.library.path", libraryPath);
                logger.info("Librería DLL cargada: {}", libraryPath);
            } catch (Exception e) {
                logger.error("Error al cargar la librería DLL. {}", libraryPath);
            }
        }
    }

    private static boolean isRunningTest() {
        return "true".equals(System.getProperty("spring.test.context"));
    }
}