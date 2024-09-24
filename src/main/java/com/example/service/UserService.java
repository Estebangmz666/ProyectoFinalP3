package com.example.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.example.model.User;

import java.util.ArrayList;

public class UserService {

    private static List<User> users = new ArrayList<>();

    private static int userIdCounter = 0;

    private static final String RUTA_FILE_PATH = "D:\\Users\\PCSHOP-COL\\Desktop\\proyectofinalp3\\src\\main\\resources\\com\\example\\RutaDB.properties";

    public static String getRuta() {
        String ruta = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ruta=")) {
                    ruta = line.split("=")[1];
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
        return ruta;
    }
    
    public static boolean isValidEmail(String email, String password){
        try (BufferedReader br = new BufferedReader(new FileReader(getRuta()))) {
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
        return email.contains("@gmail.com") || email.contains("@email.com");
    }
}