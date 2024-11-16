package com.example.controller;

import java.math.BigDecimal;

import com.example.model.Account;
import com.example.model.ViewLoader;
import com.example.service.TransactionService;
import com.example.service.UserService;

import javafx.animation.PauseTransition;
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
import javafx.util.Duration;

public class WithdrawDashboardController implements ViewLoader {

    private Account account;

    public void setAccount(Account account) {
        this.account = account;
    }

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnWithdraw;

    @FXML
    private Label lbMessage;

    @FXML
    private TextField tfAmountToWithdraw;

    @FXML
    void btnCancelClicked(ActionEvent event) {
        loadView(event, "/view/UserDashboard.fxml");
        UserService.logToFile("INFO", "Usuario canceló la operación de retiro y volvió al panel principal.");
    }

    @FXML
    void btnWithdrawClicked(ActionEvent event) {
        if (account == null) {
            lbMessage.setText("Error: No se ha asignado una cuenta para el retiro.");
            UserService.logToFile("SEVERE", "Error en el retiro: No se ha asignado una cuenta.");
            return;
        }

        String amountText = tfAmountToWithdraw.getText();
        if (!amountText.matches("\\d+(\\.\\d{1,2})?")) {
            lbMessage.setText("Por favor, ingrese una cantidad válida.");
            UserService.logToFile("WARNING", "Usuario ingresó una cantidad no válida para el retiro.");
            return;
        }

        BigDecimal amount = new BigDecimal(amountText);
        if (!TransactionService.isPositiveAmount(amount)) {
            lbMessage.setText("La cantidad debe ser positiva y mayor que 0.");
            UserService.logToFile("WARNING", "Usuario intentó realizar un retiro con una cantidad no positiva.");
            return;
        }

        if (account.getBalance().compareTo(amount) < 0) {
            lbMessage.setText("Saldo insuficiente para realizar el retiro.");
            UserService.logToFile("WARNING", "Usuario intentó realizar un retiro con fondos insuficientes.");
            return;
        }

        boolean success = TransactionService.updateBalance(UserService.getCurrentUser(), account, account.getBalance().subtract(amount));
        if (success) {
            lbMessage.setText("Retiro realizado con éxito.");
            UserService.logToFile("INFO", "Retiro exitoso de " + amount + " de la cuenta " + account.getAccountNumber() + ".");
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e -> loadView(event, "/view/UserDashboard.fxml"));
            pause.play();
        } else {
            lbMessage.setText("Error al procesar el retiro.");
            UserService.logToFile("SEVERE", "Error al procesar el retiro en la cuenta " + account.getAccountNumber() + ".");
        }
    }

    @Override
    public void loadView(ActionEvent event, String view) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(view));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            UserService.logToFile("INFO", "Vista cargada: " + view);
        } catch (Exception e) {
            lbMessage.setText("Error al cargar la vista.");
            UserService.logToFile("SEVERE", "Error al cargar la vista: " + view + ". " + e.getMessage());
            e.printStackTrace();
        }
    }
}