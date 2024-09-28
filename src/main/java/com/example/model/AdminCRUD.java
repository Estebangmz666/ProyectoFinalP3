package com.example.model;


import java.io.Serializable;
import java.util.ArrayList;

public class AdminCRUD implements Serializable {
    private int adminId;          
    private String name;         
    private String email;         
    private String address;       
    private String phone;         
    private ArrayList<Account> accounts; // Lista de cuentas gestionadas por el administrador

    public AdminCRUD(int adminId, String name, String email, String address, String phone) {
        this.adminId = adminId;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.accounts = new ArrayList<>(); 
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    // Métodos CRUD

    // Crear un nuevo usuario (cuenta)
    public void addAccount(Account account) {
        if (account != null) {
            accounts.add(account);
        }
    }

    // Leer datos de un usuario (cuenta)
    public Account getAccount(int userId) {
        for (Account account : accounts) {
            if (account.getUserId() == userId) {
                return account;
            }
        }
        return null; // No se encontró la cuenta
    }

    // Actualizar un usuario (cuenta)
    public boolean updateAccount(int userId, Account updatedAccount) {
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            if (account.getUserId() == userId) {
                accounts.set(i, updatedAccount);
                return true; // Actualización exitosa
            }
        }
        return false; // No se encontró la cuenta para actualizar
    }

    // Eliminar un usuario (cuenta)
    public boolean removeAccount(int userId) {
        return accounts.removeIf(account -> account.getUserId() == userId);
    }

    // Listar todos los usuarios (cuentas)
    public ArrayList<Account> listAccounts() {
        return new ArrayList<>(accounts);
    }
}
