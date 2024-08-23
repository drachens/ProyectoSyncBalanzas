package com.marsol.sync.service.SFTP;

import com.jcraft.jsch.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientSFTP {
    private String username;
    private String password;
    private String host;
    private int port;
    private Session session;
    private ChannelSftp channel;
    private final static int WINDOW_SIZE = 6 * 1024; //6Kb
    private final static int TIMEOUT = 5000; //5s

    public ClientSFTP(String username, String password, String host, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException, JSchException {
        JSch jsch = new JSch();
        session = jsch.getSession(username,host,port);
        session.setPassword(password);

        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        session.connect();

        channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();
        System.out.println("Conectado a " + host + ":" + port);
    }

    public void disconnect() {
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }

    public Vector<ChannelSftp.LsEntry> listFiles(String remoteDir) throws SftpException {
        return channel.ls(remoteDir);
    }

    public boolean fileExist(String fileName){
        try{
            System.out.println("Verificando el archivo: "+fileName);
            channel.lstat(fileName);

            return true;
        } catch (SftpException e) {
            if(e.id == channel.SSH_FX_NO_SUCH_FILE){
                return false;
            } else{
                return true;
            }

        }
    }

    public void uploadAndRenameFile(String localFilePath, String remoteFilePath, String newFileName) throws SftpException {
        File localFile = new File(localFilePath);
        channel.cd(remoteFilePath);

        //verificar si archivo existe
        boolean fileExists = false;
        try{
            @SuppressWarnings("unchecked")
            Vector<ChannelSftp.LsEntry> files = channel.ls(remoteFilePath);
            for (ChannelSftp.LsEntry entry : files) {
                if(entry.getFilename().equals(newFileName)){
                    fileExists = true;
                    break;
                }
            }
        }catch(SftpException e){e.printStackTrace();}

        if(!fileExists){
            //Cargar al directorio remoto y renombrar
            try(FileInputStream fis = new FileInputStream(localFile)){
                channel.put(fis, localFile.getName());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String originalFilename = localFile.getName();
            channel.rename(originalFilename,newFileName);
            System.out.println("File "+newFileName+" uploaded to " + remoteFilePath);
        }

    }
    public List<String> localFiles(String directoryPath){
        List<String> fileNames = new ArrayList<>();
        File directory = new File(directoryPath);
        if(directory.exists() && directory.isDirectory()){
            //Obtener la lista de archivos y directorios en el directorio
            File[] files = directory.listFiles();
            //Si no está vacío
            if(files != null){
                for(File file : files){
                    if(file.isFile()){
                        fileNames.add(file.getName());
                    }
                }
            }else {System.out.println("El directorio está vacío.");}
        }else{System.out.println("El directorio no existe o no es un directorio.");}
        return fileNames;
    }

    public String newFileName(String input){
        StringBuilder result = new StringBuilder();
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);
        while(matcher.find()){
            result.append(matcher.group());
        }
        String resultFinal = result.toString()+".jpg";
        return resultFinal;
    }

    public Map<Long,String> filesMap(List<String> fileNamesNoProcessed){
        Map<Long,String> filesMap = new HashMap<>();
        Pattern pattern = Pattern.compile("\\d+");
        if(fileNamesNoProcessed == null || fileNamesNoProcessed.isEmpty()){
            return filesMap;
        }
        for(String fileName : fileNamesNoProcessed){
            StringBuilder result = new StringBuilder();
            Matcher matcher = pattern.matcher(fileName);
            if(matcher.find()){
                result.append(matcher.group());
            }
            Long plu_nbr = Long.parseLong(result.toString());
            filesMap.put(plu_nbr,fileName);
        }
        return filesMap;
    }
}
