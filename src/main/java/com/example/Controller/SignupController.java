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
    void btnSignupClicked(ActionEvent event) {
        String cellphoneText = tfCellphone.getText();
        String directionText = tfDirection.getText();
        String nameText = tfName.getText();
        String emailText = tfEmail.getText();
        String passwordText = tfPassword.getText();
        String confirmedPasswordText = tfConfirmPassword.getText();

        try {
            if (nameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty() || confirmedPasswordText.isEmpty() || cellphoneText.isEmpty()) {
                throw new EmptyFieldException("Por favor, completa todos los campos!");
            }

            if (!UserService.verifyPassword(passwordText, confirmedPasswordText)) {
                throw new PasswordMismatchException("Las contraseñas no coinciden!");
            }

            if (UserService.emailAlreadyExists(emailText)) {
                throw new UserAlreadyExistsException("El correo electrónico ya está en uso!");
            }

            if (!UserService.verifyCellphone(cellphoneText)) {
                throw new InvalidPhoneNumberException("Número de celular no válido!");
            }

            if (!UserService.verifyEmailDomain(emailText)) {
                throw new InvalidEmailDomainException("Dominio de correo electrónico no válido!");
            }

            UserService.addUser(nameText, emailText, passwordText, directionText, cellphoneText);
            UserService.logToFile("INFO", "Usuario " + nameText + " registrado exitosamente.");
            loadView(event, "/view/Login.fxml");

        } catch (EmptyFieldException | PasswordMismatchException | UserAlreadyExistsException |
                 InvalidPhoneNumberException | InvalidEmailDomainException e) {
            lbMessage.setText(e.getMessage());
            UserService.logToFile("WARNING", e.getMessage());
        }
    }

    @FXML
    void hlGoToLoginClicked(ActionEvent event) {
        loadView(event, "/view/Login.fxml");
        UserService.logToFile("INFO", "Usuario fue a Login.");
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
}