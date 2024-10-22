package com.example.controller;

import java.text.DecimalFormat;

import com.example.model.Account;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AccountManagementController {

    @FXML
    private Label lblAccountId;

    @FXML
    private Label lblAccountNumber;

    @FXML
    private Label lblAccountType;

    @FXML
    private Label lblSaldo;

    private Account account;

    public void setAccount(Account account) {
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

    @FXML
    void btnBackClicked() {
    }
}
