package com.marsol.sync.service.communication;

public class SyncSDKImpl implements SyncSDKIntf{

    @Override
    public long SDK_UpChangePriceLogA(int var1, String var2) {
        return 0;
    }

    @Override
    public long SDK_ExecTaskA(int var1, int var2, int var3, String var4, TSDKOnProgressEvent var5, int var6) {
        return 0;
    }

    @Override
    public void SDK_StopTask(long var1) {

    }

    @Override
    public void SDK_WaitForTask(long var1) {

    }

    @Override
    public void SDK_Initialize(){

    }

    @Override
    public void SDK_Finalize() {

    }

    @Override
    public boolean SDK_GetDevicesInfo(int var1, TSDKDeviceInfo var2) {

        return false;
    }

    @Override
    public int SDK_GetNetworkSectionDevicesInfo(int var1, TSDKDeviceInfo[] var2, int var3) {
        return 0;
    }

    @Override
    public int SDK_GetOnePLUA(int var1, int var2, String var3) {
        return 0;
    }

    @Override
    public int SDK_GetGroupPLUA(int var1, String var2, String var3) {
        return 0;
    }
}
