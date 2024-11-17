package com.example.service;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Account;
import com.example.model.AccountType;
import com.example.model.User;

public class AccountService {

    private static int accountIdCounter = 0;

    public static int getNextAccountId() {
        return ++accountIdCounter;
    }

    public static void addAccount(Account account) {
        User currentUser = UserService.getCurrentUser();
        if (currentUser != null){
            currentUser.addAccount(account);
        } else {
            System.out.println("No se encontró un usuario actual");
        }
    }

    public static List<Account> getAllAccounts() {
        List<Account> allAccounts = new ArrayList<>();
        File folder = new File(UserService.getBasePath());

        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                User currentUser = UserService.getCurrentUser();
                if (currentUser != null) {
                    allAccounts.addAll(deserializeAccountsFromTxt(currentUser)); 
                }
            }
        }
        return allAccounts;
    }
    
    public static void serializeAccountsToTxt(Account account, User user) {
        String filePath = UserService.getBasePath() + "\\user_" + user.getUserId() + ".txt";
        
        File directory = new File(UserService.getBasePath());
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            String data = String.format("%d@@%s@@%s@@%s%n",
                account.getAccountId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getBalance().toString());
            bw.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al serializar cuenta en txt.");
        }
    }
    
    public static boolean doesAccountExist(String accountNumber) {
        List<Account> allAccounts = getAllAccounts();
        for (Account account : allAccounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return true;
            }
        }
        return false;
    }

    public static Account getAccountByNumber(String accountNumber) {
        List<Account> allAccounts = getAllAccounts();
        
        for (Account account : allAccounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        
        return null;
    }

    public static List<Account> deserializeAccountsFromTxt(User user) {
        String filePath = UserService.getBasePath() + "\\user_" + user.getUserId() + ".txt";
        List<Account> accounts = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip the first line, which is user info
                    continue;
                }
                
                String[] parts = line.split("@@");
                if (parts.length == 4) {
                    int accountId = Integer.parseInt(parts[0].trim());
                    String accountNumber = parts[1].trim();
                    AccountType accountType = AccountType.valueOf(parts[2].trim().toUpperCase());
                    BigDecimal accountBalance = new BigDecimal(parts[3].trim());

                    Account account = new Account(accountId, accountNumber, accountType, accountBalance, user);
                    accounts.add(account);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al deserializar cuentas desde el archivo TXT");
        }
        
        return accounts;
    }

    // Eliminar la cuenta específica
    public static void deleteAccount(Account account) {
        User currentUser = UserService.getCurrentUser();
        if (currentUser != null) {
            // Eliminar la cuenta de la lista de cuentas del usuario
            currentUser.getAccounts().remove(account);

            // Eliminar la cuenta del archivo correspondiente
            deleteAccountFromFile(currentUser, account);

            // Actualizar el archivo con las cuentas restantes
            updateUserFile(currentUser);
        } else {
            System.out.println("No se encontró el usuario actual");
        }
    }

    // Eliminar la cuenta desde el archivo
    private static void deleteAccountFromFile(User user, Account account) {
        String filePath = UserService.getBasePath() + "\\user_" + user.getUserId() + ".txt";
        File tempFile = new File(UserService.getBasePath() + "\\temp_user_" + user.getUserId() + ".txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    writer.write(line + System.lineSeparator()); // Escribir la primera línea (información del usuario)
                    isFirstLine = false;
                } else {
                    String[] data = line.split("@@");
                    if (data.length >= 4 && !data[1].equals(account.getAccountNumber())) {
                        writer.write(line + System.lineSeparator()); // Escribir las líneas que no coincidan con la cuenta que se desea eliminar
                    }
                }
            }

            // Reemplazar el archivo original por el archivo temporal sin la cuenta eliminada
            File originalFile = new File(filePath);
            if (originalFile.delete()) {
                tempFile.renameTo(originalFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al eliminar la cuenta del archivo: " + e.getMessage());
        }
    }

    // Método para actualizar el archivo con las cuentas restantes
    private static void updateUserFile(User user) {
        String filePath = UserService.getBasePath() + "\\user_" + user.getUserId() + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(String.format("%d@@%s@@%s@@%s@@%s%n", // Escribir la primera línea de información del usuario
                    user.getUserId(),
                    user.getName(),
                    user.getEmail(),
                    user.getCellphone(),
                    user.getDirection())); // Añadir la dirección

            for (Account account : user.getAccounts()) {
                String data = String.format("%d@@%s@@%s@@%s%n",
                        account.getAccountId(),
                        account.getAccountNumber(),
                        account.getAccountType(),
                        account.getBalance().toString());
                writer.write(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al actualizar el archivo del usuario: " + e.getMessage());
        }
    }
}