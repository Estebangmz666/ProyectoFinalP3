package co.edu.uniquindio.banco.bancouq.controller;

import java.util.List;

import co.edu.uniquindio.banco.bancouq.controller.service.IAccountPersistenceService;
import co.edu.uniquindio.banco.bancouq.model.Account;

public class AccountPersistenceService implements IAccountPersistenceService{
    @Override
    public void saveAccount(Account account) {
        // Logic to save a single account
    }

    @Override
    public void saveAllAccounts(List<Account> accounts) {
        // Logic to save all accounts
    }

    @Override
    public List<Account> loadAllAccounts() {
        // Logic to load all accounts
        return null;
    }

    @Override
    public Account deserializeAccount(String accountId) {
        // Logic to deserialize an account by its ID
        return new Account.AccountBuilder()
                .setAccountId(Integer.parseInt(accountId))
                // Assuming other details are deserialized accordingly
                .build();
    }

    @Override
    public void serializeAccount(Account account) {
        // Logic to serialize an account
    }
}
