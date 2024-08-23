package com.marsol.sync.service.communication;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public interface SyncSDKIntf extends Library {
    SyncSDKIntf INSTANCE = (SyncSDKIntf) Native.loadLibrary(Platform.isWindows() ? "SyncSDK.dll" : "libSyncSDK.so", SyncSDKIntf.class);

    long SDK_UpChangePriceLogA(int var1, String var2);

    long SDK_ExecTaskA(int var1, int var2, int var3, String var4, TSDKOnProgressEvent var5, int var6);

    void SDK_StopTask(long var1);

    void SDK_WaitForTask(long var1);

    void SDK_Initialize();

    void SDK_Finalize();

    boolean SDK_GetDevicesInfo(int var1, TSDKDeviceInfo var2);

    int SDK_GetNetworkSectionDevicesInfo(int var1, TSDKDeviceInfo[] var2, int var3);

    int SDK_GetOnePLUA(int var1, int var2, String var3);

    int SDK_GetGroupPLUA(int var1, String var2, String var3);
}
