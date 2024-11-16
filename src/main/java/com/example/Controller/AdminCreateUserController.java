package com.example.controller;

import com.example.service.UserService;
import com.example.model.ViewLoader;

import com.example.exception.EmptyFieldException;
import com.example.exception.PasswordMismatchException;
import com.example.exception.UserAlreadyExistsException;
import com.example.exception.InvalidPhoneNumberException;
import com.example.exception.InvalidEmailDomainException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminCreateUserController implements ViewLoader {

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

    @FXML
    private Hyperlink hlGoToAdminDashboard;

    @Override
    public void loadView(ActionEvent event, String view) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(view));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            UserService.logToFile("INFO", "Vista cargada: " + view);
        } catch (Exception e) {
            lbMessage.setText("Error cargando la vista: " + view);
            UserService.logToFile("ERROR", "Error cargando la vista " + view + ": " + e.getMessage());
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

        try {
            if (nameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty() || confirmedPasswordText.isEmpty() || cellphoneText.isEmpty()) {
                UserService.logToFile("WARNING", "Intento de crear usuario con campos vacíos.");
                throw new EmptyFieldException("Por favor, completa todos los campos!");
            }

            if (!UserService.verifyPassword(passwordText, confirmedPasswordText)) {
                UserService.logToFile("WARNING", "Intento de crear usuario con contraseñas no coincidentes.");
                throw new PasswordMismatchException("Las contraseñas no coinciden!");
            }

            if (UserService.emailAlreadyExists(emailText)) {
                UserService.logToFile("WARNING", "Intento de crear usuario con un correo ya existente: " + emailText);
                throw new UserAlreadyExistsException("El correo electrónico ya está en uso!");
            }

            if (!UserService.verifyCellphone(cellphoneText)) {
                UserService.logToFile("WARNING", "Número de celular no válido al crear usuario.");
                throw new InvalidPhoneNumberException("Número de celular no válido!");
            }

            if (!UserService.verifyEmailDomain(emailText)) {
                UserService.logToFile("WARNING", "Dominio de correo no válido al crear usuario: " + emailText);
                throw new InvalidEmailDomainException("Dominio de correo electrónico no válido!");
            }

            UserService.addUser(nameText, emailText, passwordText, directionText, cellphoneText);
            loadView(event, "/view/AdminDashboard.fxml");
            UserService.logToFile("INFO", "Usuario " + nameText + " creado exitosamente.");

        } catch (EmptyFieldException | PasswordMismatchException | UserAlreadyExistsException | 
                 InvalidPhoneNumberException | InvalidEmailDomainException e) {
            lbMessage.setText(e.getMessage());
            UserService.logToFile("WARNING", "Error en la creación de usuario: " + e.getMessage());
        }
    }

    @FXML
    void hlGoToAdminDashboardClicked(ActionEvent event) {
        loadView(event, "/view/AdminDashboard.fxml");
        UserService.logToFile("INFO", "Admin fue al panel principal.");
    }
}