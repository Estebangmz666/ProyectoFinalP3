package com.example.service;

import com.example.sockets.ClientSocket;
import com.example.util.PropertiesLoader;
import com.example.exception.UserAlreadyExistsException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AuthService {
    
    private static ClientSocket clientSocket;
    
    public static void initializeClientSocket() {
        if (clientSocket == null) {
            clientSocket = new ClientSocket("localhost", 12345);
        }
    }

    public static void sendAccessGrantedMessage(String userType) {
        if (clientSocket != null) {
            clientSocket.sendMessage("Acceso concedido: " + userType);
        } else {
            System.err.println("Error: clientSocket no está inicializado.");
        }
    }

    public static boolean isValidEmail(String email, String password){
        String ruta = PropertiesLoader.getRutaFromProperties("users_path");

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
                        sendAccessGrantedMessage("Usuario");
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
        try (BufferedReader br = new BufferedReader(new FileReader(PropertiesLoader.getRutaFromProperties("users_path")))) {
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
            sendAccessGrantedMessage("Administrador");
            return true;
        } 
        return false;
    }
    
    public static void closeClientSocket() {
        if (clientSocket != null) {
            clientSocket.close();
        }
    }
}
