package com.example.controller;

import java.io.IOException;
import java.text.DecimalFormat;

import com.example.model.Account;
import com.example.model.ViewLoader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AccountManagementController implements ViewLoader{

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

    @FXML
    void btnBackClicked(ActionEvent event) {
        loadView(event, "/view/UserDashboard.fxml");
    }

    @FXML
    void btnGoToDepositClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DepositDashboard.fxml"));
            Parent root = loader.load();
            
            DepositDashboardController depositController = loader.getController();
            depositController.setAccount(account);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnGoToTransferenceClicked(ActionEvent event) {
        try {FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TranferenceDashboard.fxml"));
            Parent root = loader.load();

            TransferenceDashboardController transactionController = loader.getController();
            transactionController.setAccount(account);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void btnGoToWithdrawClicked(ActionEvent event) {
        try {FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/WithdrawDashboard.fxml"));
            Parent root = loader.load();

            WithdrawDashboardController withdrawController = loader.getController();
            withdrawController.setAccount(account);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setAccount(Account account){
        this.account = account;
        displayAccountDetails();
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