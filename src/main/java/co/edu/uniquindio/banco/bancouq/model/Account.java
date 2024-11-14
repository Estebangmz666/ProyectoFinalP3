package co.edu.uniquindio.banco.bancouq.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private int accountId;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal balance;

    private Account(AccountBuilder builder) {
        this.accountId = builder.accountId;
        this.accountNumber = builder.accountNumber;
        this.accountType = builder.accountType;
        this.balance = builder.balance;
    }

    
    public int getAccountId() {
        return accountId;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public AccountType getAccountType() {
        return accountType;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }

    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }


    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }


    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Account other = (Account) obj;
        return accountId == other.accountId && 
        Objects.equals(accountNumber, other.accountNumber) &&
        accountType == other.accountType;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(accountId, accountNumber, accountType);
    }
    public static class AccountBuilder {
        private int accountId;
        private String accountNumber;
        private AccountType accountType;
        private BigDecimal balance;

        public AccountBuilder setAccountId(int accountId) {
            this.accountId = accountId;
            return this;
        }

        public AccountBuilder setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public AccountBuilder setAccountType(AccountType accountType) {
            this.accountType = accountType;
            return this;
        }

        public AccountBuilder setBalance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public Account build() {
            return new Account(this);
        }
    }
}
