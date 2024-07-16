package com.marsol.sync.util;

public class HEXUtils {
	
	public static long hexToCardinal(String hex) {
		long cardinal = Long.parseLong(hex, 16);
		if (cardinal < 0 || cardinal > 0xFFFFFFFFL) {
			throw new IllegalArgumentException("Valor hexadecimal fuera de rango de unit32");
		}
		return cardinal;
	}
	
	public static String cardinalToHex(long cardinal) {
		if (cardinal < 0 || cardinal > 0xFFFFFFFFL) {
			throw new IllegalArgumentException("Valor hexadecimal fuera de rango de unit32");
		}
		return Long.toHexString(cardinal);
	}

}
