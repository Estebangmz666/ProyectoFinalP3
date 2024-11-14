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

public class WithdrawDashboardView {
     @FXML
    private ComboBox<Account> accountComboBox;
    @FXML
    private TextField withdrawAmountField;
    @FXML
    private Label statusLabel;

    private final BancoController bancoController;

    public WithdrawDashboardView() {
        this.bancoController = BancoController.getInstance();
    }

    @FXML
    public void initialize() {
        User currentUser = getCurrentUser();
        accountComboBox.getItems().setAll(bancoController.getAccountService().getAccountsByUser(currentUser));
    }

    @FXML
    public void handleWithdraw(ActionEvent event) {
        try {
            Account selectedAccount = accountComboBox.getValue();
            BigDecimal withdrawAmount = new BigDecimal(withdrawAmountField.getText());

            if (selectedAccount != null && withdrawAmount.compareTo(BigDecimal.ZERO) > 0 && selectedAccount.getBalance().compareTo(withdrawAmount) >= 0) {
                BigDecimal newBalance = selectedAccount.getBalance().subtract(withdrawAmount);
                selectedAccount.setBalance(newBalance);
                bancoController.getAccountService().updateAccount(getCurrentUser(), selectedAccount);
                statusLabel.setText("Withdrawal successful! New balance: " + newBalance);
            } else {
                statusLabel.setText("Invalid account or insufficient balance.");
            }
        } catch (Exception e) {
            statusLabel.setText("Failed to withdraw: " + e.getMessage());
        }
    }

    private User getCurrentUser() {
        // Placeholder method to retrieve the logged-in user
        return new User.UserBuilder().setUserId(1).setName("John Doe").build();
    }
}
