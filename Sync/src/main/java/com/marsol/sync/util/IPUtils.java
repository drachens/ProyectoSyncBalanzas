package com.marsol.sync.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class IPUtils {

	public static int ipToCardinal(String ip) throws UnknownHostException {
		InetAddress inetAddress = Inet4Address.getByName(ip);
		byte[] bytes = inetAddress.getAddress();
		
		int cardinal = ((bytes[0] & 0xFF) << 24) |
                ((bytes[1] & 0xFF) << 16) |
                ((bytes[2] & 0xFF) << 8) |
                (bytes[3] & 0xFF);
		return cardinal;
	}
	
	public static String cardinalToIp(long cardinal) throws UnknownHostException {
		byte[] bytes = new byte[] {
				(byte) ((cardinal >> 24) & 0xFF),
				(byte) ((cardinal >> 16) & 0xFF),
				(byte) ((cardinal >> 8) & 0xFF),
				(byte) (cardinal & 0xFF)
		};
		InetAddress inetAddress = InetAddress.getByAddress(bytes);
		return inetAddress.getHostAddress();
	}
}
