package com.marsol.sync;

import com.sun.jna.NativeLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@PropertySource("classpath:application-test.properties")
public class MainClass implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MainClass.class);

    public static void main(String[] args) {
        SpringApplication.run(MainClass.class, args);
    }

    @Override
    public void run(String... args) {
        logger.info("Aplicación iniciada correctamente.");

        try {
            NativeLibrary lib = NativeLibrary.getInstance("SyncSDK");
            System.out.println("✅ SDK cargado desde: " + lib.getFile().getAbsolutePath());
        } catch (Exception e) {
            System.err.println("❌ No se pudo cargar el SDK: " + e.getMessage());
        }

        // Si quieres hacer algo extra al iniciar, lo pones aquí.
    }
}
