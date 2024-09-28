package com.example.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import com.example.model.User;
import java.util.Properties;
import java.io.FileInputStream;

import java.util.ArrayList;

public class UserService {

    private static Properties properties = new Properties();

    private static List<User> users = new ArrayList<>();

    private static int userIdCounter = 0;

    private static final String RUTA_FILE_PATH = "C:\\Users\\jvill\\OneDrive\\Escritorio\\UNI\\ProyectoFinalProgramacion3\\ProyectoFinalP3\\src\\main\\resources\\com\\example\\RutaDB.properties";

    public static void loadProperties(){
        try (FileInputStream in = new FileInputStream(RUTA_FILE_PATH)){
            properties.load(in);
            System.out.println("Propiedades Cargadas Correctamente");
            System.out.println("Ruta de la DB: " + getRuta());
        } catch (IOException e){
            System.err.println("Error al cargar propiedades: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getRuta() {
        String ruta = properties.getProperty("ruta");
        if (ruta == null || ruta.isEmpty()){
            System.err.println("La ruta no esta definida");
        }
        return ruta;
    }
    
    public static boolean isValidEmail(String email, String password){
        String ruta = getRuta();

        if (ruta == null || ruta.isEmpty()){
            System.err.println("No se puede validar el email");
            return false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String storedEmail = parts[0].trim();
                    String storedPassword = parts[1].trim();
                    if (storedEmail.equals(email) && storedPassword.equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer los archivos");
            e.printStackTrace();
        }
        return false;
    }

    public static void textUtil(String username, String password) {
        try(FileWriter txtUtil = new FileWriter(getRuta(), true)){
            txtUtil.write(username + "," + password + "\n");
       } catch (IOException e){
            System.out.println("Error: " + e.getMessage());
       }
    }

    public static boolean verifyPassword(String password, String confirmedPassword){
        return password.equals(confirmedPassword);
    }

    public static boolean emailAlreadyExists(String email){
        try (BufferedReader br = new BufferedReader(new FileReader(getRuta()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(email)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; 
    }

    public static void addUser(String name, String email, String password, String direction, String cellphone) {
        userIdCounter++;
        int userId = userIdCounter;
        User newUser = new User(userId, name, email, direction, cellphone, null);
        users.add(newUser);
        try (FileWriter writer = new FileWriter(getRuta(), true)) {
            writer.write(email + "," + password + "\n");
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }   

    public static boolean verifyCellphone(String cellphone) {
        try {
            Integer.parseInt(cellphone);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean verifyEmailDomain(String email) {
        String[] validDomains = {
            "@gmail.com", "@yahoo.com", "@hotmail.com",
            "@outlook.com", "@live.com", "@icloud.com",
            "@movistar.com.co", "@clarotv.co", "@une.net.co",
            "@tigo.com.co", "@netcol.net.co", "@rapipago.com",
            "@pse.com.co"
        };
        for (String domain : validDomains) {
            if (email.endsWith(domain)) {
                return true;
            }
        }
        return false;
    }
    
    
}