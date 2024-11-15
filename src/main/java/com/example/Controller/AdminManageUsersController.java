package com.example.controller;

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

import com.example.exception.*;
import com.example.model.User;
import com.example.model.ViewLoader;
import com.example.service.UserService;

public class AdminManageUsersController implements ViewLoader{

    @Override
    public void loadView(ActionEvent event, String view) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(view));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    @FXML
    void hlGoToAdminDashboardClicked(ActionEvent event) {
        loadView(event, "/view/AdminDashboard.fxml");
        UserService.logToFile("INFO", "Admin fue al panel principal");
    }

    @FXML
    void btnDeleteUserClicked(ActionEvent event) {
        String idText = tfId.getText();
        
        if (idText.isEmpty()) {
            lbFullName.setText("Por favor, ingresa un ID!");
            UserService.logToFile("WARNING", "Admin intento buscar usuario que no existe.");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            User user = UserService.searchById(id);
            if (user == null) {
                throw new UserNotFoundException("Usuario no encontrado para eliminar.");
            }

            boolean deleted = UserService.deleteUserById(id);
            if (deleted) {
                lbFullName.setText("Usuario eliminado correctamente.");
                lbEmail.setText("");
                lbDirection.setText("");
                lbCellphone.setText("");
                UserService.logToFile("INFO", "Admin elimino usuario " + lbFullName.getText());
            } else {
                lbFullName.setText("Usuario no encontrado para eliminar.");
                UserService.logToFile("WARNING", "Admin intento eliminar usuario inexistente.");
            }
        } catch (NumberFormatException e) {
            lbFullName.setText("ID inválido!");
            UserService.logToFile("INFO", "Admin ingreso un ID invalido en gestión de usuarios.");
        } catch (UserNotFoundException e) {
            lbFullName.setText(e.getMessage());
            UserService.logToFile("SEVERE", e.getMessage());
        }
    }

    @FXML
    void btnFind(ActionEvent event) {
        String idText = tfId.getText();
        
        if (idText.isEmpty()) {
            lbFullName.setText("Por favor, ingresa un ID!");
            lbEmail.setText("");
            lbCellphone.setText("");
            UserService.logToFile("WARNING", "Admin intento buscar usuario inexistente");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            User user = UserService.searchById(id);
            if (user == null) {
                throw new UserNotFoundException("Usuario no encontrado.");
            }
            
            lbFullName.setText(user.getName());
            lbEmail.setText(user.getEmail());
            lbDirection.setText(user.getDirection());
            lbCellphone.setText(user.getCellphone());
            UserService.logToFile("INFO", "Admin buscó usuario " + lbFullName.getText());

        } catch (NumberFormatException e) {
            lbFullName.setText("ID inválido!");
            lbDirection.setText("");
            lbCellphone.setText("");
            UserService.logToFile("INFO", "Admin ingreso un ID invalido en gestión de usuarios.");
        } catch (UserNotFoundException e) {
            lbFullName.setText(e.getMessage());
            lbDirection.setText("");
            lbCellphone.setText("");
            UserService.logToFile("SEVERE", e.getMessage());
        }
    }

    @FXML
    void hlGoToUserDashboardClicked(ActionEvent event) {
        loadView(event, "/view/AdminDashboard.fxml");
        UserService.logToFile("INFO", "Admin fué al Panel de Control.");
    }

    @FXML
    void btnUpdateUserClicked(ActionEvent event) throws UserAlreadyExistsException {
        String idText = tfId.getText().trim();
        String oldEmail = lbEmail.getText().trim();
        
        if (idText.isEmpty() || !idText.matches("\\d+")) {
            lbFullName.setText("Ingresa un ID válido!");
            UserService.logToFile("INFO", "Admin ingresó un ID inválido en gestión de usuarios.");
            return;
        }    

        try {
            int id = Integer.parseInt(idText);
            User user = UserService.searchById(id);
            if (user == null) {
                throw new UserNotFoundException("Usuario no encontrado para actualizar.");
            }

            String updatedName = tfUpdatedName.getText().trim();
            String updatedEmail = tfUpdatedEmail.getText().trim();
            String updatedDirection = tfUpdatedDirection.getText().trim();
            String updatedCellphone = tfUpdatedCellphone.getText().trim();

            System.out.println("Nombre actualizado: " + updatedName);
            System.out.println("Email actualizado: " + updatedEmail);
            System.out.println("Dirección actualizada: " + updatedDirection);
            System.out.println("Teléfono actualizado: " + updatedCellphone);

            if (updatedName.isEmpty() || updatedEmail.isEmpty() || updatedCellphone.isEmpty() || updatedDirection.isEmpty()) {
                lbFullName.setText("Completa todos los campos!");
                UserService.logToFile("WARNING", "Admin intentó actualizar datos sin completar todos los campos.");
                return;
            }

            // Aquí implementamos la excepción para verificar el dominio del correo
            if (!UserService.verifyEmailDomain(updatedEmail)) {
                throw new InvalidEmailDomainException("El email no tiene dominio valido!");
            }

            if (UserService.emailAlreadyExists(updatedEmail)) {
                lbFullName.setText("El email ya está en uso");
                UserService.logToFile("WARNING", "Admin intentó actualizar email que ya está en uso.");
                return;
            } else if (!UserService.verifyCellphone(updatedCellphone)) {
                lbFullName.setText("Ingresa un numero de telefono valido");
                UserService.logToFile("WARNING", "Admin intentó actualizar numero de telefono invalido.");
                return;
            }

            user.setName(updatedName);
            user.setEmail(updatedEmail);
            user.setDirection(updatedDirection);
            user.setCellphone(updatedCellphone);

            boolean updated = UserService.updateUser(id, updatedName, updatedEmail, updatedDirection, updatedCellphone);
            
            if (updated) {
                UserService.updateUserXML(id, updatedName, updatedEmail, updatedDirection, updatedCellphone);
                UserService.updateEmailInUsersFile(oldEmail, updatedEmail);
                lbFullName.setText("Usuario actualizado correctamente");
                lbEmail.setText(user.getEmail());
                lbDirection.setText(user.getDirection());
                lbCellphone.setText(user.getCellphone());
                UserService.logToFile("INFO", "Admin actualizó datos de usuario " + lbFullName.getText());
            } else {
                lbFullName.setText("Error al actualizar el usuario.");
                UserService.logToFile("SEVERE", "Admin no pudo actualizar los datos del usuario.");
            }
        } catch (NumberFormatException e) {
            lbFullName.setText("ID inválido!");
            System.out.println("Error al convertir el ID: '" + idText + "'");
            e.printStackTrace();
        } catch (UserNotFoundException e) {
            lbFullName.setText(e.getMessage());
            lbEmail.setText("");
            lbDirection.setText("");
            lbCellphone.setText("");
            UserService.logToFile("SEVERE", e.getMessage());
        } catch (InvalidEmailDomainException e) {
            lbFullName.setText(e.getMessage());
            UserService.logToFile("SEVERE", e.getMessage());
        }        
    }
}
