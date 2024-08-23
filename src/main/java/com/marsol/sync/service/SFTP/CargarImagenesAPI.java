package com.marsol.sync.service.SFTP;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.marsol.sync.app.ConfigLoader;
import com.marsol.sync.model.Layout;
import com.marsol.sync.service.api.LayoutService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CargarImagenesAPI {
    private ConfigLoader configLoader;
    private LayoutService layoutService;
    private ClientSFTP clientSFTP;

    public CargarImagenesAPI() {
        configLoader = new ConfigLoader();
    }
    public void setLayoutService(LayoutService layoutService) {
        this.layoutService = layoutService;
    }
    public void setClientSFTP(String username, String password, String host, int port){
        clientSFTP = new ClientSFTP(username, password, host, port);
    }
    public List<Layout> getLayoutAPI(int storeNbr, int deptNbr) throws Exception {
        List<Layout> layouts = new ArrayList<>();
        if(deptNbr == 98 || deptNbr == 94){
            switch (deptNbr){
                case 98: //Panaderia
                    List<Layout> layoutsPanaderia = layoutService.getLayoutsPanaderia(storeNbr);
                    layouts.addAll(layoutsPanaderia);
                case 94: //Vegetales
                    List<Layout> layoutVegetales = layoutService.getLayoutsVegetales(storeNbr);
                    layouts.addAll(layoutVegetales);
            }
        } else {
            throw new Exception("El departamento ingresado no es válido.");
        }
        return layouts;
    }

    public void cargarImagen(List<Layout> layouts) throws JSchException, IOException, SftpException {
        clientSFTP.connect();
        String localFilePath = configLoader.getProperty("local_file_path");
        String remoteFilePath = configLoader.getProperty("remote_file_path");

        for (Layout layout : layouts) {
            String plu_nbr = String.valueOf(layout.getPlu());
            String imagen  = layout.getImagen();
            String newFileName = plu_nbr+".jpg";
            if(fileExist(localFilePath+imagen)){
                System.out.println("FileExist: "+localFilePath+imagen);
                clientSFTP.uploadAndRenameFile(localFilePath+imagen,remoteFilePath,newFileName);
            }else{System.out.println("FileNotExist: "+localFilePath+imagen);}
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
