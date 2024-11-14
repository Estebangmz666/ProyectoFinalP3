package co.edu.uniquindio.banco.bancouq.controller;

import java.util.List;

import co.edu.uniquindio.banco.bancouq.controller.service.IAuthenticationService;
import co.edu.uniquindio.banco.bancouq.controller.service.IUserPersistenceService;
import co.edu.uniquindio.banco.bancouq.controller.service.IUserService;
import co.edu.uniquindio.banco.bancouq.model.User;

public class UserController {
    private final IUserService userService;
    private final IAuthenticationService authenticationService;
    private final IUserPersistenceService userPersistenceService;

    public UserController(IUserService userService, IAuthenticationService authenticationService, IUserPersistenceService userPersistenceService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.userPersistenceService = userPersistenceService;
    }

    public void registerUser(User user) {
        userService.addUser(user);
    }

    public boolean authenticateUser(String email, String password) {
        return authenticationService.login(email, password);
    }

    public void saveUsersToFile() {
        List<User> users = userService.getAllUsers();
        userPersistenceService.saveAllUsersToTxt(users);
    }
}
