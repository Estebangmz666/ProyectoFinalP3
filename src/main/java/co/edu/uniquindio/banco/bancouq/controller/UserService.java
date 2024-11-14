package co.edu.uniquindio.banco.bancouq.controller;
import java.util.ArrayList;
import java.util.List;

import co.edu.uniquindio.banco.bancouq.controller.service.IUserService;
import co.edu.uniquindio.banco.bancouq.model.User;

public class UserService implements IUserService {

    private static List<User> users = new ArrayList<>();
    private static int userIdCounter = 0;

    @Override
    public void addUser(User user) {
        userIdCounter++;
        User newUser = new User.UserBuilder()
                .setUserId(userIdCounter)
                .setName(user.getName())
                .setEmail(user.getEmail())
                .setDirection(user.getDirection())
                .setCellphone(user.getCellphone())
                .build();
        users.add(newUser);
    }

    @Override
    public void deleteUser(String userId) {
        User userToDelete = findUserById(userId);
        if (userToDelete != null) {
            users.remove(userToDelete);
        }
    }

    @Override
    public User findUserById(String userId) {
        return users.stream()
                .filter(user -> String.valueOf(user.getUserId()).equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return users;
    }

    @Override
    public void updateUser(User user) {
        User existingUser = findUserById(String.valueOf(user.getUserId()));
        if (existingUser != null) {
            existingUser = new User.UserBuilder()
                    .setUserId(existingUser.getUserId())
                    .setName(user.getName())
                    .setEmail(user.getEmail())
                    .setDirection(user.getDirection())
                    .setCellphone(user.getCellphone())
                    .setAccounts(existingUser.getAccounts())
                    .build();
        }
    }
   
}
