package co.edu.uniquindio.banco.bancouq.viewController;

import co.edu.uniquindio.banco.bancouq.controller.BancoController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdminDashboardView {
     @FXML
    private Label welcomeLabel;
    private final BancoController bancoController;

    public AdminDashboardView() {
        this.bancoController = BancoController.getInstance();
    }

    @FXML
    public void initialize() {
        // Set welcome message or initialize other UI components
        welcomeLabel.setText("Welcome to Admin Dashboard!");
    }

    @FXML
    public void handleManageUsers(ActionEvent event) {
        // Logic to load manage users view or perform related actions
    }

    @FXML
    public void handleViewReports(ActionEvent event) {
        // Logic to view reports or analytics
    }
}
