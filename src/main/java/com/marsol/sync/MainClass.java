package com.marsol.sync;

import com.marsol.sync.controller.ScalesNetworkController;
import com.marsol.sync.controller.SendPluInfoController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class MainClass implements CommandLineRunner {

    @Autowired
    private ScalesNetworkController scalesNetworkController;

    @Autowired
    private SendPluInfoController sendPluInfoController;

    public static void main(String[] args) {
        SpringApplication.run(MainClass.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        String marca = "HPRT";
        //scalesNetworkController.scheduleTask();
        //sendPluInfoController.evaluateScales();
    }
}
