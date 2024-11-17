package com.example.controller;

import com.example.model.Account;
import com.example.model.Category;
import com.example.model.User;
import com.example.model.Transaction;
import com.example.model.TransactionType;
import com.example.service.AccountService;
import com.example.service.TransactionService;
import com.example.service.UserService;
import com.example.util.LogToFile;
import com.example.util.ViewLoader;
import com.example.dao.UserDAO;
import com.example.exception.InsufficientFundsException;
import com.example.exception.UserNotFoundException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

public class TransferenceDashboardController implements ViewLoader {

    private Account account;

    public void setAccount(Account account) {
        this.account = account;
    }

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnTransfer;

    @FXML
    private ComboBox<String> cbCategory;

    @FXML
    private ComboBox<String> cbAccountToTransfer;

    @FXML
    private Label lbMessage;

    @FXML
    private TextField tfAmountToTranfer;

    @FXML
    private TextField tfDescription;

    @FXML
    public void initialize() {
        loadAllAccounts();
        loadCategories();
        LogToFile.logToFile("INFO", "Inicializando Transferencia Dashboard.");
    }

    private void loadCategories() {
        Category[] categories = Category.values();
        for (Category category : categories) {
            cbCategory.getItems().add(category.name());
        }
        LogToFile.logToFile("INFO", "Categorías cargadas desde el enum Category en ComboBox.");
    }

    private void loadAllAccounts() {
        List<Account> allAccounts = AccountService.getAllAccounts();
        for (Account acc : allAccounts) {
            String accountInfo = "ID: " + acc.getAccountId() + " - Número: " + acc.getAccountNumber() + " - Saldo: $" + acc.getBalance();
            cbAccountToTransfer.getItems().add(accountInfo);
        }
        LogToFile.logToFile("INFO", "Cuentas cargadas en ComboBox de transferencia.");
    }

    @FXML
    void btnCancelClicked(ActionEvent event) {
        loadView(event, "/view/UserDashboard.fxml");
        LogToFile.logToFile("INFO", "Usuario canceló la transferencia y regresó al Dashboard.");
    }

    @FXML
    void btnTransferClicked(ActionEvent event) {
        String selectedAccountStr = cbAccountToTransfer.getSelectionModel().getSelectedItem();
        String amountText = tfAmountToTranfer.getText();
        String selectedCategory = cbCategory.getSelectionModel().getSelectedItem();
        String description = tfDescription.getText();

        if (selectedAccountStr == null || selectedCategory == null || amountText.isEmpty()) {
            lbMessage.setText("Por favor, complete todos los campos.");
            LogToFile.logToFile("WARNING", "Transferencia fallida: Campos incompletos.");
            return;
        }

        if (!amountText.matches("\\d+(\\.\\d{1,2})?")) {
            lbMessage.setText("Por favor, ingrese una cantidad válida.");
            LogToFile.logToFile("WARNING", "Transferencia fallida: Cantidad no válida.");
            return;
        }

        BigDecimal amount = new BigDecimal(amountText);
        
        String destinationAccountNumber = selectedAccountStr.split(" - ")[1].split(": ")[1];
        Account destinationAccount = AccountService.getAccountByNumber(destinationAccountNumber);
        
        if (destinationAccount == null) {
            lbMessage.setText("Cuenta de destino no encontrada.");
            LogToFile.logToFile("WARNING", "Transferencia fallida: Cuenta de destino no encontrada.");
            return;
        }
        
        User sourceUser = UserService.getCurrentUser();
        if (sourceUser == null) {
            lbMessage.setText("Usuario de origen no encontrado.");
            LogToFile.logToFile("SEVERE", "Error en transferencia: Usuario de origen no encontrado.");
            return;
        }
        
        User destinationUser = UserDAO.getUserByAccount(destinationAccount);

        try {
            TransactionService.transfer(sourceUser, account, destinationUser, destinationAccount, amount);
            lbMessage.setText("Transferencia realizada con éxito.");
            LogToFile.logToFile("INFO", "Transferencia realizada de " + account.getAccountNumber() + " a " + destinationAccount.getAccountNumber() + " por $" + amount);
            TransactionService.updateUserBudgetsAfterTransaction(UserService.getCurrentUser().getUserId(), amount, TransactionType.TRANSFERENCIA);
            Transaction transaction = new Transaction(
                    generateTransactionId(),
                    LocalDateTime.now(),
                    TransactionType.TRANSFERENCIA,
                    amount,
                    description,
                    account,
                    destinationAccount,
                    selectedCategory
            );
            saveTransaction(transaction, sourceUser.getUserId());

        } catch (UserNotFoundException e) {
            lbMessage.setText("Error: Usuario no encontrado.");
            LogToFile.logToFile("SEVERE", "Transferencia fallida: Usuario no encontrado. " + e.getMessage());
        } catch (InsufficientFundsException e) {
            lbMessage.setText("Error: Fondos insuficientes.");
            LogToFile.logToFile("WARNING", "Transferencia fallida: Fondos insuficientes.");
        } catch (Exception e) {
            lbMessage.setText("Error inesperado en la transferencia.");
            LogToFile.logToFile("SEVERE", "Error inesperado en la transferencia. " + e.getMessage());
        }
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
                transaction.getDestinationAccount().getAccountNumber(),
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

    @Override
    public void loadView(ActionEvent event, String view) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(view));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            LogToFile.logToFile("INFO", "Vista cargada: " + view);
        } catch (Exception e) {
            lbMessage.setText("Error al cargar la vista.");
            LogToFile.logToFile("SEVERE", "Error al cargar la vista " + view + ": " + e.getMessage());
        }
    }
}