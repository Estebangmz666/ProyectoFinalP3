package com.example.controller;

import com.example.model.Account;
import com.example.model.User;
import com.example.model.ViewLoader;
import com.example.service.AccountService;
import com.example.service.TransactionService;
import com.example.service.UserService;
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
    private ComboBox<String> cbAccountToTransfer;

    @FXML
    private Label lblMessage;

    @FXML
    private TextField tfAmountToTranfer;

    @FXML
    public void initialize() {
        loadAllAccounts();
        UserService.logToFile("INFO", "Inicializando Transferencia Dashboard.");
    }

    private void loadAllAccounts() {
        List<Account> allAccounts = AccountService.getAllAccounts();
        for (Account acc : allAccounts) {
            String accountInfo = "ID: " + acc.getAccountId() + " - Número: " + acc.getAccountNumber() +
                                 " - Saldo: $" + acc.getBalance();
            cbAccountToTransfer.getItems().add(accountInfo);
        }
        UserService.logToFile("INFO", "Cuentas cargadas en ComboBox de transferencia.");
    }

    @FXML
    void btnCancelClicked(ActionEvent event) {
        loadView(event, "/view/UserDashboard.fxml");
        UserService.logToFile("INFO", "Usuario canceló la transferencia y regresó al Dashboard.");
    }

    @FXML
    void btnTransferClicked(ActionEvent event) {
        String selectedAccountStr = cbAccountToTransfer.getSelectionModel().getSelectedItem();
        String amountText = tfAmountToTranfer.getText();
        
        if (selectedAccountStr == null) {
            lblMessage.setText("Por favor, seleccione una cuenta de destino.");
            UserService.logToFile("WARNING", "Transferencia fallida: No se seleccionó una cuenta de destino.");
            return;
        }

        if (!amountText.matches("\\d+(\\.\\d{1,2})?")) {
            lblMessage.setText("Por favor, ingrese una cantidad válida.");
            UserService.logToFile("WARNING", "Transferencia fallida: Cantidad no válida ingresada.");
            return;
        }

        BigDecimal amount = new BigDecimal(amountText);
        
        String destinationAccountNumber = selectedAccountStr.split(" - ")[1].split(": ")[1];
        Account destinationAccount = AccountService.getAccountByNumber(destinationAccountNumber);
        
        if (destinationAccount == null) {
            lblMessage.setText("Cuenta de destino no encontrada.");
            UserService.logToFile("WARNING", "Transferencia fallida: Cuenta de destino no encontrada.");
            return;
        }
        
        User sourceUser = UserService.getCurrentUser();
        if (sourceUser == null) {
            lblMessage.setText("Usuario de origen no encontrado.");
            UserService.logToFile("SEVERE", "Error en transferencia: Usuario de origen no encontrado.");
            return;
        }
        
        User destinationUser = UserService.getUserByAccount(destinationAccount);

        try {
            TransactionService.transfer(sourceUser, account, destinationUser, destinationAccount, amount);
            lblMessage.setText("Transferencia realizada con éxito.");
            UserService.logToFile("INFO", "Transferencia realizada de " + account.getAccountNumber() + 
                                  " a " + destinationAccount.getAccountNumber() + " por $" + amount);
        } catch (UserNotFoundException e) {
            lblMessage.setText("Error: Usuario no encontrado.");
            UserService.logToFile("SEVERE", "Transferencia fallida: Usuario no encontrado. " + e.getMessage());
            e.printStackTrace();
        } catch (InsufficientFundsException e) {
            lblMessage.setText("Error: Fondos insuficientes para realizar la transferencia.");
            UserService.logToFile("WARNING", "Transferencia fallida: Fondos insuficientes.");
            e.printStackTrace();
        } catch (Exception e) {
            lblMessage.setText("Error inesperado en la transferencia.");
            UserService.logToFile("SEVERE", "Error inesperado en la transferencia. " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void loadView(ActionEvent event, String view) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(view));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            UserService.logToFile("INFO", "Vista cargada: " + view);
        } catch (Exception e) {
            lblMessage.setText("Error al cargar la vista.");
            UserService.logToFile("SEVERE", "Error al cargar la vista " + view + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
