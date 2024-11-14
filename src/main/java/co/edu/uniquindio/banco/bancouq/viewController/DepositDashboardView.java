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

public class DepositDashboardView {
     @FXML
    private ComboBox<Account> accountComboBox;
    @FXML
    private TextField depositAmountField;
    @FXML
    private Label statusLabel;

    private final BancoController bancoController;

    public DepositDashboardView() {
        this.bancoController = BancoController.getInstance();
    }

    @FXML
    public void initialize() {
        // Load user's accounts into the combo box
        User currentUser = getCurrentUser();
        accountComboBox.getItems().setAll(bancoController.getAccountService().getAccountsByUser(currentUser));
    }

    @FXML
    public void handleDeposit(ActionEvent event) {
        try {
            Account selectedAccount = accountComboBox.getValue();
            BigDecimal depositAmount = new BigDecimal(depositAmountField.getText());

            if (selectedAccount != null && depositAmount.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal newBalance = selectedAccount.getBalance().add(depositAmount);
                selectedAccount.setBalance(newBalance);
                bancoController.getAccountService().updateAccount(getCurrentUser(), selectedAccount);
                statusLabel.setText("Deposit successful! New balance: " + newBalance);
            } else {
                statusLabel.setText("Invalid account or amount.");
            }
        } catch (Exception e) {
            statusLabel.setText("Failed to deposit: " + e.getMessage());
        }
    }

    private User getCurrentUser() {
        // Placeholder method to retrieve the logged-in user
        // Replace this with the actual logic to get the user from session or context
        return new User.UserBuilder().setUserId(1).setName("John Doe").build();
    }
}
