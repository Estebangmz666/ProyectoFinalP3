package co.edu.uniquindio.banco.bancouq.controller;

import co.edu.uniquindio.banco.bancouq.controller.service.IUserPersistenceService;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import co.edu.uniquindio.banco.bancouq.model.User;
import co.edu.uniquindio.banco.bancouq.utils.PathSolver;

public class UserPersistenceService implements IUserPersistenceService {
    @Override
    public void saveUserToTxt(User user) {
        String filePath = PathSolver.getResourcePath("persistance/files") + "/user_" + user.getUserId() + ".txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            String data = String.format("%s@@%s@@%s@@%s@@%s%n", 
                    user.getUserId(),
                    user.getName(),
                    user.getEmail(),
                    user.getCellphone(),
                    user.getDirection());
            bw.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al serializar usuario en txt.");
        }
    }

    @Override
    public void saveAllUsersToTxt(List<User> users) {
        for (User user : users) {
            saveUserToTxt(user);
        }
    }

    @Override
    public List<User> loadAllUsersFromTxt() {
        // Logic to load all users from text file
        return null;
    }

    @Override
    public User deserializeFromBinary(int userId) {
        String filepath = PathSolver.getResourcePath("persistance/files") + "/user_" + userId + ".bin";
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filepath))) {
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error al deserializar objeto bin.");
        }
        return null;
    }

    @Override
    public User deserializeFromXML(int userId) {
        String filePath = PathSolver.getResourcePath("persistance/files") + "/user_" + userId + ".xml";
        try (XMLDecoder decoder = new XMLDecoder(new FileInputStream(filePath))) {
            return (User) decoder.readObject();
        } catch (IOException e) {
            System.out.println("Error al deserializar el usuario desde XML: " + e.getMessage());
        }
        return null;
    }

    @Override
    public User deserializeFromText(int userId) {
        String filePath = PathSolver.getResourcePath("persistance/files") + "/user_" + userId + ".txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            if ((line = br.readLine()) != null) {
                String[] parts = line.split("@@");
                if (parts.length == 5) {
                    return new User.UserBuilder()
                            .setUserId(Integer.parseInt(parts[0].trim()))
                            .setName(parts[1].trim())
                            .setEmail(parts[2].trim())
                            .setDirection(parts[4].trim())
                            .setCellphone(parts[3].trim())
                            .build();
                }
            }
        } catch (IOException e) {
            System.out.println("Error al deserializar usuario desde TXT: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void serializeToXML(User user, int userId) {
        String xmlFilePath = PathSolver.getResourcePath("persistance/files") + "/user_" + userId + ".xml";
        try (XMLEncoder encoder = new XMLEncoder(new FileOutputStream(xmlFilePath))) {
            encoder.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al serializar el usuario en XML.");
        }
    }

    @Override
    public void serializeToBinary(User user) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PathSolver.getResourcePath("persistance/files") + "/user_" + user.getUserId() + ".bin"))) {
            oos.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al serializar usuario en binario.");
        }
    }

    @Override
    public void serializeToText(User user) {
        saveUserToTxt(user);
    }

    @Override
    public void copyXMLFile(int userId) throws IOException {
        String sourceFile = PathSolver.getResourcePath("persistance/files") + "/user_" + userId + ".xml";
        String backupFolder = PathSolver.getResourcePath("persistance/backup");
        File folder = new File(backupFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yy_HH-mm-ss"));
        String destinationFile = backupFolder + "/user_" + userId + "_" + timestamp + ".xml";
        Files.copy(Paths.get(sourceFile), Paths.get(destinationFile), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("XML file copied to backup folder: " + destinationFile);
    }

    @Override
    public void updateUserXML(int id, String name, String email, String direction, String cellphone) {
        String xmlFilePath = PathSolver.getResourcePath("persistance/files") + "/user_" + id + ".xml";
        try (XMLDecoder decoder = new XMLDecoder(new FileInputStream(xmlFilePath))) {
            User user = (User) decoder.readObject();
            user = new User.UserBuilder()
                    .setUserId(user.getUserId())
                    .setName(name)
                    .setEmail(email)
                    .setDirection(direction)
                    .setCellphone(cellphone)
                    .setAccounts(user.getAccounts())
                    .build();
            try (XMLEncoder encoder = new XMLEncoder(new FileOutputStream(xmlFilePath))) {
                encoder.writeObject(user);
                System.out.println("Usuario actualizado correctamente en el archivo XML: " + xmlFilePath);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error al guardar el archivo XML");
            }
            copyXMLFile(id);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Archivo XML no encontrado: " + xmlFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al copiar el archivo XML");
        }
    }
} 