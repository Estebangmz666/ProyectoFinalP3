package com.example.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.model.User;
import com.example.util.PropertiesLoader;

public class UserService {

    private static User currentUser;


    public static User getCurrentUser(){
        return currentUser;
    }

    public static void setCurrentUser(User user){
        currentUser = user;
    }

    public static User searchByIdAndSetCurrentUser(int id) {
    String filePath = PropertiesLoader.getRutaFromProperties("base_path");
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
        File folder = new File(PropertiesLoader.getRutaFromProperties("txt_path"));
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

    public static List<User> loadUsers(String directoryPath) {
        List<User> users = new ArrayList<>();
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("El directorio no existe.");
            return users;
        }
        File[] userFiles = directory.listFiles((dir, name) -> name.startsWith("user_") && name.endsWith(".txt"));
        if (userFiles != null) {
            for (File file : userFiles) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line = br.readLine();
                    if (line != null) {
                        String[] data = line.split("@@");
                        if (data.length == 5) {
                            User user = new User(
                                Integer.parseInt(data[0].trim()), // userId
                                data[1].trim(),                   // name
                                data[2].trim(),                   // email
                                data[4].trim(),                   // direction
                                data[3].trim(),                   // cellphone
                                new ArrayList<>()                 // accounts
                            );
                            users.add(user);
                        } else {
                            System.out.println("Formato incorrecto " + file.getName());
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error intentando leer el archivo " + file.getName() + " - " + e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("Error converting user ID in file: " + file.getName() + " - " + e.getMessage());
                }
            }
        }
        if(users.size() > 0){
            User.setCounterId(users.get(users.size()-1).getUserId());   
        }else{
            User.setCounterId(0);
        }
        return users;
    }
}