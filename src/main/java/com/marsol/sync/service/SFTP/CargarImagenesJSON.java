/*
package com.marsol.sync.service.SFTP;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.marsol.sync.app.ConfigLoader;
import com.marsol.sync.model.Layout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CargarImagenesJSON {
    private ConfigLoader configLoader;
    private ClientSFTP clientSFTP;

    public CargarImagenesJSON() {
        configLoader = new ConfigLoader();
    }
    public void setClientSFTP(String username, String password, String host, int port){
        clientSFTP = new ClientSFTP(username, password, host, port);
    }
    public List<Layout> getLayoutJSON(int storeNbr, int deptNbr){
        List<Layout> layouts = new ArrayList<>();
        String pathBase = configLoader.getProperty("ruteJsons");
        String fileName = "json_flashkeys_"+deptNbr+"_"+storeNbr;
        String pathComplete = pathBase + "\\" + "jsons_" + storeNbr + "\\"+fileName+"\\"+fileName;
        Gson gson = new Gson();
        try(FileReader reader = new FileReader(pathComplete)){
            Type layoutsListType = new TypeToken<List<Layout>>(){}.getType();
            List<Layout> layoutS = gson.fromJson(reader, layoutsListType);
            layouts.addAll(layoutS);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return layouts;
    }

    public void cargarImagen(List<Layout> layouts) throws JSchException, IOException, SftpException {
        clientSFTP.connect();
        String localFilePath = configLoader.getProperty("local_file_path");
        String remoteFilePath = configLoader.getProperty("remote_file_path");

        for(Layout layout : layouts){
            String plu_nbr = String.valueOf(layout.getPlu());
            String imagen  = layout.getImagen();
            String newFileName = plu_nbr+".jpg";
            if(fileExist(localFilePath+imagen)){
                //System.out.println("FileExist: "+localFilePath+imagen);
                clientSFTP.uploadAndRenameFile(localFilePath+imagen,remoteFilePath,newFileName);
            }
        }
        clientSFTP.disconnect();
    }

    public boolean fileExist(String path){
        File file = new File(path);
        if(file.exists() && file.isFile()){
            return true;
        } else {
            return false;
        }
    }
}


 */