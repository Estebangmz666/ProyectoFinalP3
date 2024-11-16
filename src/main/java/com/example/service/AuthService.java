package com.example.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.example.exception.UserAlreadyExistsException;

public class AuthService {
    
    public static boolean isValidEmail(String email, String password){
        String ruta = UserService.getUsersPath();

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

    public static boolean emailAlreadyExists(String email) throws UserAlreadyExistsException {
        try (BufferedReader br = new BufferedReader(new FileReader(UserService.getUsersPath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(email)) {
                    throw new UserAlreadyExistsException("El correo electrónico ya está en uso!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; 
    }

    public static boolean verifyPassword(String password, String confirmedPassword){
        return password.equals(confirmedPassword);
    }

    public static boolean verifyCellphone(String cellphone) {
        return cellphone != null && cellphone.matches("\\d+");
    }
    
    public static boolean verifyEmailDomain(String email) {
        String[] validDomains = {
            "@gmail.com", "@yahoo.com", "@hotmail.com",
            "@outlook.com", "@live.com", "@icloud.com",
            "@movistar.com.co", "@clarotv.co", "@une.net.co",
            "@tigo.com.co", "@netcol.net.co", "@rapipago.com",
            "@pse.com.co", "uqvirtual.edu.co"
        };
        for (String domain : validDomains) {
            if (email.endsWith(domain)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAdmin(String email, String password) {
        if ("root".equals(email) && "root".equals(password)){
            return true;
        } else {
            return false;
        }  
    }
}