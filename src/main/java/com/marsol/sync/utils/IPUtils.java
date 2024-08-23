package com.marsol.sync.utils;

import com.sun.jna.NativeLong;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/*
	Esta clase contiene funciones relacionadas con la conversión de direcciones IP para el uso correcto del SDK
 */

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

	public static int ipToLong(String strIp) {
		int[] ip = new int[4];
		int position1 = strIp.indexOf(".");
		int position2 = strIp.indexOf(".", position1 + 1);
		int position3 = strIp.indexOf(".", position2 + 1);
		ip[0] = Integer.parseInt(strIp.substring(0, position1));
		ip[1] = Integer.parseInt(strIp.substring(position1 + 1, position2));
		ip[2] = Integer.parseInt(strIp.substring(position2 + 1, position3));
		ip[3] = Integer.parseInt(strIp.substring(position3 + 1));

		return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
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

	public static NativeLong ipToNativeLong(String ip) {
		String[] parts = ip.split("\\.");
		int ipInt = (Integer.parseInt(parts[0]) & 0xFF) |
				((Integer.parseInt(parts[1]) & 0xFF) << 8) |
				((Integer.parseInt(parts[2]) & 0xFF) << 16) |
				((Integer.parseInt(parts[3]) & 0xFF) << 24);
		return new NativeLong(ipInt);
	}
	public static long ipToCardinal_2(String ip) throws UnknownHostException {
		InetAddress inetAddress = InetAddress.getByName(ip);
		byte[] addressBytes = inetAddress.getAddress();
		// Convierte los bytes a un valor entero sin signo
		return ByteBuffer.wrap(addressBytes).getInt() & 0xFFFFFFFFL;
	}
}
