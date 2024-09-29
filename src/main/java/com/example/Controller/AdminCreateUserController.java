package com.example.controller;

import com.example.service.UserService;
import com.example.model.ViewLoader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminCreateUserController implements ViewLoader{

    @FXML
    private Button btnSignup;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private PasswordField pfConfirmedPassword;

    @FXML
    private TextField tfCellphone;

    @FXML
    private TextField tfDirection;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfName;

    @FXML
    private Label lbMessage;

    @Override
    public void loadView(ActionEvent event, String view) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(view));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            lbMessage.setText("Error cargando la: " + view);
            e.printStackTrace();
        }
    }

    @FXML
    void btnSignupClicked(ActionEvent event) {
        String cellphoneText = tfCellphone.getText();
        String directionText = tfDirection.getText();
        String nameText = tfName.getText();
        String emailText = tfEmail.getText();
        String passwordText = pfPassword.getText();
        String confirmedPasswordText = pfConfirmedPassword.getText();

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
        loadView(event, "/view/AdminDashboard.fxml");
    }

}
