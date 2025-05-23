package com.marsol.sync.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactoryFriend;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ConnectionTest {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionTest.class);

    public static boolean sendPingRequest(String ipAddress) throws IOException {
        try{
            InetAddress ip = InetAddress.getByName(ipAddress);
            boolean reachable = ip.isReachable(5000);
            if(reachable) {
                logger.debug("Ping exitoso a la dirección {}",ip.getHostAddress());
                return true;
            }else{
                logger.debug("No se recibió respuesta al ping desde {}",ip.getHostAddress());
                return false;
            }
        }catch(IOException e){
            logger.debug("Error al intentar hacer ping a la IP: {}",e.getMessage());
            return false;
        }
    }
}
