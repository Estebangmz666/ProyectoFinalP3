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

public class LoginController {

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

    @FXML
    void btnLoginClicked(ActionEvent event) {
        String emailText = tfEmail.getText();
        String passwordText = pfPassword.getText();

        if (UserService.isValidUser(emailText, passwordText)){
            UserService.textUtil(emailText, passwordText);
            try {
                Parent root = FXMLLoader.load(getClass().getResource("com/example/view/UserDashboard.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            lbMessage.setText("Correo o contraseña invalidos.");
        }

    }

    @FXML
    void hlSignupClicked(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Signup.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}