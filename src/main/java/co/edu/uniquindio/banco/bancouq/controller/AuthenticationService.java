package co.edu.uniquindio.banco.bancouq.controller;

import co.edu.uniquindio.banco.bancouq.controller.service.IAuthenticationService;
import co.edu.uniquindio.banco.bancouq.model.User;

public class AuthenticationService implements IAuthenticationService {
    @Override
    public boolean login(String email, String password) {
        // Logic to authenticate user
        return false;
    }

    @Override
    public void logout(User user) {
        // Logic to logout user
    }

    @Override
    public boolean isUserAuthenticated(String userId) {
        // Logic to check if user is authenticated
        return false;
    }
}