package com.example.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

import com.example.model.ViewLoader;

public class AdminDahsboardController implements ViewLoader{

    @Override
    public void loadView(ActionEvent event, String view) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(view));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private Button btnCreateUser;

    @FXML
    private Button btnManageUsers;

    @FXML
    private Hyperlink hlLogout;

    @FXML
    void btnCreateUserClicked(ActionEvent event) {
        loadView(event, "/view/AdminCreateUser.fxml");
    }

    @FXML
    void btnManageUsersClicked(ActionEvent event) {
        loadView(event, "/view/AdminManageUsers.fxml");
    }

    @FXML
    void hlLogoutClicked(ActionEvent event) {
        Platform.exit();
    }

}
