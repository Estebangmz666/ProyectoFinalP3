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
            UserService.logToFile("ERROR", "Error al cargar la vista " + view + ": " + e.getMessage());
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
            if (accountNumber.isEmpty() || accountTypeStr == null) {
                UserService.logToFile("WARNING", "Usuario " + UserService.getCurrentUser().getName() + " intentó crear una cuenta con campos vacíos.");
                throw new EmptyFieldException("Por favor, complete todos los campos.");
            }

            if (!accountNumber.matches("\\d{10,16}")) {
                UserService.logToFile("WARNING", "Usuario " + UserService.getCurrentUser().getName() + " ingresó un número de cuenta inválido.");
                throw new InvalidAccountNumberException("El número de cuenta debe tener entre 10 y 16 dígitos.");
            }

            try {
                Long.parseLong(accountNumber);
            } catch (NumberFormatException e) {
                UserService.logToFile("WARNING", "Usuario " + UserService.getCurrentUser().getName() + " ingresó un número de cuenta no numérico.");
                throw new InvalidUserInputException("El número de cuenta debe contener solo dígitos.");
            }

            if (AccountService.doesAccountExist(accountNumber)) {
                UserService.logToFile("WARNING", "Usuario " + UserService.getCurrentUser().getName() + " intentó crear una cuenta duplicada.");
                throw new AccountAlreadyExistsException("La cuenta con el número " + accountNumber + " ya existe.");
            }

            AccountType accountType = accountTypeStr.equals("Corriente") ? AccountType.CORRIENTE : AccountType.AHORROS;
            Account newAccount = new Account(AccountService.getNextAccountId(), accountNumber, accountType, BigDecimal.ZERO, UserService.getCurrentUser());

            AccountService.addAccount(newAccount);
            AccountService.serializeAccountsToTxt(newAccount, UserService.getCurrentUser());

            UserService.logToFile("INFO", "Usuario " + UserService.getCurrentUser().getName() + " creó la cuenta con número " + accountNumber + " exitosamente.");
            lblMessage.setText("Cuenta creada exitosamente. Redirigiendo...");
            lblMessage.setStyle("-fx-text-fill: green;");

            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e -> loadView(event, "/view/UserDashboard.fxml"));
            pause.play();

            tfAccountNumber.clear();
            cbAccountType.setValue(null);

        } catch (EmptyFieldException | InvalidAccountNumberException | AccountAlreadyExistsException | InvalidUserInputException e) {
            UserService.logToFile("ERROR", "Error durante la creación de cuenta para el usuario " + UserService.getCurrentUser().getName() + ": " + e.getMessage());
            lblMessage.setText(e.getMessage());
        }
    }

    @FXML
    void btnCancelClicked(ActionEvent event) {
        UserService.logToFile("INFO", "Usuario " + UserService.getCurrentUser().getName() + " canceló la creación de una cuenta.");
        tfAccountNumber.clear();
        cbAccountType.setValue(null);
        lblMessage.setText("");
        loadView(event, "/view/UserDashboard.fxml");
    }
}