package com.example.service;

import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Account;
import com.example.model.AccountType;
import com.example.model.User;

public class AccountService {

    static int accountIdCounter = 0;
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
        return accounts;
    }

    public static void setupBigDecimalPersistence(Encoder encoder) {
        PersistenceDelegate bigDecimalDelegate = new DefaultPersistenceDelegate() {
            protected Expression instantiate(Object oldInstance, Encoder out) {
                BigDecimal bd = (BigDecimal) oldInstance;
                return new Expression(bd, bd.getClass(), "new", new Object[]{bd.toString()});
            }
        };
        encoder.setPersistenceDelegate(BigDecimal.class, bigDecimalDelegate);
    }

    public static void serializeAccountsToTxt(Account account, User user) {
        String filePath = "src\\main\\java\\com\\example\\persistance\\files\\user_" + user.getUserId() + ".txt";
        
        File directory = new File("src\\main\\java\\com\\example\\persistance\\files");
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
    
    public static List<Account> deserializeAccountsFromTxt(User user) {
        String filePath = "src\\main\\java\\com\\example\\persistance\\files\\user_" + user.getUserId() + ".txt";
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

                    Account account = new Account(accountId, accountNumber, accountType, accountBalance);
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

}