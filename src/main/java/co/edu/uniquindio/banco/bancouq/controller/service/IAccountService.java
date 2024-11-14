package co.edu.uniquindio.banco.bancouq.controller.service;

import java.util.List;

import co.edu.uniquindio.banco.bancouq.model.Account;
import co.edu.uniquindio.banco.bancouq.model.User;

public interface IAccountService {
    void createAccount(User user, Account account);
    void deleteAccount(User user, String accountId);
    Account findAccountById(User user, String accountId);
    List<Account> getAccountsByUser(User user);
    void updateAccount(User user, Account account);
    List<Account> getAllAccounts();
}
