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

import com.example.service.UserService;
import com.example.model.ViewLoader;

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
        } catch (Exception e) {
            lbMessage.setText("Error cargando la vista: " + view);
            e.printStackTrace();
        }
    }

    @FXML
    void btnLoginClicked(ActionEvent event) {
        String emailText = tfEmail.getText();
        String passwordText = pfPassword.getText();

        if (emailText.isEmpty() || passwordText.isEmpty()) {
            lbMessage.setText("Por favor, llena todos los campos!");
            return;
        }

        if (UserService.isAdmin(emailText, passwordText)) {
            loadView(event, "/view/AdminDashboard.fxml");
            UserService.logToFile("INFO", "Admin ha iniciado sesión");
        } else if (UserService.isValidEmail(emailText, passwordText)) {
            loadView(event, "/view/UserDashboard.fxml");
            UserService.logToFile("INFO", "Usuario " + emailText + " ha iniciado sesión.");
        } else {
            lbMessage.setText("Nombre o Contraseña Invalidos!");
            UserService.logToFile("WARNING", "Intento fallido de inicio de sesión para el usuaio" + emailText);
        }
    }

    @FXML
    void hlSignupClicked(ActionEvent event) {
        loadView(event, "/view/Signup.fxml");
    }
}
