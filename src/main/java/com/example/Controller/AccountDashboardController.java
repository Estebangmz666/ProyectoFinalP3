package com.example.controller;

import java.math.BigDecimal;

import com.example.model.Account;
import com.example.model.AccountType;
import com.example.model.ViewLoader;
import com.example.service.AccountService;
import com.example.service.UserService;
import com.example.exception.EmptyFieldException;
import com.example.exception.InvalidAccountNumberException;
import com.example.exception.AccountAlreadyExistsException;
import com.example.exception.InvalidUserInputException;

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

    @FXML
    private TextField tfAccountNumber;

    @FXML
    private ComboBox<String> cbAccountType;

    @FXML
    private Label lblMessage;

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
    public void initialize() {
        ObservableList<String> accountTypes = FXCollections.observableArrayList("Corriente", "Ahorros");
        cbAccountType.setItems(accountTypes);
    }

    @FXML
    void btnCreateAccountClicked(ActionEvent event) {
        String accountNumber = tfAccountNumber.getText();
        String accountTypeStr = cbAccountType.getValue();

        try {
            // Verificar que no haya campos vacíos
            if (accountNumber.isEmpty() || accountTypeStr == null) {
                throw new EmptyFieldException("Por favor, complete todos los campos.");
            }

            // Verificar que el número de cuenta tenga entre 10 y 16 dígitos
            if (!accountNumber.matches("\\d{10,16}")) {
                throw new InvalidAccountNumberException("El número de cuenta debe tener entre 10 y 16 dígitos.");
            }

            // Verificar que la entrada sea un número válido
            try {
                Long.parseLong(accountNumber);  // Intentamos convertir el número de cuenta a Long
            } catch (NumberFormatException e) {
                throw new InvalidUserInputException("El número de cuenta debe contener solo dígitos.");
            }

            // Verificar si la cuenta ya existe
            if (AccountService.doesAccountExist(accountNumber)) {
                throw new AccountAlreadyExistsException("La cuenta con el número " + accountNumber + " ya existe.");
            }

            // Si todo está bien, crear la cuenta
            AccountType accountType = accountTypeStr.equals("Corriente") ? AccountType.CORRIENTE : AccountType.AHORROS;
            Account newAccount = new Account(AccountService.getNextAccountId(), accountNumber, accountType, BigDecimal.ZERO);

            // Agregar la cuenta y serializarla
            AccountService.addAccount(newAccount);
            AccountService.serializeAccountsToTxt(newAccount, UserService.getCurrentUser());

            // Mensaje de éxito
            lblMessage.setText("Cuenta creada exitosamente. Redirigiendo...");
            lblMessage.setStyle("-fx-text-fill: green;");

            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e -> loadView(event, "/view/UserDashboard.fxml"));
            pause.play();

            tfAccountNumber.clear();
            cbAccountType.setValue(null);

        } catch (EmptyFieldException | InvalidAccountNumberException | AccountAlreadyExistsException | InvalidUserInputException e) {
            lblMessage.setText(e.getMessage());
        }
    }

    @FXML
    void btnCancelClicked(ActionEvent event) {
        tfAccountNumber.clear();
        cbAccountType.setValue(null);
        lblMessage.setText("");
        loadView(event, "/view/UserDashboard.fxml");
    }
}
