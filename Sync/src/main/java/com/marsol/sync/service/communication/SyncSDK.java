package com.marsol.sync.service.communication;

import com.marsol.sync.model.TSDKDeviceInfo;
import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;

public interface SyncSDK extends Library {

	SyncSDK INSTANCE = Native.load((Platform.isWindows() ? "SyncSDK":"syncsdk"), SyncSDK.class);

	//Metodos de las funciones nativas del SDK
	boolean SDK_Initialize();
	abstract void SDK_Finalize();
	abstract TSDKDeviceInfo SDK_GetDeviceInfo(long Addr);
	abstract int SDK_GetNetworkSectionDevicesInfo(long Addr, Pointer DeviceInfos, int Count);
	abstract Pointer SDK_ExecTaskA(long Addr, int ProcType, int DataType, String filename, TSDKOnProgressEvent OnProgress, Pointer UserData);
	abstract Pointer SDK_ExecTask(long Addr, int ProcType, int DataType, String filename, TSDKOnProgressEvent OnProgress, Pointer UserData);
	abstract Pointer SDK_ExecTaskW(long Addr, int ProcType, int DataType, String filename, TSDKOnProgressEvent OnProgress, Pointer UserData);
	abstract int SDK_GetLastTaskError();
	abstract void SDK_StopTask(Pointer THandle);
	//THandle = 0 Significa detener todas las tareas
	abstract void SDK_WaitForTask(Pointer THandle);

	interface TSDKOnProgressEvent extends Callback {
		abstract void callback(int var1, int var2, int var3, int var4);
	}
}
