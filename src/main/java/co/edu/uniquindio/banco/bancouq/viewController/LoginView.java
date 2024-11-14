package co.edu.uniquindio.banco.bancouq.viewController;

import co.edu.uniquindio.banco.bancouq.controller.BancoController;
import co.edu.uniquindio.banco.bancouq.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginView {
      @FXML
    private TextField userIdField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label statusLabel;

    private final BancoController bancoController;

    public LoginView() {
        this.bancoController = BancoController.getInstance();
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        try {
            String userId = userIdField.getText();
            String password = passwordField.getText();

            User user = bancoController.getUserService().findUserById(userId);
            if (user != null && password.equals("password123")) { // Placeholder password check
                statusLabel.setText("Login successful! Welcome, " + user.getName());
            } else {
                statusLabel.setText("Invalid user ID or password.");
            }
        } catch (Exception e) {
            statusLabel.setText("Failed to login: " + e.getMessage());
        }
    }
}
