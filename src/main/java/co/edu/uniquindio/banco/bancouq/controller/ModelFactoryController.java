package co.edu.uniquindio.banco.bancouq.controller;

import co.edu.uniquindio.banco.bancouq.controller.service.IAccountPersistenceService;
import co.edu.uniquindio.banco.bancouq.controller.service.IAccountService;
import co.edu.uniquindio.banco.bancouq.controller.service.IUserService;
import co.edu.uniquindio.banco.bancouq.model.Account;
import co.edu.uniquindio.banco.bancouq.model.User;

import java.util.List;

public class ModelFactoryController {
    private static ModelFactoryController instance;

    private final IAccountService accountService;
    private final IAccountPersistenceService accountPersistenceService;
    private final IUserService userService;

    private ModelFactoryController() {
        this.accountService = new AccountService();
        this.accountPersistenceService = new AccountPersistenceService();
        this.userService = new UserService();
    }

    public static ModelFactoryController getInstance() {
        if (instance == null) {
            instance = new ModelFactoryController();
        }
        return instance;
    }

    public IAccountService getAccountService() {
        return accountService;
    }

    public IAccountPersistenceService getAccountPersistenceService() {
        return accountPersistenceService;
    }

    public IUserService getUserService() {
        return userService;
    }

    public void saveAllAccounts(List<Account> accounts) {
        accountPersistenceService.saveAllAccounts(accounts);
    }

    public void addUser(User user) {
        userService.addUser(user);
    }
}
