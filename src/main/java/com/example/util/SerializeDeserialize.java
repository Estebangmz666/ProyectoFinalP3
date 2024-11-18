package com.example.util;

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

import com.example.model.Account;
import com.example.model.AccountType;
import com.example.model.Budget;
import com.example.model.User;

public class SerializeDeserialize {

    public static void textUtil(String username, String password) {
        try(FileWriter txtUtil = new FileWriter(PropertiesLoader.getRutaFromProperties("users_path"), true)){
            txtUtil.write(username + "," + password + "\n");
       } catch (IOException e){
            System.out.println("Error: " + e.getMessage());
       }
    }

    public static void serializeToXML(User user, int userId) {
        String xmlFilePath = PropertiesLoader.getRutaFromProperties("txt_path") + "\\user_" + userId + ".xml";
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
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PropertiesLoader.getRutaFromProperties("txt_path") + "\\user_" + user.getUserId() + ".bin"))){
            oos.writeObject(user);
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("Error al serializar usuario en binario");
        }
    }

    public static void serializeToText(User user) {
        File directory = new File(PropertiesLoader.getRutaFromProperties("txt_path"));
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = PropertiesLoader.getRutaFromProperties("txt_path") + "\\user_" + user.getUserId() + ".txt";        
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
        String filepath = "files\\user_" + userId + ".bin";    
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filepath))){
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            System.out.println("Error al deserializar objeto bin.");
        }
        return null;
    }

     public static User deserializeFromXML(int userId) {
        String filePath = "files\\user_" + userId + ".xml";
        User user = null;
        try (XMLDecoder decoder = new XMLDecoder(new FileInputStream(filePath))) {
            user = (User) decoder.readObject();
        } catch (IOException e) {
            System.out.println("Error al deserializar el objeto desde XML: " + e.getMessage());
        }
        return user;
    }

    public static User deserializeFromText(int userId) {
        String filePath = PropertiesLoader.getRutaFromProperties("txt_path") + "\\user_" + userId + ".txt";
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
        String sourceFile = PropertiesLoader.getRutaFromProperties("txt_path") + "\\user_" + userId + ".xml";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy_HH-mm-ss");
        String timestamp = LocalDateTime.now().format(formatter);
        String timestampedName = "user_" + userId + "_" + timestamp + ".xml";
        String backupFolder = PropertiesLoader.getRutaFromProperties("backup_path");
        File folder = new File(backupFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String destinationFile = backupFolder + "\\" + timestampedName;
        Files.copy(Paths.get(sourceFile), Paths.get(destinationFile), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void updateUserXML(int id, String name, String email, String direction, String cellphone) {
        String xmlFilePath = PropertiesLoader.getRutaFromProperties("persistance_path") + "user_" + id + ".xml"; 
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

    public static User deserializeUserFromFile(File userFile) {
        User user = new User();
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            boolean isFirstLine = true;          
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    String[] userInfo = line.split("@@");
                    user.setUserId(Integer.parseInt(userInfo[0].trim()));  // ID del usuario
                    user.setName(userInfo[1].trim());                      // Nombre
                    user.setEmail(userInfo[2].trim());                     // Email
                    user.setCellphone(userInfo[3].trim());                 // Teléfono
                    user.setDirection(userInfo[4].trim());                 // Dirección
                    isFirstLine = false;
                } else {
                    String[] accountInfo = line.split("@@");
                    
                    if (accountInfo.length >= 4) {
                        Account account = new Account(
                            Integer.parseInt(accountInfo[0].trim()),                  // ID de la cuenta
                            accountInfo[1].trim(),                                    // Número de cuenta
                            AccountType.valueOf(accountInfo[2].trim().toUpperCase()), // Tipo de cuenta
                            new BigDecimal(accountInfo[3].trim()),                    // Saldo de la cuenta
                            user                                                      // Usuario propietario
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

    public static void saveBudget(int userId, Budget budget) {
        String fileName = "User" + userId + "_budgets.txt";
        File file = new File(PropertiesLoader.getRutaFromProperties("budget_base_path") + fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(budget.getBudgetId() + "@@");
            writer.write(budget.getName() + "@@");
            writer.write(budget.getTotalAmount() + "@@");
            writer.write(budget.getSpentAmount() + "@@");
            writer.write(budget.getCategory() + "@@");
            writer.newLine();
            System.out.println("Presupuesto guardado correctamente en: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al guardar el presupuesto: " + e.getMessage());
        }
    }

    public static List<Budget> loadBudgets(int userId) {
        String fileName = "User" + userId + "_budgets.txt";
        File file = new File(PropertiesLoader.getRutaFromProperties("budget_base_path") + fileName);
        List<Budget> budgets = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] attributes = line.split("@@");
                String budgetId = attributes[0];
                String name = attributes[1];
                double totalAmount = Double.parseDouble(attributes[2]);
                double spentAmount = Double.parseDouble(attributes[3]);
                String category = attributes[4];

                Budget budget = new Budget(budgetId, name, totalAmount, spentAmount, category);
                budgets.add(budget);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar los presupuestos: " + e.getMessage());
        }

        return budgets;
    }

    public static void deleteBudget(int userId, String budgetId) {
        String fileName = "User" + userId + "_budgets.txt";
        File file = new File(PropertiesLoader.getRutaFromProperties("budget_base_path") + fileName);
        File tempFile = new File(PropertiesLoader.getRutaFromProperties("budget_base_path") + "temp_" + fileName);
    
        try (BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            boolean budgetFound = false;
            while ((line = reader.readLine()) != null) {
                String[] attributes = line.split("@@");
                if (!attributes[0].equals(budgetId)) {
                    writer.write(line);
                    writer.newLine();
                } else {
                    budgetFound = true;
                }
            }
            if (budgetFound) {
                writer.close();
                reader.close();
                Files.deleteIfExists(file.toPath());
                Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Presupuesto eliminado correctamente del archivo: " + file.getAbsolutePath());
            } else {
                System.out.println("Presupuesto con ID " + budgetId + " no encontrado.");
                tempFile.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al eliminar el presupuesto: " + e.getMessage());
        }
    }     
    
}