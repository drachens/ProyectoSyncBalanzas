package com.marsol.sync.service.communication;

public class SyncDataDownloader {
    private final SyncSDKIntf sync;

    public SyncDataDownloader() {
        this.sync = SyncManager.getInstance();
    }

    public boolean downloadPLU(String filename, String ipString){
        long result;
        int ip = SyncSDKDefine.ipToLong(ipString);
        TSDKOnProgressEvent onProgress = (var1, var2, var3, var4) -> System.out.println("ErrorCode:" + var1  + " nIndex:" +var2 + " nTotal:" + var3 + " nUserDataCode:" + var4 );
        try{
            sync.SDK_Initialize();
            result = sync.SDK_ExecTaskA(ip,1,0,filename,onProgress,111);
            sync.SDK_WaitForTask(result);
            System.out.println("PLUs Descargados en: "+filename);
            return true;
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        }
    }

    public boolean downloadDepartment(String filename, String ipString){
        long result;
        int ip = SyncSDKDefine.ipToLong(ipString);
        TSDKOnProgressEvent onProgress = (var1, var2, var3, var4) -> System.out.println("ErrorCode:" + var1  + " nIndex:" +var2 + " nTotal:" + var3 + " nUserDataCode:" + var4 );
        try{
            sync.SDK_Initialize();
            result = sync.SDK_ExecTaskA(ip,1,1,filename,onProgress,111);
            sync.SDK_WaitForTask(result);
            System.out.println("Departamentos Descargados en: "+filename);
            return true;
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        }
    }
    public boolean downloadCustomBarcode(String filename, String ipString){
        long result;
        int ip = SyncSDKDefine.ipToLong(ipString);
        TSDKOnProgressEvent onProgress = (var1, var2, var3, var4) -> System.out.println("ErrorCode:" + var1  + " nIndex:" +var2 + " nTotal:" + var3 + " nUserDataCode:" + var4 );
        try{
            sync.SDK_Initialize();
            result = sync.SDK_ExecTaskA(ip,1,4,filename,onProgress,111);
            sync.SDK_WaitForTask(result);
            System.out.println("CustomBarcodes Descargados en: "+filename);
            return true;
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        }
    }
    public boolean downloadNotes(String filename, String ipString){
        long result;
        int ip = SyncSDKDefine.ipToLong(ipString);
        TSDKOnProgressEvent onProgress = (var1, var2, var3, var4) -> System.out.println("ErrorCode:" + var1  + " nIndex:" +var2 + " nTotal:" + var3 + " nUserDataCode:" + var4 );
        try{
            sync.SDK_Initialize();
            result = sync.SDK_ExecTaskA(ip,1,5,filename+"1",onProgress,111);
            sync.SDK_WaitForTask(result);
            result = sync.SDK_ExecTaskA(ip,1,6,filename+"2",onProgress,111);
            sync.SDK_WaitForTask(result);
            result = sync.SDK_ExecTaskA(ip,1,7,filename+"3",onProgress,111);
            sync.SDK_WaitForTask(result);
            result = sync.SDK_ExecTaskA(ip,1,8,filename+"4",onProgress,111);
            sync.SDK_WaitForTask(result);
            System.out.println("Notas Descargados en: "+filename);
            return true;
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        }
    }
    public boolean downloadSystemParameters(String filename, String ipString){
        long result;
        int ip = SyncSDKDefine.ipToLong(ipString);
        TSDKOnProgressEvent onProgress = (var1, var2, var3, var4) -> System.out.println("ErrorCode:" + var1  + " nIndex:" +var2 + " nTotal:" + var3 + " nUserDataCode:" + var4 );
        try{
            sync.SDK_Initialize();
            result = sync.SDK_ExecTaskA(ip,1,12,filename,onProgress,111);
            sync.SDK_WaitForTask(result);
            System.out.println("System Parameters Descargados en: "+filename);
            return true;
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        }
    }
}
