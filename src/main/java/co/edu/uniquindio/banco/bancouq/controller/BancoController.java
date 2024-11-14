package co.edu.uniquindio.banco.bancouq.controller;

import co.edu.uniquindio.banco.bancouq.controller.service.IAccountPersistenceService;
import co.edu.uniquindio.banco.bancouq.controller.service.IAccountService;
import co.edu.uniquindio.banco.bancouq.controller.service.IUserService;

public class BancoController {
    private static BancoController instance;
    private final ModelFactoryController modelFactoryController;

    private BancoController() {
        this.modelFactoryController = ModelFactoryController.getInstance();
    }

    public static BancoController getInstance() {
        if (instance == null) {
            instance = new BancoController();
        }
        return instance;
    }

    public IAccountService getAccountService() {
        return modelFactoryController.getAccountService();
    }

    public IAccountPersistenceService getAccountPersistenceService() {
        return modelFactoryController.getAccountPersistenceService();
    }

    public IUserService getUserService(){
        return modelFactoryController.getUserService();
    }

}
