package com.marsol.sync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SyncApplication {
    public static void main(String[] args) {
        SpringApplication.run(SyncApplication.class, args);
        int origenData = 1;
        switch (origenData){
            case 1:
                //Archivo Json
                String ipBalanza = "192.168.5.23";
                String jsonItems = "C:\\Users\\Drach\\Desktop\\MARSOL\\APIWalmart\\jsons_674\\json_items_674_98\\json_items_674_98";
                String jsonInfonut = "C:\\Users\\Drach\\Desktop\\MARSOL\\APIWalmart\\jsons_674\\json_infonut_674_98\\json_infonut_674_98";

        }
    }
}
