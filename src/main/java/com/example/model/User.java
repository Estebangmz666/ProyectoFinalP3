package com.example.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private static int idCounter;
    private static final long serialVersionUID = 1L;
    private int userId;
    private String name;
    private String email;
    private String direction;
    private String cellphone;
    private ArrayList<Account> accounts;

    public User() {
        this.accounts = new ArrayList<>();
    }

    public User(int userId, String name, String email, String direction, String cellphone, ArrayList<Account> accounts) {

        if(userId == 0){
            int newId = ++User.idCounter;
            User.idCounter = newId;
            this.userId = newId;
        }else{
            this.userId = userId;
        }
        this.name = name;
        this.email = email;
        this.direction = direction;
        this.cellphone = cellphone;
        this.accounts = accounts != null ? accounts : new ArrayList<>();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts != null ? new ArrayList<>(accounts) : new ArrayList<>();
    }    

    public void addAccount(Account account) {
        if (this.accounts == null) {
            this.accounts = new ArrayList<>();
        }
        this.accounts.add(account);
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public static int getIdCounter(){
        return User.idCounter;
    }

    public static void setCounterId(int id){
        User.idCounter = id;
    }
}