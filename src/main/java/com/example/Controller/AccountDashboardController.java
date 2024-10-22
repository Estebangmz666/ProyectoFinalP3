package com.example.controller;

import java.math.BigDecimal;

import com.example.model.Account;
import com.example.model.AccountType;
import com.example.model.ViewLoader;
import com.example.service.AccountService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.PauseTransition;

public class AccountDashboardController implements ViewLoader {

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
    private TextField tfAccountNumber;

    @FXML
    private ComboBox<String> cbAccountType;

    @FXML
    private Label lblMessage;

    @FXML
    public void initialize() {
        ObservableList<String> accountTypes = FXCollections.observableArrayList("Corriente", "Ahorros");
        cbAccountType.setItems(accountTypes);
    }

    @FXML
    void btnCreateAccountClicked(ActionEvent event) {
        String accountNumber = tfAccountNumber.getText();
        String accountTypeStr = cbAccountType.getValue();

        if (accountNumber.isEmpty() || accountTypeStr == null) {
            lblMessage.setText("Por favor, complete todos los campos.");
            return;
        }

        if (!accountNumber.matches("\\d{10,16}")) {
            lblMessage.setText("El número de cuenta debe tener entre 10 y 16 dígitos.");
            return;
        }

        AccountType accountType = accountTypeStr.equals("Corriente") ? AccountType.CORRIENTE : AccountType.AHORROS;

        Account newAccount = new Account(AccountService.getNextAccountId(), accountNumber, accountType, BigDecimal.ZERO);

        AccountService.addAccount(newAccount);

        lblMessage.setText("Cuenta creada exitosamente. Redirigiendo...");
        lblMessage.setStyle("-fx-text-fill: green;");

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> loadView(event, "/view/UserDashboard.fxml"));
        pause.play();

        tfAccountNumber.clear();
        cbAccountType.setValue(null);
    }

    @FXML
    void btnCancelClicked(ActionEvent event) {
        tfAccountNumber.clear();
        cbAccountType.setValue(null);
        lblMessage.setText("");
    }

}
