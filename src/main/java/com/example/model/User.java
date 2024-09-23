package com.example.model;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{
    private String userId;
    private String name;
    private String email;
    private String direction;
    private String cellphone;
    private ArrayList<Account> accounts;

    public User(String userId, String name, String email, String direction, String cellphone, ArrayList<Account> accounts){

        this.userId = userId;
        this.name = name;
        this.email = email;
        this.direction = direction;
        this.cellphone = cellphone;
        this.accounts = accounts != null ? accounts : new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }
}