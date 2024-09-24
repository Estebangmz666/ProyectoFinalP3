package com.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import com.example.service.UserService;

public class SignupController {

    @FXML
    private Button btnSignup;

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
    void btnSignupClicked(ActionEvent event) {
        String cellphoneText = tfCellphone.getText();
        String directionText = tfDirection.getText();
        String nameText = tfName.getText();
        String emailText = tfEmail.getText();
        String passwordText = tfPassword.getText();
        String confirmedPasswordText = tfConfirmPassword.getText();

        if (UserService.verifyPassword(passwordText, confirmedPasswordText) && UserService.userAlreadyExists(emailText)){
            UserService.addUser(nameText, emailText, directionText, cellphoneText);
            try {
            Parent root = FXMLLoader.load(getClass().getResource("com/example/view/UserDashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            } catch (Exception e) {
            e.printStackTrace();
            }
        }
    }

}
