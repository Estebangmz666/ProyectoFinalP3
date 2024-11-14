package co.edu.uniquindio.banco.bancouq.controller.service;

import co.edu.uniquindio.banco.bancouq.model.User;

public interface IAuthenticationService {
    boolean login(String email, String password);
    void logout(User user);
    boolean isUserAuthenticated(String userId);
}
