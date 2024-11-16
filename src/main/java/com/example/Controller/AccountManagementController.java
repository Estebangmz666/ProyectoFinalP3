package com.example.controller;

import java.text.DecimalFormat;

import com.example.model.Account;
import com.example.model.ViewLoader;
import com.example.service.UserService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AccountManagementController implements ViewLoader {

    @FXML
    private Button btnGoToDeposit;

    @FXML
    private Button btnGotoTransactions;

    @FXML
    private Button btnGoToWithdraw;

    @FXML
    private Label lblAccountId;

    @FXML
    private Label lblAccountNumber;

    @FXML
    private Label lblAccountType;

    @FXML
    private Label lblSaldo;

    private Account account;

    @Override
    public void loadView(ActionEvent event, String view) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(view));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            UserService.logToFile("INFO", "Usuario " + UserService.getCurrentUser().getName() + " cambió la vista a " + view);
        } catch (Exception e) {
            UserService.logToFile("ERROR", "Error al cargar la vista " + view + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void btnBackClicked(ActionEvent event) {
        UserService.logToFile("INFO", "Usuario " + UserService.getCurrentUser().getName() + " regresó al UserDashboard.");
        loadView(event, "/view/UserDashboard.fxml");
    }

    @FXML
    void btnGoToDepositClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DepositDashboard.fxml"));
            Parent root = loader.load();

            DepositDashboardController controller = loader.getController();
            if (controller != null && account != null) {
                controller.setAccount(account);
                UserService.logToFile("INFO", "Usuario " + UserService.getCurrentUser().getName() + " abrió la vista de depósito para la cuenta " + account.getAccountNumber());
            } else {
                UserService.logToFile("ERROR", "No se pudo pasar la cuenta a DepositDashboardController.");
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            UserService.logToFile("ERROR", "Error al cargar la vista de depósito: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void btnGoToTransferenceClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TransferenceDashboard.fxml"));
            Parent root = loader.load();

            TransferenceDashboardController controller = loader.getController();
            if (controller != null && account != null) {
                controller.setAccount(account);
                UserService.logToFile("INFO", "Usuario " + UserService.getCurrentUser().getName() + " abrió la vista de transferencia para la cuenta " + account.getAccountNumber());
            } else {
                UserService.logToFile("ERROR", "No se pudo pasar la cuenta a TransferenceDashboardController.");
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            UserService.logToFile("ERROR", "Error al cargar la vista de transferencia: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void btnGoToWithdrawClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/WithdrawDashboard.fxml"));
            Parent root = loader.load();

            WithdrawDashboardController controller = loader.getController();
            if (controller != null && account != null) {
                controller.setAccount(account);
                UserService.logToFile("INFO", "Usuario " + UserService.getCurrentUser().getName() + " abrió la vista de retiro para la cuenta " + account.getAccountNumber());
            } else {
                UserService.logToFile("ERROR", "No se pudo pasar la cuenta a WithdrawDashboardController.");
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            UserService.logToFile("ERROR", "Error al cargar la vista de retiro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setAccount(Account account) {
        this.account = account;
        displayAccountDetails();
        UserService.logToFile("INFO", "Usuario " + UserService.getCurrentUser().getName() + " seleccionó la cuenta " + account.getAccountNumber());
    }

    private void displayAccountDetails() {
        if (account != null) {
            lblAccountId.setText("ID: " + account.getAccountId());
            lblAccountNumber.setText("Número: " + account.getAccountNumber());
            lblAccountType.setText("Tipo: " + account.getAccountType());

            DecimalFormat df = new DecimalFormat("#,##0.00");
            lblSaldo.setText("Saldo: $" + df.format(account.getBalance()));
        }
    }
}