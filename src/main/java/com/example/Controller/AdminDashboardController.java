package com.example.controller;

import com.example.util.LogToFile;
import com.example.util.ViewLoader;

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

public class AdminDashboardController implements ViewLoader {

    @FXML
    private Button btnCreateUser;

    @FXML
    private Button btnManageUsers;

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
            LogToFile.logToFile("INFO", "Vista cargada: " + view);
        } catch (Exception e) {
            LogToFile.logToFile("ERROR", "Error al cargar la vista " + view + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void btnCreateUserClicked(ActionEvent event) {
        loadView(event, "/view/AdminCreateUser.fxml");
        LogToFile.logToFile("INFO", "Admin fue a crear usuario.");
    }

    @FXML
    void btnManageUsersClicked(ActionEvent event) {
        loadView(event, "/view/AdminManageUsers.fxml");
        LogToFile.logToFile("INFO", "Admin fue al panel de administración de usuarios.");
    }

    @FXML
    void hlLogoutClicked(ActionEvent event) {
        LogToFile.logToFile("INFO", "Admin cerró el programa.");
        Platform.exit();
    }
}
