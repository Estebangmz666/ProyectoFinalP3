package com.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UserDashboardController {

    @FXML
    private Label lbWelcomeMessage;

    @FXML
    private Label lbMessage;

    @FXML
    private Button btnLogout;

    @FXML
    void initialize() {
        // Aquí podrías establecer el nombre del usuario si tienes un método para obtenerlo
        String userName = "Usuario"; // Reemplaza esto con la lógica real
        lbWelcomeMessage.setText("¡Hola, " + userName + "!");
    }

    @FXML
    void btnViewProfileClicked(ActionEvent event) {
        // Lógica para ver perfil del usuario
        lbMessage.setText("Ver perfil clickeado.");
    }

    @FXML
    void btnManageAccountsClicked(ActionEvent event) {
        // Lógica para gestionar cuentas
        lbMessage.setText("Gestionar cuentas clickeado.");
    }

    @FXML
    void btnLogoutClicked(ActionEvent event) {
        // Lógica para cerrar sesión
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        stage.close(); // Cierra la ventana actual
        // Aquí podrías redirigir a la pantalla de inicio de sesión
    }
}
