package co.edu.uniquindio.banco.bancouq.viewController;

import co.edu.uniquindio.banco.bancouq.controller.BancoController;
import co.edu.uniquindio.banco.bancouq.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AdminCreateUserView {
    @FXML
    private TextField userNameField;
    @FXML
    private TextField userEmailField;
    @FXML
    private TextField userDirectionField;
    @FXML
    private TextField userCellphoneField;
    @FXML
    private Label statusLabel;

    private final BancoController bancoController;

    public AdminCreateUserView() {
        this.bancoController = BancoController.getInstance();
    }

    @FXML
    public void handleCreateUser(ActionEvent event) {
        try {
            String name = userNameField.getText();
            String email = userEmailField.getText();
            String direction = userDirectionField.getText();
            String cellphone = userCellphoneField.getText();

            User newUser = new User.UserBuilder()
                    .setName(name)
                    .setEmail(email)
                    .setDirection(direction)
                    .setCellphone(cellphone)
                    .build();

            bancoController.getUserService().addUser(newUser);
            statusLabel.setText("User created successfully!");
        } catch (Exception e) {
            statusLabel.setText("Failed to create user: " + e.getMessage());
        }
    }
}
