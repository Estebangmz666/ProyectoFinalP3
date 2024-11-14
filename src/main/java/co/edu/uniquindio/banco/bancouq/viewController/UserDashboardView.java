package co.edu.uniquindio.banco.bancouq.viewController;

import co.edu.uniquindio.banco.bancouq.controller.BancoController;
import co.edu.uniquindio.banco.bancouq.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserDashboardView {
    @FXML
    private Label welcomeLabel;
    private final BancoController bancoController;
    private User currentUser;

    public UserDashboardView() {
        this.bancoController = BancoController.getInstance();
    }

    @FXML
    public void initialize() {
        currentUser = getCurrentUser();
        welcomeLabel.setText("Welcome, " + currentUser.getName() + "!");
    }

    @FXML
    public void handleViewAccounts(ActionEvent event) {
        // Logic to load the user's account information
    }

    @FXML
    public void handleTransferFunds(ActionEvent event) {
        // Logic to navigate to transfer funds view
    }

    private User getCurrentUser() {
        // Placeholder method to retrieve the logged-in user
        return new User.UserBuilder().setUserId(1).setName("John Doe").build();
    }
}
