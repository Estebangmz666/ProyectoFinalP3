package co.edu.uniquindio.banco.bancouq.controller.service;

import java.util.List;
import co.edu.uniquindio.banco.bancouq.model.User;

public interface IUserService {
    void addUser(User user);
    void deleteUser(String userId);
    User findUserById(String userId);
    List<User> getAllUsers();
    void updateUser(User user);
}
