package com.marsol.sync.service.communication;

import com.marsol.sync.model.TSDKDeviceInfo;
import com.sun.jna.Pointer;

public class SyncSDKImpl implements SyncSDK {
	//Metodos que implementan la interfaz

	@Override
	public boolean SDK_Initialize() {
		System.out.println("Inicializando SDK...");
		return SyncSDK.INSTANCE.SDK_Initialize();
	}

	@Override
	public void SDK_Finalize() {
		System.out.println("Finalizando SDK...");
	}

	@Override
	public TSDKDeviceInfo SDK_GetDeviceInfo(long Addr) {
		System.out.println("Obteniendo información de dispositivos del dispositivo: "+Addr+" ...");
		return SyncSDK.INSTANCE.SDK_GetDeviceInfo(Addr);
	}

	@Override
	public int SDK_GetNetworkSectionDevicesInfo(long Addr, Pointer DeviceInfos, int Count) {
		System.out.println("Obteniendo información del segmento de red: "+Addr+" ...");
		return INSTANCE.SDK_GetNetworkSectionDevicesInfo(Addr, DeviceInfos, Count);
	}

	@Override
	public Pointer SDK_ExecTaskA(long Addr, int ProcType, int DataType, String filename, TSDKOnProgressEvent OnProgress,
			Pointer UserData) {
		System.out.println("Ejecutando tarea...");
		return INSTANCE.SDK_ExecTaskA(Addr, ProcType, DataType, filename, OnProgress, UserData);
	}

	@Override
	public Pointer SDK_ExecTask(long Addr, int ProcType, int DataType, String filename, TSDKOnProgressEvent OnProgress,
			Pointer UserData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pointer SDK_ExecTaskW(long Addr, int ProcType, int DataType, String filename, TSDKOnProgressEvent OnProgress,
			Pointer UserData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SDK_StopTask(Pointer THandle) {
		System.out.println("Deteniendo la tarea: "+THandle+" ...");
	}

	@Override
	public void SDK_WaitForTask(Pointer THandle) {
		System.out.println("Esperando la tarea: "+THandle+" ...");

	}

	@Override
	public int SDK_GetLastTaskError() {
		// TODO Auto-generated method stub
		return 0;
	}

}
