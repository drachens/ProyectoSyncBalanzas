package com.marsol.sync;

import com.marsol.sync.controller.ScalesNetworkController;
import com.marsol.sync.controller.SendPluInfoController;
import com.marsol.sync.service.communication.SyncManager;
import com.marsol.sync.service.communication.SyncSDKIntf;
import com.marsol.sync.utils.LibraryLoader;
import com.sun.jna.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class MainClass implements CommandLineRunner {
    @Value("${library.path}")
    private String libraryPath;
    static {
        String libraryName = Platform.isWindows() ? "SyncSDK.dll" : "libSyncSDK.so";
        LibraryLoader.loadLibrary(libraryName);
    }

    private static final Logger logger = LoggerFactory.getLogger(MainClass.class);

    public static void main(String[] args) {
        SpringApplication.run(MainClass.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try{
            System.setProperty("java.library.path", libraryPath);
            logger.info("Libreria dll cargada: {}",libraryPath);
        }catch(Exception e){
            logger.error("Error al cargar la libreria dll. {}", libraryPath);
        }
    }
}
