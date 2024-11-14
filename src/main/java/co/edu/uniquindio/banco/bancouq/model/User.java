package co.edu.uniquindio.banco.bancouq.model;

import java.io.Serializable;
import java.util.ArrayList;

// User class with Builder
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private int userId;
    private String name;
    private String email;
    private String direction;
    private String cellphone;
    private ArrayList<Account> accounts;
    private String password;

    
    private User(UserBuilder builder) {
        this.userId = builder.userId;
        this.name = builder.name;
        this.email = builder.email;
        this.direction = builder.direction;
        this.cellphone = builder.cellphone;
        this.accounts = builder.accounts != null ? builder.accounts : new ArrayList<>();
        this.password = builder.password;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getDirection() {
        return direction;
    }
    
    public String getCellphone() {
        return cellphone;
    }
    
    public ArrayList<Account> getAccounts() {
        return accounts;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setDirection(String direction) {
        this.direction = direction;
    }
    
    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }
    
    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }
    
    public void addAccount(Account account) {
        this.accounts.add(account);
    }
    
    public String getPassword() {
        return password;
    }

    public static class UserBuilder {
        private int userId;
        private String name;
        private String email;
        private String direction;
        private String cellphone;
        private ArrayList<Account> accounts;
        private String password;
    
        public UserBuilder setUserId(int userId) {
            this.userId = userId;
            return this;
        }
    
        public UserBuilder setName(String name) {
            this.name = name;
            return this;
        }
    
        public UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }
    
        public UserBuilder setDirection(String direction) {
            this.direction = direction;
            return this;
        }
    
        public UserBuilder setCellphone(String cellphone) {
            this.cellphone = cellphone;
            return this;
        }
    
        public UserBuilder setAccounts(ArrayList<Account> accounts) {
            this.accounts = accounts;
            return this;
        }

        public UserBuilder setPassword(String password){
            this.password = password;
            return this;
        }
    
        public User build() {
            return new User(this);
        }
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}