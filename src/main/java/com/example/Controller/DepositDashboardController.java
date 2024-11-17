package com.example.controller;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import com.example.model.Account;
import com.example.model.Transaction;
import com.example.model.TransactionType;
import com.example.service.TransactionService;
import com.example.service.UserService;
import com.example.util.LogToFile;
import com.example.util.ViewLoader;

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

public class DepositDashboardController implements ViewLoader {

    @Override
    public void loadView(ActionEvent event, String view) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(view));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            LogToFile.logToFile("INFO", "Vista cargada: " + view);
        } catch (Exception e) {
            lbMessage.setText("Error al cargar la vista.");
            LogToFile.logToFile("ERROR", "Error al cargar la vista " + view + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private Button btnDeposit;

    @FXML
    private Label lbMessage;

    @FXML
    private TextField tfAmountToDeposit;

    private Account account;

    @FXML
    void btnCancelClicked(ActionEvent event) {
        loadView(event, "/view/UserDashboard.fxml");
        LogToFile.logToFile("INFO", "Usuario canceló el depósito y regresó al panel principal.");
    }

    @FXML
    void btnDepositClicked(ActionEvent event) {
        if (account == null) {
            lbMessage.setText("Error: No se ha asignado una cuenta para el depósito.");
            LogToFile.logToFile("WARNING", "Intento de depósito sin cuenta asignada.");
            return;
        }

        String amountText = tfAmountToDeposit.getText();
        if (!amountText.matches("\\d+(\\.\\d{1,2})?")) {
            lbMessage.setText("Por favor, ingrese una cantidad válida.");
            LogToFile.logToFile("WARNING", "Cantidad inválida ingresada para depósito: " + amountText);
            return;
        }

        BigDecimal amount = new BigDecimal(amountText);
        if (!TransactionService.isPositiveAmount(amount)) {
            lbMessage.setText("La cantidad debe ser positiva o diferente de 0.");
            LogToFile.logToFile("WARNING", "Cantidad de depósito no positiva ingresada: " + amountText);
            return;
        }

        boolean success = TransactionService.updateBalance(UserService.getCurrentUser(), account, amount);
        if (success) {
            lbMessage.setText("Depósito correcto.");
            LogToFile.logToFile("INFO", "Depósito exitoso de " + amount + " en la cuenta " + account.getAccountNumber());
            
            Transaction transaction = new Transaction(
                generateTransactionId(),
                LocalDateTime.now(),
                TransactionType.DEPOSITO,
                amount,
                "Depósito en cuenta",
                account,
                null,
                "DEPÓSITO"
            );
            saveTransaction(transaction, UserService.getCurrentUser().getUserId());

            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e -> loadView(event, "/view/UserDashboard.fxml"));
            pause.play();
        } else {
            lbMessage.setText("Error al procesar el depósito.");
            LogToFile.logToFile("ERROR", "Error al procesar el depósito para la cuenta " + account.getAccountNumber());
        }
    }

    public void setAccount(Account account) {
        this.account = account;
        LogToFile.logToFile("INFO", "Cuenta asignada para depósito: " + account.getAccountNumber());
    }

    private void saveTransaction(Transaction transaction, int userId) {
        String userIdStr = String.valueOf(userId);
        String transactionData = String.join("@@",
                transaction.getTransactionId(),
                transaction.getDate().toString(),
                transaction.getTransactionType().toString(),
                transaction.getAmount().toString(),
                transaction.getDescription(),
                transaction.getSourceAccount().getAccountNumber(),
                transaction.getDestinationAccount() != null ? transaction.getDestinationAccount().getAccountNumber() : "N/A",
                transaction.getCategory());
        try {
            String filePath = UserService.getTransactionBasePath() + "User" + userIdStr + "_transactions.txt";
            Files.write(Paths.get(filePath), (transactionData + System.lineSeparator()).getBytes(), java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND);
            LogToFile.logToFile("INFO", "Transacción serializada y guardada en: " + filePath);
        } catch (Exception e) {
            LogToFile.logToFile("SEVERE", "Error al guardar la transacción: " + e.getMessage());
        }
    }

    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis();
    }
}