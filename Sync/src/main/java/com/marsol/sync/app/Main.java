package com.marsol.sync.app;

import java.net.UnknownHostException;

import com.marsol.sync.model.TSDKDeviceInfo;
import com.marsol.sync.service.communication.SyncSDK;
import com.marsol.sync.service.communication.SyncSDKImpl;
import com.sun.jna.Native;
import com.marsol.sync.util.*;

public class Main {

	public static void main(String[] args) throws UnknownHostException {
		//1062730473
		try {
			SyncSDK syncSDK = SyncSDK.INSTANCE;
			TSDKDeviceInfo deviceInfo = new TSDKDeviceInfo();
			System.out.println(deviceInfo);
			int ip = IPUtils.ipToCardinal("192.168.5.23");
			System.out.println(ip);
			deviceInfo = syncSDK.SDK_GetDeviceInfo(ip);
			System.out.println(deviceInfo);
		} catch (UnsatisfiedLinkError e) {
			System.err.println("Error al cargar la biblioteca nativa: " + e.getMessage());
		}
	/*
		try {
			SyncSDK syncSDK = SyncSDK.INSTANCE;
			boolean init = syncSDK.SDK_Initialize();
			System.out.println(init);
			syncSDK.SDK_Finalize();
		} catch(UnsatisfiedLinkError e) {
			 System.err.println("Error al cargar la biblioteca nativa: " + e.getMessage());
		}
	*/
		/*
		SyncSDK syncSDK = null;
		try {
			syncSDK = new SyncSDKImpl();
			boolean init = syncSDK.SDK_Initialize();
			if(!init) {
				throw new IllegalStateException("Fallo al inicializar el SDK"); 
			}
			
		} finally {
			syncSDK.SDK_Finalize();
		}*/
	}
}
