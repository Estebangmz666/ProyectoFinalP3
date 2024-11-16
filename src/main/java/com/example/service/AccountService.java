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
    private static List<Account> accounts = new ArrayList<>();

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
        accounts.add(account);
    }

    public static List<Account> getAllAccounts() {
        List<Account> allAccounts = new ArrayList<>();
        File folder = new File(UserService.getBasePath());

        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                allAccounts.addAll(deserializeAccountsFromFile(file));
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
        for (Account account : accounts) {
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
                    isFirstLine = false;
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
                } else {
                    System.out.println("Formato incorrecto en la línea: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al deserializar cuentas desde el archivo TXT");
        }
        
        return accounts;
    }

    private static List<Account> deserializeAccountsFromFile(File userFile) {
        List<Account> accounts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                String[] parts = line.split("@@");
                if (parts.length == 4) {
                    int accountId = Integer.parseInt(parts[0].trim());
                    String accountNumber = parts[1].trim();
                    AccountType accountType = AccountType.valueOf(parts[2].trim().toUpperCase());
                    BigDecimal accountBalance = new BigDecimal(parts[3].trim());

                    Account account = new Account(accountId, accountNumber, accountType, accountBalance, null);
                    accounts.add(account);
                } else {
                    System.out.println("Formato incorrecto en la línea: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al deserializar cuentas desde el archivo del usuario: " + userFile.getName());
        }
        
        return accounts;
    }
}