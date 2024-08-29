package com.marsol.sync;

import com.marsol.sync.controller.ScalesNetworkController;
import com.marsol.sync.controller.SendPluInfoController;
import com.marsol.sync.utils.LibraryLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class MainClass implements CommandLineRunner {

    static {
        LibraryLoader.loadLibrary("SyncSDK.dll");
    }
    private static final Logger logger = LogManager.getLogger(MainClass.class);
    @Autowired
    private ScalesNetworkController scalesNetworkController;

    @Autowired
    private SendPluInfoController sendPluInfoController;

    public static void main(String[] args) {
        logger.info("Logback está configurado correctamente.");
        SpringApplication.run(MainClass.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        String marca = "HPRT";
        logger.info("Logback está configurado correctamente.");
        System.out.println("Classpath: " + System.getProperty("java.class.path"));
    }
}
