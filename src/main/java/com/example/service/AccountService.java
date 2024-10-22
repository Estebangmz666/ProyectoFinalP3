package com.example.service;

import com.example.model.Account;
import com.example.model.User;

import java.util.ArrayList;
import java.util.List;

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
}
