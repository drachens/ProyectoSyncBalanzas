package com.marsol.sync.service.communication;

public class SyncManager {
    private static volatile SyncSDKIntf instance;

    private SyncManager() {

    }

    public static SyncSDKIntf getInstance() {
        if (instance == null) {
            synchronized (SyncManager.class) {
                if (instance == null) {
                    instance = SyncSDKIntf.INSTANCE;
                    instance.SDK_Initialize();
                }
            }
        }
        return instance;
    }

    public static void finalizeInstance(){
        if(instance != null){
            synchronized (SyncManager.class) {
                if(instance != null){
                    instance.SDK_Finalize();
                    instance = null;
                }
            }
        }
    }
}
