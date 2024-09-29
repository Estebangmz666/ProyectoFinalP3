package com.example.controller;

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

public class AdminDashboardController implements ViewLoader {

    @FXML
    private Button btnCreateUser;

    @FXML
    private Button btnDeleteUser;

    @FXML
    private Button btnFindUser;

    @FXML
    private Button btnUpdateUser;

    @FXML
    private Hyperlink hlLogout;

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
    void btnCreateUserClicked(ActionEvent event) {
        loadView(event, "/view/CreateUser.fxml");
    }

    @FXML
    void btnDeleteUserClicked(ActionEvent event) {
        loadView(event, "/view/DeleteUser.fxml");
    }

    @FXML
    void btnFindUserClicked(ActionEvent event) {
        loadView(event, "/view/FindUser.fxml");
    }

    @FXML
    void btnUpdateUserClicked(ActionEvent event) {
        loadView(event, "/view/UpdateUser.fxml");
    }

    @FXML
    void hlLogoutClicked(ActionEvent event) {
        loadView(event, "/view/Login.fxml");
    }
}
