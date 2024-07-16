package com.marsol.sync.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.marsol.sync.model.TSDKDeviceInfo;
import com.marsol.sync.service.communication.SyncSDK;
import com.marsol.sync.service.communication.SyncSDKFactory;
import com.marsol.sync.service.communication.SyncSDKImpl;
import com.marsol.sync.util.IPUtils;
import com.sun.jna.Pointer;

import java.net.UnknownHostException;
import java.util.List;

import org.junit.Before;

@RunWith(MockitoJUnitRunner.class)
public class SyncSDKServiceTest {
	
	@Mock
	private SyncSDK syncSDK;
	
	@Mock
	private SyncSDKImpl syncSDKImpl;
	
	@InjectMocks
	private SyncSDKFactory syncSDKFactory;

	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		SyncSDKFactory syncSDKFactory = new SyncSDKFactory();
		SyncSDK syncSDKImpl = new SyncSDKImpl();
	}
	
	@Test
	public void testInit() {
		SyncSDK syncSDK = SyncSDKFactory.createSyncSDK();
		boolean init = syncSDK.SDK_Initialize();
		System.out.println(init);
	}
	
	@Test
	public void testDeviceInfo() throws UnknownHostException {
		String Addr = "192.168.5.23";
		long ip = IPUtils.ipToCardinal(Addr);
		SyncSDK syncSDK = new SyncSDKImpl();
		TSDKDeviceInfo deviceInfo = new TSDKDeviceInfo();
		deviceInfo = syncSDK.SDK_GetDeviceInfo(ip);
		System.out.println(deviceInfo);
	}
}
