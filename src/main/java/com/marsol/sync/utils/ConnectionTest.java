package com.marsol.sync.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactoryFriend;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class ConnectionTest {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionTest.class);

    //@Value("${wm.ping.max_attemps}")
    private static int max_attempts = 5;

    public static boolean sendPingRequest(String ipAddress) throws IOException {
        boolean result = false;
        int iterador = 1;
        while (!result && iterador<=max_attempts) {
            try{
                InetAddress ip = InetAddress.getByName(ipAddress);
                boolean reachable = ip.isReachable(1000);
                if(reachable) {
                    result = true;
                    logger.debug("Ping exitoso a la dirección {}",ip.getHostAddress());
                    return true;
                }else{
                    iterador++;
                    logger.debug("No se recibió respuesta al ping desde {}",ip.getHostAddress());

                }
            }catch(IOException e){
                logger.debug("Error al intentar hacer ping a la IP: {}",e.getMessage());
                return false;
            }
        }
        return result;
    }

}
