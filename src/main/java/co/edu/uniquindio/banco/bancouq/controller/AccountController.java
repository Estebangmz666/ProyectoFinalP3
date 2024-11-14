package co.edu.uniquindio.banco.bancouq.controller;

import java.math.BigDecimal;
import java.util.List;

import co.edu.uniquindio.banco.bancouq.controller.service.IAccountPersistenceService;
import co.edu.uniquindio.banco.bancouq.controller.service.IAccountService;
import co.edu.uniquindio.banco.bancouq.model.Account;
import co.edu.uniquindio.banco.bancouq.model.AccountType;
import co.edu.uniquindio.banco.bancouq.model.User;

public class AccountController {
     private final IAccountService accountService;
    private final IAccountPersistenceService accountPersistenceService;

    public AccountController(IAccountService accountService, IAccountPersistenceService accountPersistenceService) {
        this.accountService = accountService;
        this.accountPersistenceService = accountPersistenceService;
    }

    public void addAccount(User user, String accountNumber, AccountType accountType, BigDecimal balance) {
        Account account = new Account.AccountBuilder()
                .setAccountNumber(accountNumber)
                .setAccountType(accountType)
                .setBalance(balance)
                .build();
        accountService.createAccount(user, account);
    }

    public void removeAccount(User user, String accountId) {
        accountService.deleteAccount(user, accountId);
    }

    public void saveAllAccounts(User user) {
        List<Account> accounts = accountService.getAccountsByUser(user);
        accountPersistenceService.saveAllAccounts(accounts);
    }
}
