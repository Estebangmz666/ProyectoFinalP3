package com.example.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import com.example.model.User;

public class UserService {

    private static Properties properties = new Properties();

    private static User currentUser;

    private static final String RUTA_FILE_PATH = "src\\main\\resources\\com\\example\\RutaDB.properties";

    public static User getCurrentUser(){
        return currentUser;
    }

    public static void setCurrentUser(User user){
        currentUser = user;
    }
    
    public static void loadProperties(){
        try (FileInputStream in = new FileInputStream(RUTA_FILE_PATH)){
            properties.load(in);
        } catch (IOException e){
            System.err.println("Error al cargar propiedades: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getUsersPath() {
        String ruta = properties.getProperty("users_path");
        if (ruta == null || ruta.isEmpty()){
            System.err.println("La ruta no esta definida");
        }
        return ruta;
    }

    public static String getBasePath() {
        String ruta = properties.getProperty("base_path");
        if (ruta == null || ruta.isEmpty()){
            System.err.println("La ruta no esta definida");
        }
        return ruta;
    }

    public static String getLogPath() {
        String ruta = properties.getProperty("log_path");
        if (ruta == null || ruta.isEmpty()){
            System.err.println("La ruta no esta definida");
        }
        return ruta;
    }

    public static String getPersistancePath() {
        String ruta = properties.getProperty("persistance_path");
        if (ruta == null || ruta.isEmpty()){
            System.err.println("La ruta no esta definida");
        }
        return ruta;
    }

    public static String getTxtPath() {
        String ruta = properties.getProperty("txt_path");
        if (ruta == null || ruta.isEmpty()){
            System.err.println("La ruta no esta definida");
        }
        return ruta;
    }

    public static String getBackupPath() {
        String ruta = properties.getProperty("backup_path");
        if (ruta == null || ruta.isEmpty()){
            System.err.println("La ruta no esta definida");
        }
        return ruta;
    }  

    public static User searchByIdAndSetCurrentUser(int id) {
    String filePath = getBasePath() + "\\user_" + id + ".txt";
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split("@@");
            if (data.length >= 5) {
                int storedId = Integer.parseInt(data[0].trim());
                if (storedId == id) {
                    User user = new User(
                        Integer.parseInt(data[0]), // ID
                        data[1],                   // Nombre
                        data[2],                   // Email
                        data[4],                   // Dirección
                        data[3],                   // Teléfono
                        new ArrayList<>()          // Inicializa lista de cuentas vacía
                    );
                    setCurrentUser(user);
                    return user;
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
        System.out.println("Error al leer el archivo.");
    } 
    return null;
    }

    public static User searchByEmailAndSetCurrentUser(String email) {
        File folder = new File(getTxtPath());
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().startsWith("user_") && file.getName().endsWith(".txt")) {
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] data = line.split("@@");
                            if (data.length >= 5) {
                                String storedEmail = data[2].trim();
                                if (storedEmail.equals(email)) {
                                    int id = Integer.parseInt(data[0].trim());
                                    return searchByIdAndSetCurrentUser(id);
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error al leer el archivo: " + file.getName());
                    }
                }
            }
        } else {
            System.out.println("No se encontraron archivos en la carpeta.");
        }
        System.out.println("No se encontró ningún usuario con el correo proporcionado.");
        return null;
    }
}