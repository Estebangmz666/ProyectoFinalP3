package com.example.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.example.model.User;

import java.util.ArrayList;

public class UserService {

    private static List<User> users = new ArrayList<>();

    private static int userIdCounter = 0;

    private static final String FILE_PATH = "src\\main\\resources\\com\\example\\Users.txt";
    
    public static boolean isValidUser(String username, String password){
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String storedUsername = parts[0].trim();
                    String storedPassword = parts[1].trim();
                    if (storedUsername.equals(username) && storedPassword.equals(password)) {
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
        try(FileWriter txtUtil = new FileWriter(FILE_PATH, true)){
            txtUtil.write(username + "," + password + "\n");
       } catch (IOException e){
            System.out.println("Error: " + e.getMessage());
       }
    }

    public static boolean verifyPassword(String password, String confirmedPassword){
        return password.equals(confirmedPassword);
    }

    public static boolean userAlreadyExists(String email){
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
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

    public static void addUser(String name, String email, String direction, String cellphone){
        userIdCounter ++;
        int userId = userIdCounter;
        User newUser = new User(userId, name, email, direction, cellphone, null);
        users.add(newUser);
    }
}
