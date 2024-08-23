/*
package com.marsol.sync.service.communication;

public class main {
    static TSDKOnProgressEvent OnProgressEvent = new TSDKOnProgressEvent(){
        @Override
        public void callback(int nErrorCode,int nIndex,int nTotal,int nUserDataCode) {
            // TODO Auto-generated method stub
            System.out.println("ErrorCode:" + nErrorCode  + " nIndex:" +nIndex + " nTotal:" + nTotal + " nUserDataCode:" + nUserDataCode );
        }
    };

    public static void main(String[] args) {
        SyncSDKIntf.INSTANCE.SDK_Initialize();
        int ip = SyncSDKDefine.ipToLong("192.168.5.23");
        System.out.println("ip:" + ip);
        TSDKDeviceInfo DeviceInfo = new TSDKDeviceInfo();
        boolean resok = SyncSDKIntf.INSTANCE.SDK_GetDevicesInfo(ip,DeviceInfo);
        System.out.println("resok:" + resok + " -- ProtocolType:"+  DeviceInfo.MacAddr);
        System.out.printf(System.getProperty("user.dir") + "\\PLU.txt");
        long res = SyncSDKIntf.INSTANCE.SDK_ExecTaskA(ip, SyncSDKDefine.SDK_ProtocolType_None,SyncSDKDefine.Sync_Action_DownLoad, System.getProperty("user.dir")+ "\\PLU.txt", OnProgressEvent, 1234);
        SyncSDKIntf.INSTANCE.SDK_WaitForTask(res);
        System.out.println(res);
    }
}


 */