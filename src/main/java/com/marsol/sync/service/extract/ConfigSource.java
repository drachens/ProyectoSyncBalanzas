package com.marsol.sync.service.extract;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class ConfigSource {

    public void apiSource(String url, String puerto){
        Properties prop = new Properties();
        try(OutputStream os = new FileOutputStream("config.properties")){
            prop.setProperty("urlBase",url+puerto);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void csvSource(String filepath){

    }
    public void jsonSource(String filepath){

    }
    public void xmlSource(String filepath){

    }
    public void plainTextSource(String filepath){

    }
    public void dbSource(){

    }
}
