package com.example.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Account;
import com.example.model.User;
import com.example.service.UserService;
import com.example.util.SerializeDeserialize;

public class UserDAO {

    private static List<User> users = new ArrayList<>();

    private static int userIdCounter = 0;

    public static void addUser(String name, String email, String password, String direction, String cellphone) {
        userIdCounter++;
        int userId = userIdCounter;
        User newUser = new User(userId, name, email, direction, cellphone, null);
        users.add(newUser);
        SerializeDeserialize.serializeToXML(newUser, userId);
        SerializeDeserialize.serializeToBinary(newUser);
        SerializeDeserialize.serializeToText(newUser);
        try (FileWriter writer = new FileWriter(UserService.getUsersPath(), true)) {
            writer.write(email + "," + password + "\n");
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    public static User searchById(int id) {
        try (BufferedReader br = new BufferedReader(new FileReader(UserService.getBasePath() + "\\user_" + id + ".txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("@@");
                if (data.length >= 5) {
                    try {
                        int storedId = Integer.parseInt(data[0].trim());
                        if (storedId == id) {
                            User user = new User(
                                storedId,              // ID
                                data[1],               // Nombre
                                data[2],               // Email
                                data[3],               // Telefono
                                data[4],               // Direccion
                                new ArrayList<>()      // Lista de cuentas vacía
                            );
                            return user; 
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error al convertir el ID a un número: " + data[0]);
                    }
                } else {
                    System.out.println("Línea incorrecta o incompleta en el archivo: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al leer el archivo.");
        }
        return null;
    }

    public static boolean deleteUserById(int id) {
        User userToDelete = searchById(id);
        if (userToDelete != null) {
            users.remove(userToDelete);
            return true;
        }
        return false;
    }    

    public static boolean updateUser(int id, String name, String email, String direction, String cellphone) {
        List<String> lines = new ArrayList<>();
        boolean userFound = false;
        
        try (BufferedReader br = new BufferedReader(new FileReader(UserService.getBasePath() + "\\user_" + id + ".txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userInfo = line.split("@@");
                if (Integer.parseInt(userInfo[0].trim()) == id) {
                    line = id + "@@" + name + "@@" + email + "@@" + cellphone + "@@" + direction;
                    userFound = true;
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if (userFound) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(UserService.getBasePath() + "\\user_" + id + ".txt"))) {
                for (String updatedLine : lines) {
                    bw.write(updatedLine);
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

    public static void updateEmailInUsersFile(String oldEmail, String newEmail) {
        String filePath = UserService.getUsersPath();
        List<String> lines = new ArrayList<>();
        boolean emailFound = false;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo[0].trim().equals(oldEmail)) {
                    line = newEmail + "," + userInfo[1];
                    emailFound = true;
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }    
        if (emailFound) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
                for (String updatedLine : lines) {
                    bw.write(updatedLine);
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Correo no encontrado en Users.txt");searchById(userIdCounter);
        }
    }

    public static User getUserByAccount(Account account) {
        File folder = new File(UserService.getBasePath());
        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                User user = SerializeDeserialize.deserializeUserFromFile(file);
                for (Account acc : user.getAccounts()) {
                    if (acc.getAccountNumber().equals(account.getAccountNumber())) {
                        return user;
                    }
                }
            }
        }
        return null;
    }
}