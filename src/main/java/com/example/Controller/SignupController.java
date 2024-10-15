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

import com.example.service.UserService;
import com.example.model.ViewLoader;

public class SignupController implements ViewLoader {

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
    private Hyperlink hlGoToLogin;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfPassword;

    @FXML
    void hlGoToLoginClicked(ActionEvent event){
        loadView(event, "/view/Login.fxml");
        UserService.logToFile("INFO", "Usuario fue a Login.");
    }

    @FXML
    void initialize() {
        lbMessage.setWrapText(true);
        lbMessage.setStyle("-fx-text-alignment: center; -fx-font-size: 14px;");
    }

    @Override
    public void loadView(ActionEvent event, String view) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(view));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            lbMessage.setText("Error loading view: " + view);
            e.printStackTrace();
        }
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
            UserService.logToFile("WARNING", "Usuario intento registrarse con campos vacios.");
            return;
        }

        if (!UserService.verifyPassword(passwordText, confirmedPasswordText)) {
            lbMessage.setText("Las contraseñas no coinciden!");
            UserService.logToFile("WARNING", "Usuario intento registrarse con contraseñas que no coinciden.");
            return;
        }

        if (UserService.emailAlreadyExists(emailText)) {
            lbMessage.setText("El correo electrónico ya está en uso!");
            UserService.logToFile("WARNING", "Usuario intento registrarse con correo electronico en uso.");
            return;
        }

        if (!UserService.verifyCellphone(cellphoneText)) {
            lbMessage.setText("Número de celular no válido!");
            UserService.logToFile("WARNING", "Usuario intento registrarse con numero telefonico invalido.");
            return;
        }

        if (!UserService.verifyEmailDomain(emailText)) {
            lbMessage.setText("Dominio de correo electrónico no válido!");
            UserService.logToFile("WARNING", "Usuario intento registrarse con dominio de correo invalido.");
            return;
        }

        UserService.addUser(nameText, emailText, passwordText, directionText, cellphoneText);
        UserService.logToFile("INFO", "Usuario " + nameText + " registrado exitosamente.");
        loadView(event, "/view/Login.fxml");
    }
}