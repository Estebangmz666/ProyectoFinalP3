package com.example.service;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import com.example.model.User;
import java.util.Properties;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class UserService {

    private static Properties properties = new Properties();

    private static List<User> users = new ArrayList<>();

    private static int userIdCounter = 0;

    private static final String RUTA_FILE_PATH = "D:\\Users\\PCSHOP-COL\\Desktop\\proyectofinalp3\\src\\main\\resources\\com\\example\\RutaDB.properties";

    private static final String LOG_FILE_PATH = "D:\\Users\\PCSHOP-COL\\Desktop\\proyectofinalp3\\src\\main\\java\\com\\example\\persistance\\log\\VirtualWallet_Log.txt";

    public static void logToFile(String level, String message){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))){
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write(String.format("%s [%s]: %s%n", timestamp, level, message));
        } catch (IOException e){
            System.out.println("Error al escribir en el log" + e.getMessage());
        }
    }

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
            "@pse.com.co", "uqvirtual.edu.co"
        };
        for (String domain : validDomains) {
            if (email.endsWith(domain)) {
                return true;
            }
        }
        return false;
    }
    
    
    public static User searchById(int id) {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/com/example/UserInfo.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 5) {
                    try {
                        int storedId = Integer.parseInt(data[0]);
                        if (storedId == id) {
                            User user = new User(
                                storedId,              // ID
                                data[1],               // Nombre
                                data[2],               // Email
                                data[3],               // Dirección
                                data[4],               // Teléfono
                                new ArrayList<>()      // Inicializar lista de cuentas vacía
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

    public static boolean isAdmin(String email, String password) {
        if ("admin@uqvirtual.edu.co".equals(email) && "123".equals(password)){
            return true;
        } else {
            return false;
        }
        
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

        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/com/example/UserInfo.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (Integer.parseInt(userInfo[0]) == id) {
                    line = id + "," + name + "," + email + "," + direction + "," + cellphone;
                    userFound = true;
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if (userFound) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/com/example/UserInfo.txt"))) {
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



}