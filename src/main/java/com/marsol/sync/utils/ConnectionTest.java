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
        InetAddress ip = InetAddress.getByName(ipAddress);
        logger.debug("Enviando solicitud ping a {}",ipAddress);

        if(ip.isReachable(5000)){
            logger.debug("Solicitud realizada!");
            return true;
        }else{
            logger.debug("Solicitud no encontrada!");
            return false;
        }
    }
}
