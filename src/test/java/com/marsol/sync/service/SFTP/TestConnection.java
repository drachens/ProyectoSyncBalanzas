package com.marsol.sync.service.SFTP;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class TestConnection {


    @Test
    public void testConnection() throws JSchException, IOException, SftpException {
        ClientSFTP sftpClient = new ClientSFTP("sftpuser","password","localhost",2222);
        sftpClient.connect();
        String localFilePathBase = "C:\\Users\\Drach\\Desktop\\MARSOL\\BC CL-9AI\\sftp-data";
        List<String> files = sftpClient.localFiles("C:\\Users\\Drach\\Desktop\\MARSOL\\BC CL-9AI\\sftp-data");
        /*
        for (String file : files) {
            String newFileName = sftpClient.newFileName(file);
            sftpClient.uploadAndRenameFile(localFilePathBase+"\\"+file,"/upload",newFileName);
        }
         */
        Map<Long,String> fileMap = sftpClient.filesMap(files);
        System.out.println(fileMap);
    }
}
