package com.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import com.example.service.UserService;

public class SignupController {

    @FXML
    private Button btnSignup;

    @FXML
    private Label lbMessage;

    @FXML
    private TextField tfCellphone;

    @FXML
    private TextField tfConfirmPassword;

    @FXML
    private TextField tfDirection;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfPassword;

    @FXML
    void initialize(){
        lbMessage.setWrapText(true);
        lbMessage.setStyle("-fx-text-alignment: center; -fx-font-size: 14px;");
    }

    @FXML
    void btnSignupClicked(ActionEvent event) {
        String cellphoneText = tfCellphone.getText();
        String directionText = tfDirection.getText();
        String nameText = tfName.getText();
        String emailText = tfEmail.getText();
        String passwordText = tfPassword.getText();
        String confirmedPasswordText = tfConfirmPassword.getText();

        if (nameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty() || confirmedPasswordText.isEmpty() || cellphoneText.isEmpty()) {
            lbMessage.setText("Por favor, completa todos los campos!");
            return;
        }
        
        if (!UserService.verifyPassword(passwordText, confirmedPasswordText)) {
            lbMessage.setText("Las contraseñas no coinciden!");
            return;
        }
        
        if (UserService.emailAlreadyExists(emailText)) {
            lbMessage.setText("El correo electrónico ya está en uso!");
            return;
        }
        
        if (!UserService.verifyCellphone(cellphoneText)) {
            lbMessage.setText("Número de celular no válido!");
            return;
        }
        
        if (!UserService.verifyEmailDomain(emailText)) {
            lbMessage.setText("Dominio de correo electrónico no válido!");
            return;
        }

        UserService.addUser(nameText, emailText, passwordText, directionText, cellphoneText);
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (Exception e) {
            lbMessage.setText("Error al cargar la página de inicio de sesión.");
            e.printStackTrace();
        }
    }
}