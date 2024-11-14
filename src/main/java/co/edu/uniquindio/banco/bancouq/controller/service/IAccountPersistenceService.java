package co.edu.uniquindio.banco.bancouq.controller.service;

import java.util.List;

import co.edu.uniquindio.banco.bancouq.model.Account;

public interface IAccountPersistenceService {
    void saveAccount(Account account);
    void saveAllAccounts(List<Account> accounts);
    List<Account> loadAllAccounts();
    Account deserializeAccount(String accountId);
    void serializeAccount(Account account);;
}
