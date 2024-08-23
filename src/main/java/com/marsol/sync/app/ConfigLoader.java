package com.marsol.sync.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Properties;

@Component
public class ConfigLoader {
    private final Properties props = new Properties();
    private static final String FILE_NAME = "config.properties";

    public ConfigLoader() {
        loadProperties();
    }

    public void loadProperties(){
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(FILE_NAME)) {
            if(in == null) {
                System.out.println("File not found");
                return;
            }
            props.load(in);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }

    public int getIntProperty(String key) {
        return Integer.parseInt(props.getProperty(key));
    }

    public void setProperty(String key, String value) {
        props.setProperty(key, value);
    }
    public void saveProperty() {
        try (OutputStream out = new FileOutputStream(FILE_NAME)) {
            props.store(out, null);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reload(){
        loadProperties();
    }
}
