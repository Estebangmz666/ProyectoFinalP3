package co.edu.uniquindio.banco.bancouq.controller.service;

import java.io.IOException;
import java.util.List;
import co.edu.uniquindio.banco.bancouq.model.User;

public interface IUserPersistenceService {
    void saveUserToTxt(User user);
    void saveAllUsersToTxt(List<User> users);
    List<User> loadAllUsersFromTxt();
    User deserializeFromBinary(int userId);
    User deserializeFromXML(int userId);
    User deserializeFromText(int userId);
    void serializeToXML(User user, int userId);
    void serializeToBinary(User user);
    void serializeToText(User user);
    void copyXMLFile(int userId) throws IOException;
    void updateUserXML(int id, String name, String email, String direction, String cellphone);
}