package com.example.controller;

import com.example.dao.UserDAO;
import com.example.exception.UserAlreadyExistsException;
import com.example.model.User;
import com.example.service.AuthService;
import com.example.service.UserService;
import com.example.util.LogToFile;
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

public class UserUpdateInfoController implements ViewLoader{

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
            LogToFile.logToFile("SEVERE", "Error al cargar la vista: " + view + ". " + e.getMessage());
            e.printStackTrace();
        }
    }

    User currentUser = UserService.getCurrentUser();

    @FXML
    private Label lbMessage;

    @FXML
    private Button btnUpdateInfo;

    @FXML
    private Hyperlink hlGoToMainMenu;

    @FXML
    private Label lbCellphone;

    @FXML
    private Label lbDirection;

    @FXML
    private Label lbName;
    
    @FXML
    private Label lbEmail;

    @FXML
    private TextField tfUpdatedCellphone;

    @FXML
    private TextField tfUpdatedDirection;

    @FXML
    private TextField tfUpdatedName;

    @FXML
    private TextField tfUpdatedEmail;

    @FXML
    void btnUpdateInfoClicked(ActionEvent event) {
        String updatedCellphone = tfUpdatedCellphone.getText();
        String updatedDirection = tfUpdatedDirection.getText();
        String updatedName = tfUpdatedName.getText();
        String updatedEmail = tfUpdatedEmail.getText();
        if (updatedCellphone.isEmpty() || updatedDirection.isEmpty() || updatedName.isEmpty() || updatedEmail.isEmpty()) {
            lbMessage.setText("Por favor, completa todos los campos");
            LogToFile.logToFile("WARNING", "No se pueden dejar campos vacíos.");
            return;
        }        
        if (!AuthService.verifyEmailDomain(updatedEmail)){
            lbMessage.setText("Formato incorrecto de correo electronico");
            LogToFile.logToFile("WARNING", "Formato de correo incorrecto.");
            return;
        }
        if (!AuthService.verifyCellphone(updatedCellphone)){
            lbMessage.setText("Numero de telefono invalido");
            LogToFile.logToFile("WARNING", "Formato de telefono incorrecto.");
            return;
        }
        try {
            if (AuthService.emailAlreadyExists(updatedEmail)) {
                lbMessage.setText("El correo ya está en uso");
                LogToFile.logToFile("WARNING", "Correo ya existente.");
                return;
            }
        } catch (UserAlreadyExistsException e) {
            lbMessage.setText(e.getMessage());
            LogToFile.logToFile("SEVERE", "Excepción: " + e.getMessage());
        }
        boolean result = UserDAO.updateUserAndEmail(
            currentUser.getUserId(),
            updatedName,
            updatedEmail,
            updatedDirection,
            updatedCellphone
        );
        if (result) {
            lbMessage.setText("Información actualizada con éxito");
            LogToFile.logToFile("INFO", "Información del usuario actualizada correctamente.");
            lbName.setText("Nombre: " + updatedName);
            lbCellphone.setText("Telefono: " + updatedCellphone);
            lbDirection.setText("Direccion: " + updatedDirection);
            lbEmail.setText("Email: " + updatedEmail);
            currentUser.setName(updatedName);
            currentUser.setCellphone(updatedCellphone);
            currentUser.setDirection(updatedDirection);
            currentUser.setEmail(updatedEmail);
        } else{
            lbMessage.setText("Error al actualizar la información.");
            LogToFile.logToFile("SEVERE", "Error al actualizar la información del usuario.");
        }            
    }    

    @FXML
    void hlGoToMainMenuClicked(ActionEvent event) {
        loadView(event, "/view/UserDashboard.fxml");
    }

}