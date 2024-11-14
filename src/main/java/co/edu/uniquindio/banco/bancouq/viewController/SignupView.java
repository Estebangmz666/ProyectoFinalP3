package co.edu.uniquindio.banco.bancouq.viewController;

import co.edu.uniquindio.banco.bancouq.controller.BancoController;
import co.edu.uniquindio.banco.bancouq.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SignupView {
    @FXML
    private TextField userNameField;
    @FXML
    private TextField userEmailField;
    @FXML
    private TextField userPasswordField;
    @FXML
    private Label statusLabel;

    private final BancoController bancoController;

    public SignupView() {
        this.bancoController = BancoController.getInstance();
    }

    @FXML
    public void handleSignup(ActionEvent event) {
        try {
            String name = userNameField.getText();
            String email = userEmailField.getText();
            String password = userPasswordField.getText();

            User newUser = new User.UserBuilder()
                    .setName(name)
                    .setEmail(email)
                    .setPassword(password)
                    .build();

            bancoController.getUserService().addUser(newUser);
            statusLabel.setText("Signup successful! You can now log in.");
        } catch (Exception e) {
            statusLabel.setText("Failed to sign up: " + e.getMessage());
        }
    }
}
