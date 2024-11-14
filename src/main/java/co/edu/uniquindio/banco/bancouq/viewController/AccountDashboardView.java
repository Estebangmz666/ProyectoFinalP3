package co.edu.uniquindio.banco.bancouq.viewController;

import java.math.BigDecimal;

import co.edu.uniquindio.banco.bancouq.controller.BancoController;
import co.edu.uniquindio.banco.bancouq.model.Account;
import co.edu.uniquindio.banco.bancouq.model.AccountType;
import co.edu.uniquindio.banco.bancouq.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AccountDashboardView {
    @FXML
    private TextField accountNumberField;
    @FXML
    private ComboBox<AccountType> accountTypeComboBox;
    @FXML
    private TextField balanceField;
    @FXML
    private Label statusLabel;

    private final BancoController bancoController;

    public AccountDashboardView() {
        this.bancoController = BancoController.getInstance();
    }

    @FXML
    public void initialize() {
        // Initialize account type combo box, for example with available account types
        accountTypeComboBox.getItems().setAll(AccountType.values());
    }

    @FXML
    public void handleCreateAccount(ActionEvent event) {
        try {
            String accountNumber = accountNumberField.getText();
            AccountType accountType = accountTypeComboBox.getValue();
            BigDecimal balance = new BigDecimal(balanceField.getText());

            User currentUser = getCurrentUser(); // Assume this method retrieves the logged-in user
            bancoController.getAccountService().createAccount(currentUser, 
                new Account.AccountBuilder()
                    .setAccountNumber(accountNumber)
                    .setAccountType(accountType)
                    .setBalance(balance)
                    .build()
            );

            statusLabel.setText("Account created successfully!");
        } catch (Exception e) {
            statusLabel.setText("Failed to create account: " + e.getMessage());
        }
    }

    private User getCurrentUser() {
        // Placeholder method to retrieve the logged-in user
        // Replace this with the actual logic to get the user from session or context
        return new User.UserBuilder().setUserId(1).setName("John Doe").build();
    }
}
