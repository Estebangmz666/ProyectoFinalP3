package com.example.service;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.example.exception.*;
import com.example.model.Account;
import com.example.model.AccountType;
import com.example.model.User;

public class UserService {

    private static Properties properties = new Properties();

    private static List<User> users = new ArrayList<>();

    private static int userIdCounter = 0;

    private static User currentUser;

    private static final String RUTA_FILE_PATH = "src\\main\\resources\\com\\example\\RutaDB.properties";

    private static final String LOG_FILE_PATH = "src\\main\\java\\com\\example\\persistance\\log\\VirtualWallet_Log.log";

    private static final String FILES_FILE_PATH = "src\\main\\java\\com\\example\\persistance";

    private static final String TXT_FILE_PATH = "src\\main\\java\\com\\example\\persistance\\files";

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

    public static String getBasePath() {
        String ruta = properties.getProperty("base_path");
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

    public static boolean emailAlreadyExists(String email) throws UserAlreadyExistsException {
        try (BufferedReader br = new BufferedReader(new FileReader(getRuta()))) {
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

    public static void addUser(String name, String email, String password, String direction, String cellphone) {
        userIdCounter++;
        int userId = userIdCounter;
        User newUser = new User(userId, name, email, direction, cellphone, null);
        users.add(newUser);
        serializeToXML(newUser, userId);
        serializeToBinary(newUser);
        serializeToText(newUser);
        try (FileWriter writer = new FileWriter(getRuta(), true)) {
            writer.write(email + "," + password + "\n");
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
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
    
    public static User searchById(int id) {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/example/persistance/files/user_" + id + ".txt"))) {
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
        
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/example/persistance/files/user_" + id + ".txt"))) {
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
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/java/com/example/persistance/files/user_" + id + ".txt"))) {
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
    

    public static void serializeToXML(User user, int userId) {
        String xmlFilePath = FILES_FILE_PATH + "\\user_" + userId + ".xml";
        try (XMLEncoder encoder = new XMLEncoder(new FileOutputStream(xmlFilePath))) {
            encoder.writeObject(user);
            encoder.flush();
            copyXMLFile(userId);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al serializar el usuario en xml");
        }
    }
    

    public static void serializeToBinary(User user){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILES_FILE_PATH + "\\user_" + user.getUserId() + ".bin"))){
            oos.writeObject(user);
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("Error al serializar usuario en binario");
        }
    }

    public static void serializeToText(User user) {
        File directory = new File(TXT_FILE_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = TXT_FILE_PATH + "/user_" + user.getUserId() + ".txt";        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            String data = String.format("%s@@%s@@%s@@%s@@%s%n", 
                user.getUserId(),
                user.getName(), 
                user.getEmail(), 
                user.getCellphone(), 
                user.getDirection());
            bw.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al serializar usuario.");
        }
    }
    

    public static User deserializeFromBinary(int userId){
        String filepath = "files/user_" + userId + ".bin";    
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filepath))){
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            System.out.println("Error al deserializar objeto bin.");
        }
        return null;
    }

     public static User deserializeFromXML(int userId) {
        String filePath = "files/user_" + userId + ".xml";
        User user = null;
        try (XMLDecoder decoder = new XMLDecoder(new FileInputStream(filePath))) {
            user = (User) decoder.readObject();
        } catch (IOException e) {
            System.out.println("Error al deserializar el objeto desde XML: " + e.getMessage());
        }
        return user;
    }

    public static User deserializeFromText(int userId) {
        String filePath = FILES_FILE_PATH + "/user_" + userId + ".txt";
        User user = null;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            if ((line = br.readLine()) != null) {
                String[] parts = line.split("@@");
                if (parts.length == 4) {
                    String name = parts[0].trim();
                    String email = parts[1].trim();
                    String cellphone = parts[2].trim();
                    String direction = parts[3].trim();
                    user = new User(userId, name, email, direction, cellphone, new ArrayList<>());
                } else {
                    System.out.println("Formato incorrecto en la línea: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al deserializar usuario desde TXT: " + e.getMessage());
        }
        return user;
    }

    public static void copyXMLFile(int userId) throws IOException {
        String sourceFile = FILES_FILE_PATH + "\\user_" + userId + ".xml";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy_HH-mm-ss");
        String timestamp = LocalDateTime.now().format(formatter);
        String timestampedName = "user_" + userId + "_" + timestamp + ".xml";
        String backupFolder = FILES_FILE_PATH + "\\backup";
        File folder = new File(backupFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String destinationFile = backupFolder + "\\" + timestampedName;
        Files.copy(Paths.get(sourceFile), Paths.get(destinationFile), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void updateUserXML(int id, String name, String email, String direction, String cellphone) {
        String xmlFilePath = "src/main/java/com/example/persistance/user_" + id + ".xml";
        
        try (XMLDecoder decoder = new XMLDecoder(new FileInputStream(xmlFilePath))) {
            User user = (User) decoder.readObject();
            
            user.setName(name);
            user.setEmail(email);
            user.setDirection(direction);
            user.setCellphone(cellphone);
            
            try (XMLEncoder encoder = new XMLEncoder(new FileOutputStream(xmlFilePath))) {
                encoder.writeObject(user);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error al guardar el archivo XML");
            }
            try {
                copyXMLFile(id);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error al copiar el archivo XML");
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Archivo XML no encontrado: " + xmlFilePath);
        }
    }
    
    public static void updateEmailInUsersFile(String oldEmail, String newEmail) {
        String filePath = "src/main/resources/com/example/Users.txt";
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
    
    public static User getCurrentUser(){
        return currentUser;
    }

    public static void setCurrentUser(User user){
        currentUser = user;
    }

    public static User searchByIdAndSetCurrentUser(int id) {
    String filePath = "src\\main\\java\\com\\example\\persistance\\files\\user_" + id + ".txt";
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
        File folder = new File("src\\main\\java\\com\\example\\persistance\\files");
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

    public static User deserializeUserFromFile(File userFile) {
        User user = new User();
        
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    String[] userInfo = line.split("@@");
                    
                    user.setUserId(Integer.parseInt(userInfo[0].trim())); // ID del usuario
                    user.setName(userInfo[1].trim());                      // Nombre
                    user.setEmail(userInfo[2].trim());                     // Email
                    user.setCellphone(userInfo[3].trim());                 // Teléfono
                    user.setDirection(userInfo[4].trim());                 // Dirección
                    
                    isFirstLine = false;
                } else {
                    String[] accountInfo = line.split("@@");
                    
                    if (accountInfo.length >= 4) {
                        Account account = new Account(
                            Integer.parseInt(accountInfo[0].trim()),       // ID de la cuenta
                            accountInfo[1].trim(),                         // Número de cuenta
                            AccountType.valueOf(accountInfo[2].trim().toUpperCase()), // Tipo de cuenta
                            new BigDecimal(accountInfo[3].trim()),         // Saldo de la cuenta
                            user                                          // Usuario propietario
                        );
                        user.addAccount(account);
                    } else {
                        System.out.println("Formato incorrecto en la línea: " + line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al deserializar el usuario desde el archivo: " + userFile.getName());
        }
        
        return user;
    }


    public static User getUserByAccount(Account account) {
        File folder = new File(getBasePath());
        
        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                User user = deserializeUserFromFile(file);
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