package com.example.service;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import com.example.model.User;
import java.util.Properties;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileOutputStream;
import java.io.File;

public class UserService {

    private static Properties properties = new Properties();

    private static List<User> users = new ArrayList<>();

    private static int userIdCounter = 0;

    private static final String RUTA_FILE_PATH = "D:\\Users\\PCSHOP-COL\\Desktop\\proyectofinalp3\\src\\main\\resources\\com\\example\\RutaDB.properties";

    private static final String LOG_FILE_PATH = "D:\\Users\\PCSHOP-COL\\Desktop\\proyectofinalp3\\src\\main\\java\\com\\example\\persistance\\log\\VirtualWallet_Log.txt";

    private static final String FILES_FILE_PATH = "D:\\Users\\PCSHOP-COL\\Desktop\\proyectofinalp3\\src\\main\\java\\com\\example\\persistance";

    private static final String TXT_FILE_PATH = "D:\\Users\\PCSHOP-COL\\Desktop\\proyectofinalp3\\src\\main\\java\\com\\example\\persistance\\files";

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
                        int storedId = Integer.parseInt(data[0]);
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

    public static void serializeToXML(User user, int userId) {
        String xmlFilePath = FILES_FILE_PATH + "\\user_" + userId + ".xml";
        try (XMLEncoder encoder = new XMLEncoder(new FileOutputStream(xmlFilePath))) {
            encoder.writeObject(user);
            encoder.flush();
            System.out.println("Usuario serializado en formato XML: user_" + userId + ".xml");
            copyXMLFile(userId);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al serializar el usuario en xml");
        }
    }
    

    public static void serializeToBinary(User user){
        String filepath = "files/user_" + user.getUserId() + ".bin";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILES_FILE_PATH + "\\user_" + user.getUserId() + ".bin"))){
            oos.writeObject(user);
            System.out.println("Usuario serializado en binario: " + filepath);
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
            System.out.println("Usuario serializado en TXT: " + data);
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
            System.out.println("Usuario deserializado desde formato XML: " + filePath);
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
                    System.out.println("Usuario deserializado desde TXT: " + line);
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
        System.out.println("XML file copied to backup folder: " + destinationFile);
    }
    
}