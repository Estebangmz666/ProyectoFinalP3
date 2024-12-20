package com.example.controller;

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
import javafx.scene.control.Hyperlink;

import com.example.exception.*;
import com.example.service.AuthService;
import com.example.service.UserService;
import com.example.util.LogToFile;
import com.example.util.ViewLoader;
import com.example.model.User;

public class LoginController implements ViewLoader {

    @FXML
    private Button btnLogin;

    @FXML
    private Hyperlink hlSignup;

    @FXML
    private Label lbMessage;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private TextField tfEmail;

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
            lbMessage.setText("Error cargando la vista: " + view);
            LogToFile.logToFile("ERROR", "Error cargando la vista " + view + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void btnLoginClicked(ActionEvent event) {
        String emailText = tfEmail.getText();
        String passwordText = pfPassword.getText();

        try {
            if (emailText.isEmpty() || passwordText.isEmpty()) {
                LogToFile.logToFile("WARNING", "Intento de inicio de sesión con campos vacíos.");
                throw new EmptyFieldException("Por favor, llena todos los campos!");
            }

            if (AuthService.isAdmin(emailText, passwordText)) {
                loadView(event, "/view/AdminDashboard.fxml");
                LogToFile.logToFile("INFO", "Admin ha iniciado sesión.");
            } else if (AuthService.isValidEmail(emailText, passwordText)) {
                User user = UserService.searchByEmailAndSetCurrentUser(emailText);
                if (user != null) {
                    loadView(event, "/view/UserDashboard.fxml");
                    LogToFile.logToFile("INFO", "Usuario " + emailText + " ha iniciado sesión.");
                } else {
                    lbMessage.setText("Error al encontrar el usuario con el correo proporcionado.");
                    LogToFile.logToFile("SEVERE", "No se pudo encontrar el usuario con el correo: " + emailText);
                }
            } else {
                lbMessage.setText("Nombre o Contraseña Invalidos!");
                LogToFile.logToFile("SEVERE", "Intento fallido de inicio de sesión con credenciales inválidas para " + emailText);
            }
        } catch (EmptyFieldException e) {
            lbMessage.setText(e.getMessage());
            LogToFile.logToFile("WARNING", e.getMessage());
        }
    }

    @FXML
    void hlSignupClicked(ActionEvent event) {
        loadView(event, "/view/Signup.fxml");
        LogToFile.logToFile("INFO", "Usuario fue a registro.");
    }
}