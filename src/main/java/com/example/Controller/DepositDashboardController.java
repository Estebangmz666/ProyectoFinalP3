package com.example.controller;

import java.math.BigDecimal;

import com.example.model.Account;
import com.example.model.ViewLoader;
import com.example.service.TransactionService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DepositDashboardController implements ViewLoader{

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
    private Button btnDeposit;

    @FXML
    private Label lbMessage;

    @FXML
    private TextField tfAmountToDeposit;

    Account account;

    @FXML
    void btnCancelClicked(ActionEvent event){
        loadView(event, "/view/UserDashboard.fxml");
    }

    @FXML
    void btnDepositClicked(ActionEvent event){
        String amountText = tfAmountToDeposit.getText();
        if (amountText.matches("\\d+(\\.\\d{1,2})?")){
            BigDecimal amount = new BigDecimal(amountText);
            if (TransactionService.isPositiveAmount(amount)) {
                account.setBalance(account.getBalance().add(amount));
                loadView(event, "/view/UserDashboard.fxml");
            } else {
                lbMessage.setText("La cantidad debe ser positiva o diferente de 0.");
            }
        } else {
            lbMessage.setText("Porfavor, ingrese una cantidad valida.");
        }
    }
    
    public void setAccount(Account account){
        this.account = account;
    }
}