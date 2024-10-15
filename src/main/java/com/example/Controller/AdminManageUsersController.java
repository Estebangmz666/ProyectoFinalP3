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
<<<<<<< HEAD
    void btnDeleteUserClicked(ActionEvent event) {
        String idText = tfId.getText();
        
        if (idText.isEmpty()) {
            lbFullName.setText("Por favor, ingresa un ID!");
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
            } else {
                lbFullName.setText("Usuario no encontrado para eliminar.");
            }
        } catch (NumberFormatException e) {
            lbFullName.setText("ID inválido!");
=======
void btnDeleteUserClicked(ActionEvent event) {
    String idText = tfId.getText();
    
    if (idText.isEmpty()) {
        lbFullName.setText("Por favor, ingresa un ID!");
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
        } else {
            lbFullName.setText("Usuario no encontrado para eliminar.");
>>>>>>> 065d8c40b09c0bdd5df634690b95b1922c434664
        }
    }

    @FXML
    void btnFind(ActionEvent event) {
        String idText = tfId.getText();
        
        if (idText.isEmpty()) {
            lbFullName.setText("Por favor, ingresa un ID!");
            lbEmail.setText("");
            lbCellphone.setText("");
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
            } else {
                lbFullName.setText("Usuario no encontrado!");
                lbDirection.setText("");
                lbCellphone.setText("");
            }
        } catch (NumberFormatException e) {
            lbFullName.setText("ID inválido!");
            lbDirection.setText("");
            lbCellphone.setText("");
        }
    }

    @FXML
    void hlGoToUserDashboardClicked(ActionEvent event) {
        loadView(event, "/view/AdminDashboard.fxml");
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
                return;
            }

            String updatedName = tfUpdatedName.getText().trim();
            String updatedEmail = tfUpdatedEmail.getText().trim();
            String updatedDirection = tfUpdatedDirection.getText().trim();
            String updatedCellphone = tfUpdatedCellphone.getText().trim();

            if (updatedName.isEmpty() || updatedEmail.isEmpty() || updatedCellphone.isEmpty() || updatedDirection.isEmpty()){
                lbFullName.setText("Completa todos los campos!");
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
            } else {
                lbFullName.setText("Error al actualizar el usuario.");
            }
        } catch (NumberFormatException e){
            lbFullName.setText("Id invalido!");
        }
    }
}