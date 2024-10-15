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
            
            if (user != null) {
                lbFullName.setText(user.getName());
                lbEmail.setText(user.getEmail());
                lbDirection.setText(user.getDirection());
                lbCellphone.setText(user.getCellphone());
                UserService.logToFile("INFO", "Admin buscó usuario " + lbFullName.getText());

            } else {
                lbFullName.setText("Usuario no encontrado!");
                lbDirection.setText("");
                lbCellphone.setText("");
                UserService.logToFile("WARNING", "Admin intento buscar usuario inexistente");
            }
        } catch (NumberFormatException e) {
            lbFullName.setText("ID inválido!");
            lbDirection.setText("");
            lbCellphone.setText("");
            UserService.logToFile("INFO", "Admin ingreso un ID invalido en gestión de usuarios.");
        }
    }

    @FXML
    void hlGoToUserDashboardClicked(ActionEvent event) {
        loadView(event, "/view/AdminDashboard.fxml");
        UserService.logToFile("INFO", "Admin fué al Panel de Control.");
    }

    @FXML
    void btnUpdateUserClicked(ActionEvent event) {
        String idText = tfId.getText();
        if (idText.isEmpty()){
            lbFullName.setText("Ingresa un ID!");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            User user = UserService.searchById(id);

            if (user == null){
                lbFullName.setText("Usuario no encontrado!");
                UserService.logToFile("WARNING", "Admin intento buscar usuario inexistente");
                return;
            }

            String updatedName = tfUpdatedName.getText().trim();
            String updatedEmail = tfUpdatedEmail.getText().trim();
            String updatedDirection = tfUpdatedDirection.getText().trim();
            String updatedCellphone = tfUpdatedCellphone.getText().trim();

            if (updatedName.isEmpty() || updatedEmail.isEmpty() || updatedCellphone.isEmpty() || updatedDirection.isEmpty()){
                lbFullName.setText("Completa todos los campos!");
                UserService.logToFile("WARNING", "Admin intento actualizar datos de un usuario sin rellenar todos los campos.");
                return;
            }

            user.setName(updatedName);
            user.setEmail(updatedEmail);
            user.setDirection(updatedDirection);
            user.setCellphone(updatedCellphone);

            boolean updated = UserService.updateUser(Integer.parseInt(idText), updatedName, updatedEmail, updatedDirection, updatedCellphone);
            if (updated){
                lbFullName.setText("Usuario actualizado correctamente");
                lbEmail.setText(user.getEmail());
                lbDirection.setText(user.getDirection());
                lbCellphone.setText(user.getCellphone());
                UserService.logToFile("INFO", "Admin actualizó datos de usuario " + lbFullName.getText());
            } else {
                lbFullName.setText("Error al actualizar el usuario.");
                UserService.logToFile("SEVERE", "Admin no pudó actualizar datos de usuario");
            }
        } catch (NumberFormatException e){
            lbFullName.setText("Id invalido!");
            UserService.logToFile("INFO", "Admin ingreso un ID invalido en gestión de usuarios.");
        }
    }
}