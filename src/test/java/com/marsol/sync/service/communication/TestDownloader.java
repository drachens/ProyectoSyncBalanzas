package com.marsol.sync.service.communication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestDownloader {

    private SyncDataDownloader syncDataDownloader;
    @Test
    public void testDownload() {
        syncDataDownloader = new SyncDataDownloader();
        syncDataDownloader.downloadSystemParameters("C:\\Users\\sistemas\\Desktop\\MARSOL\\SP","192.168.2.84");
    }
}
