package co.edu.uniquindio.banco.bancouq.viewController;

import java.math.BigDecimal;

import co.edu.uniquindio.banco.bancouq.controller.BancoController;
import co.edu.uniquindio.banco.bancouq.model.Account;
import co.edu.uniquindio.banco.bancouq.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class TransferenceDashboardView {
    @FXML
    private ComboBox<Account> sourceAccountComboBox;
    @FXML
    private ComboBox<Account> destinationAccountComboBox;
    @FXML
    private TextField transferAmountField;
    @FXML
    private Label statusLabel;

    private final BancoController bancoController;

    public TransferenceDashboardView() {
        this.bancoController = BancoController.getInstance();
    }

    @FXML
    public void initialize() {
        User currentUser = getCurrentUser();
        sourceAccountComboBox.getItems().setAll(bancoController.getAccountService().getAccountsByUser(currentUser));
        destinationAccountComboBox.getItems().setAll(bancoController.getAccountService().getAllAccounts());
    }

    @FXML
    public void handleTransfer(ActionEvent event) {
        try {
            Account sourceAccount = sourceAccountComboBox.getValue();
            Account destinationAccount = destinationAccountComboBox.getValue();
            BigDecimal transferAmount = new BigDecimal(transferAmountField.getText());

            if (sourceAccount != null && destinationAccount != null && transferAmount.compareTo(BigDecimal.ZERO) > 0 && sourceAccount.getBalance().compareTo(transferAmount) >= 0) {
                sourceAccount.setBalance(sourceAccount.getBalance().subtract(transferAmount));
                destinationAccount.setBalance(destinationAccount.getBalance().add(transferAmount));
                bancoController.getAccountService().updateAccount(getCurrentUser(), sourceAccount);
                bancoController.getAccountService().updateAccount(getCurrentUser(), destinationAccount);
                statusLabel.setText("Transfer successful!");
            } else {
                statusLabel.setText("Invalid accounts or amount.");
            }
        } catch (Exception e) {
            statusLabel.setText("Failed to transfer: " + e.getMessage());
        }
    }

    private User getCurrentUser() {
        // Placeholder method to retrieve the logged-in user
        return new User.UserBuilder().setUserId(1).setName("John Doe").build();
    }
}
