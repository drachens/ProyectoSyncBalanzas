package com.marsol.sync.service.communication;

public class SyncSDKFactory {

	public static SyncSDK createSyncSDK() {
		return new SyncSDKImpl();
	}
}
