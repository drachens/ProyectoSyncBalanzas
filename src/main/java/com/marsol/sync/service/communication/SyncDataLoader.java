package com.marsol.sync.service.communication;

public class SyncDataLoader {
    private final SyncSDKIntf sync;

    public SyncDataLoader() {
        this.sync = SyncManager.getInstance();
    }

    public boolean loadPLU(String filename, String ipString){
        long result;
        long result_0;
        int ip = SyncSDKDefine.ipToLong(ipString);
        TSDKOnProgressEvent onProgress = (var1, var2, var3, var4) -> System.out.println("ErrorCode:" + var1  + " nIndex:" +var2 + " nTotal:" + var3 + " nUserDataCode:" + var4 );
        try{
            System.out.println("\nEliminando PLU's");
            result_0 = sync.SDK_ExecTaskA(ip,2,0,"",onProgress,111);
            sync.INSTANCE.SDK_WaitForTask(result_0);
            System.out.println("\nEliminación Completa.");
            System.out.println("\nCargando PLU's...");
            result = sync.SDK_ExecTaskA(ip,0,0,filename,onProgress,111);
            sync.INSTANCE.SDK_WaitForTask(result);
            System.out.println("\nCarga Completa.");
            return true;
        }catch(Exception e){
            System.out.println("Error al cargar PLU: " + e.getMessage());
            return false;
        }
    }

    public boolean loadNotes(String filename, String ipString, int typeNote){
        long result;
        long result_0;
        int ip = SyncSDKDefine.ipToLong(ipString);
        TSDKOnProgressEvent onProgress = (var1, var2, var3, var4) -> System.out.println("ErrorCode:" + var1  + " nIndex:" +var2 + " nTotal:" + var3 + " nUserDataCode:" + var4 );
        try{
            switch(typeNote){
                case 1:
                    System.out.println("\nEliminando Nota 1...");
                    result_0 = sync.SDK_ExecTaskA(ip,2,5,"",onProgress,111);
                    sync.SDK_WaitForTask(result_0);
                    System.out.println("\nEliminación Completa.");
                    System.out.println("\nCargando Nota 1...");
                    result = sync.SDK_ExecTaskA(ip,0,5,filename,onProgress,111);
                    sync.SDK_WaitForTask(result);
                    System.out.println("\nCarga Completa.");
                    break;
                case 2:
                    System.out.println("\nEliminando Nota 2...");
                    result_0 = sync.SDK_ExecTaskA(ip,2,6,"",onProgress,111);
                    sync.SDK_WaitForTask(result_0);
                    System.out.println("\nEliminación Completa.");
                    System.out.println("\nCargando Nota 2...");
                    result = sync.SDK_ExecTaskA(ip,0,6,filename,onProgress,111);
                    sync.SDK_WaitForTask(result);
                    System.out.println("\nCarga Completa.");
                    break;
                case 3:
                    System.out.println("\nEliminando Nota 3...");
                    result_0 = sync.SDK_ExecTaskA(ip,2,7,"",onProgress,111);
                    sync.SDK_WaitForTask(result_0);
                    System.out.println("\nEliminación Completa.");
                    System.out.println("\nCargando Nota 3...");
                    result = sync.SDK_ExecTaskA(ip,0,7,filename,onProgress,111);
                    sync.SDK_WaitForTask(result);
                    System.out.println("\nCarga Completa.");
                    break;
                case 4:
                    System.out.println("\nEliminando Nota 4...");
                    result_0 = sync.SDK_ExecTaskA(ip,2,8,"",onProgress,111);
                    sync.SDK_WaitForTask(result_0);
                    System.out.println("\nEliminación Completa.");
                    System.out.println("\nCargando Nota 4...");
                    result = sync.SDK_ExecTaskA(ip,0,8,filename,onProgress,111);
                    sync.SDK_WaitForTask(result);
                    System.out.println("\nCarga Completa.");
                    break;
                default:
                    System.out.println("\nNúmero de nota:"+typeNote+" no encontrado.");
                    break;
            }
            return true;
        }catch(Exception e){
            System.out.println("Error al cargar Notas: " + e.getMessage());
            return false;
        }
    }
}
