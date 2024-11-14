package co.edu.uniquindio.banco.bancouq.controller;

import java.util.List;

import co.edu.uniquindio.banco.bancouq.controller.service.IAccountService;
import co.edu.uniquindio.banco.bancouq.model.Account;
import co.edu.uniquindio.banco.bancouq.model.User;

public class AccountService implements IAccountService{
    @Override
    public void createAccount(User user, Account account) {
        Account newAccount = new Account.AccountBuilder()
                .setAccountId(user.getAccounts().size() + 1)
                .setAccountNumber(account.getAccountNumber())
                .setAccountType(account.getAccountType())
                .setBalance(account.getBalance())
                .build();
        user.addAccount(newAccount);
    }

    @Override
    public void deleteAccount(User user, String accountId) {
        Account account = findAccountById(user, accountId);
        if (account != null) {
            user.getAccounts().remove(account);
        }
    }

    @Override
    public Account findAccountById(User user, String accountId) {
        return user.getAccounts().stream()
                .filter(account -> String.valueOf(account.getAccountId()).equals(accountId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Account> getAccountsByUser(User user) {
        return user.getAccounts();
    }

    @Override
    public void updateAccount(User user, Account account) {
        Account existingAccount = findAccountById(user, String.valueOf(account.getAccountId()));
        if (existingAccount != null) {
            existingAccount.setBalance(account.getBalance());
            existingAccount.setAccountType(account.getAccountType());
        }
    }

    @Override
    public List<Account> getAllAccounts(){
        return List.of();
    }

}
