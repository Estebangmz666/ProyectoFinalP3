package com.example.controller;

import com.example.dao.UserDAO;
import com.example.exception.InvalidEmailDomainException;
import com.example.exception.UserAlreadyExistsException;
import com.example.exception.UserNotFoundException;
import com.example.model.User;
import com.example.service.AuthService;
import com.example.util.LogToFile;
import com.example.util.SerializeDeserialize;
import com.example.util.ViewLoader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminManageUsersController implements ViewLoader {

    @FXML
    private Button btnDeleteUser;

    @FXML
    private Hyperlink hlGoToAdminDashboard;

    @FXML
    private Button btnFind;

    @FXML
    private Button btnUpdateUser;

    @FXML
    private Hyperlink hlGoToUserDashboard;

    @FXML
    private Label lbCellphone;

    @FXML
    private Label lbEmail;

    @FXML
    private Label lbDirection;

    @FXML
    private Label lbFullName;

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfUpdatedCellphone;

    @FXML
    private TextField tfUpdatedDirection;

    @FXML
    private TextField tfUpdatedEmail;

    @FXML
    private TextField tfUpdatedName;

    @Override
    public void loadView(ActionEvent event, String view) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(view));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            LogToFile.logToFile("INFO", "Vista cargada: " + view);
        } catch (Exception e) {
            LogToFile.logToFile("ERROR", "Error cargando la vista " + view + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void hlGoToAdminDashboardClicked(ActionEvent event) {
        loadView(event, "/view/AdminDashboard.fxml");
        LogToFile.logToFile("INFO", "Admin fue al panel principal");
    }

    @FXML
    void btnDeleteUserClicked(ActionEvent event) {
        String idText = tfId.getText();
        
        if (idText.isEmpty()) {
            lbFullName.setText("Por favor, ingresa un ID!");
            LogToFile.logToFile("WARNING", "Admin intentó eliminar un usuario sin especificar ID.");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            User user = UserDAO.searchById(id);
            if (user == null) {
                throw new UserNotFoundException("Usuario no encontrado para eliminar.");
            }

            boolean deleted = UserDAO.deleteUserById(id);
            if (deleted) {
                lbFullName.setText("Usuario eliminado correctamente.");
                lbEmail.setText("");
                lbDirection.setText("");
                lbCellphone.setText("");
                LogToFile.logToFile("INFO", "Admin eliminó usuario con ID " + id);
            } else {
                lbFullName.setText("Usuario no encontrado para eliminar.");
                LogToFile.logToFile("WARNING", "Admin intentó eliminar un usuario inexistente.");
            }
        } catch (NumberFormatException e) {
            lbFullName.setText("ID inválido!");
            LogToFile.logToFile("INFO", "Admin ingresó un ID inválido en gestión de usuarios.");
        } catch (UserNotFoundException e) {
            lbFullName.setText(e.getMessage());
            LogToFile.logToFile("SEVERE", e.getMessage());
        }
    }

    @FXML
    void btnFind(ActionEvent event) {
        String idText = tfId.getText();
        
        if (idText.isEmpty()) {
            lbFullName.setText("Por favor, ingresa un ID!");
            lbEmail.setText("");
            lbCellphone.setText("");
            LogToFile.logToFile("WARNING", "Admin intentó buscar un usuario sin especificar ID.");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            User user = UserDAO.searchById(id);
            if (user == null) {
                throw new UserNotFoundException("Usuario no encontrado.");
            }
            
            lbFullName.setText(user.getName());
            lbEmail.setText(user.getEmail());
            lbDirection.setText(user.getDirection());
            lbCellphone.setText(user.getCellphone());
            LogToFile.logToFile("INFO", "Admin buscó usuario con ID " + id);

        } catch (NumberFormatException e) {
            lbFullName.setText("ID inválido!");
            LogToFile.logToFile("INFO", "Admin ingresó un ID inválido en gestión de usuarios.");
        } catch (UserNotFoundException e) {
            lbFullName.setText(e.getMessage());
            LogToFile.logToFile("SEVERE", e.getMessage());
        }
    }

    @FXML
    void hlGoToUserDashboardClicked(ActionEvent event) {
        loadView(event, "/view/AdminDashboard.fxml");
        LogToFile.logToFile("INFO", "Admin fue al Panel de Control.");
    }

    @FXML
    void btnUpdateUserClicked(ActionEvent event) throws UserAlreadyExistsException {
        String idText = tfId.getText().trim();
        String oldEmail = lbEmail.getText().trim();
        
        if (idText.isEmpty() || !idText.matches("\\d+")) {
            lbFullName.setText("Ingresa un ID válido!");
            LogToFile.logToFile("INFO", "Admin ingresó un ID inválido en actualización de usuario.");
            return;
        }    

        try {
            int id = Integer.parseInt(idText);
            User user = UserDAO.searchById(id);
            if (user == null) {
                throw new UserNotFoundException("Usuario no encontrado para actualizar.");
            }

            String updatedName = tfUpdatedName.getText().trim();
            String updatedEmail = tfUpdatedEmail.getText().trim();
            String updatedDirection = tfUpdatedDirection.getText().trim();
            String updatedCellphone = tfUpdatedCellphone.getText().trim();

            if (updatedName.isEmpty() || updatedEmail.isEmpty() || updatedCellphone.isEmpty() || updatedDirection.isEmpty()) {
                lbFullName.setText("Completa todos los campos!");
                LogToFile.logToFile("WARNING", "Admin intentó actualizar usuario con campos incompletos.");
                return;
            }

            if (!AuthService.verifyEmailDomain(updatedEmail)) {
                throw new InvalidEmailDomainException("El email no tiene dominio válido!");
            }

            if (AuthService.emailAlreadyExists(updatedEmail)) {
                lbFullName.setText("El email ya está en uso");
                LogToFile.logToFile("WARNING", "Admin intentó actualizar a un email que ya está en uso.");
                return;
            } else if (!AuthService.verifyCellphone(updatedCellphone)) {
                lbFullName.setText("Ingresa un número de teléfono válido");
                LogToFile.logToFile("WARNING", "Admin intentó actualizar con un número de teléfono inválido.");
                return;
            }

            user.setName(updatedName);
            user.setEmail(updatedEmail);
            user.setDirection(updatedDirection);
            user.setCellphone(updatedCellphone);

            boolean updated = UserDAO.updateUser(id, updatedName, updatedEmail, updatedDirection, updatedCellphone);
            
            if (updated) {
                SerializeDeserialize.updateUserXML(id, updatedName, updatedEmail, updatedDirection, updatedCellphone);
                UserDAO.updateEmailInUsersFile(oldEmail, updatedEmail);
                lbFullName.setText("Usuario actualizado correctamente");
                lbEmail.setText(user.getEmail());
                lbDirection.setText(user.getDirection());
                lbCellphone.setText(user.getCellphone());
                LogToFile.logToFile("INFO", "Admin actualizó datos de usuario con ID " + id);
            } else {
                lbFullName.setText("Error al actualizar el usuario.");
                LogToFile.logToFile("SEVERE", "Error al actualizar los datos del usuario.");
            }
        } catch (NumberFormatException e) {
            lbFullName.setText("ID inválido!");
            LogToFile.logToFile("INFO", "Error al convertir ID en actualización: " + e.getMessage());
        } catch (UserNotFoundException e) {
            lbFullName.setText(e.getMessage());
            lbEmail.setText("");
            lbDirection.setText("");
            lbCellphone.setText("");
            LogToFile.logToFile("SEVERE", e.getMessage());
        } catch (InvalidEmailDomainException e) {
            lbFullName.setText(e.getMessage());
            LogToFile.logToFile("SEVERE", e.getMessage());
        }        
    }
}
