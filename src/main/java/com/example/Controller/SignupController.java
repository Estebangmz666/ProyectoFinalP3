package com.example.controller;

import com.example.dao.UserDAO;
import com.example.exception.EmptyFieldException;
import com.example.exception.InvalidEmailDomainException;
import com.example.exception.InvalidPhoneNumberException;
import com.example.exception.PasswordMismatchException;
import com.example.exception.UserAlreadyExistsException;
import com.example.service.AuthService;
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
                LogToFile.logToFile("WARNING", "Usuario intentó registrarse con campos vacíos.");
                throw new EmptyFieldException("Por favor, completa todos los campos!");
            }

            if (!AuthService.verifyPassword(passwordText, confirmedPasswordText)) {
                LogToFile.logToFile("WARNING", "Usuario intentó registrarse con contraseñas que no coinciden.");
                throw new PasswordMismatchException("Las contraseñas no coinciden!");
            }

            if (AuthService.emailAlreadyExists(emailText)) {
                LogToFile.logToFile("WARNING", "Intento de registro con correo ya existente: " + emailText);
                throw new UserAlreadyExistsException("El correo electrónico ya está en uso!");
            }

            if (!AuthService.verifyCellphone(cellphoneText)) {
                LogToFile.logToFile("WARNING", "Usuario intentó registrarse con número de celular no válido.");
                throw new InvalidPhoneNumberException("Número de celular no válido!");
            }

            if (!AuthService.verifyEmailDomain(emailText)) {
                LogToFile.logToFile("WARNING", "Intento de registro con dominio de correo no válido: " + emailText);
                throw new InvalidEmailDomainException("Dominio de correo electrónico no válido!");
            }

            UserDAO.addUser(nameText, emailText, passwordText, directionText, cellphoneText);
            LogToFile.logToFile("INFO", "Usuario " + nameText + " registrado exitosamente.");
            loadView(event, "/view/Login.fxml");

        } catch (EmptyFieldException | PasswordMismatchException | UserAlreadyExistsException |
            InvalidPhoneNumberException | InvalidEmailDomainException e) {
            lbMessage.setText(e.getMessage());
            LogToFile.logToFile("WARNING", e.getMessage());
        }
    }

    @FXML
    void hlGoToLoginClicked(ActionEvent event) {
        loadView(event, "/view/Login.fxml");
        LogToFile.logToFile("INFO", "Usuario fue a Login.");
    }

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
            lbMessage.setText("Error loading view: " + view);
            LogToFile.logToFile("ERROR", "Error cargando la vista: " + view + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}