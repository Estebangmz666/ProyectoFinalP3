package com.example.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {

    private static Properties properties = new Properties();

    private static final String RUTA_FILE_PATH = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "com" + File.separator + "example" + File.separator + "config.properties";

    public static void loadProperties(){
        try (FileInputStream in = new FileInputStream(RUTA_FILE_PATH)){
            properties.load(in);
        } catch (IOException e){
            System.err.println("Error al cargar propiedades: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getRutaFromProperties(String key) {
        String ruta = properties.getProperty(key);
        if (ruta == null || ruta.isEmpty()) {
            System.err.println("La ruta para " + key + " no está definida.");
        }
        return ruta;
    }    
}