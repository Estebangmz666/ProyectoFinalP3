package com.example.controller;

import com.example.model.Account;
import com.example.model.User;
import com.example.service.AccountService;
import com.example.service.UserService;
import com.example.util.LogToFile;
import com.example.util.ViewLoader;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DecimalFormat;

public class UserDashboardController implements ViewLoader {

    @FXML
    private Button btnGoToAddAccount;

    @FXML
    private Button btnGoToBudgets;

    @FXML
    private Button btnGoToRecentTransactions;

    @FXML
    private Button btnHamburguer;

    @FXML
    private Hyperlink hlLogout;

    @FXML
    private ListView<String> lvAccounts;

    @FXML
    private VBox vbMenu;

    private User currentUser;

    @Override
    public void loadView(ActionEvent event, String view) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(view));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            LogToFile.logToFile("INFO", "Vista cargada: " + view);
        } catch (Exception e) {
            LogToFile.logToFile("SEVERE", "Error al cargar la vista: " + view + ". " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        currentUser = UserService.getCurrentUser();
        if (currentUser != null) {
            currentUser.setAccounts(AccountService.deserializeAccountsFromTxt(currentUser));

            ObservableList<String> accountItems = FXCollections.observableArrayList();
            DecimalFormat df = new DecimalFormat("#,##0.00");
            
            for (Account account : currentUser.getAccounts()) {
                String maskedAccountNumber = account.getAccountNumber().substring(0, 4) + "****" + account.getAccountNumber().substring(account.getAccountNumber().length() - 4);
                String accountInfo = "ID: " + account.getAccountId() + "\n" 
                                + maskedAccountNumber + "\n"
                                + "Tipo: " + account.getAccountType() + "\n"
                                + "Saldo: $" + df.format(account.getBalance());
                accountItems.add(accountInfo);
            }
            lvAccounts.setItems(accountItems);
            LogToFile.logToFile("INFO", "Cuentas cargadas en la lista del usuario " + currentUser.getName());

            lvAccounts.setOnMouseClicked((MouseEvent event) -> {
                if (event.getClickCount() == 2) {
                    int selectedIndex = lvAccounts.getSelectionModel().getSelectedIndex();
                    if (selectedIndex != -1) {
                        Account selectedAccount = currentUser.getAccounts().get(selectedIndex);
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AccountManagement.fxml"));
                            Parent root = loader.load();

                            AccountManagementController controller = loader.getController();
                            controller.setAccount(selectedAccount);

                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            Scene scene = new Scene(root);
                            stage.setScene(scene);
                            stage.show();
                            LogToFile.logToFile("INFO", "Usuario " + currentUser.getName() + " accedió a la gestión de cuenta: " + selectedAccount.getAccountNumber());
                        } catch (IOException e) {
                            LogToFile.logToFile("SEVERE", "Error al abrir AccountManagement para cuenta " + selectedAccount.getAccountNumber() + ": " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    @FXML
    void btnGoToAddAccountClicked(ActionEvent event) {
        loadView(event, "/view/AccountDashboard.fxml");
        LogToFile.logToFile("INFO", "Usuario fue a añadir una nueva cuenta.");
    }

    @FXML
    void btnGoToBudgetsClicked(ActionEvent event) {
        loadView(event, "/view/BudgetDashboard.fxml");
        LogToFile.logToFile("INFO", "Usuario fue al panel de presupuestos.");
    }

    @FXML
    void btnGoToRecentTransactionsClicked(ActionEvent event) {
        loadView(event, "/view/TransactionsDashboard.fxml");
        LogToFile.logToFile("INFO", "Usuario fue al panel de transacciones recientes.");
    }

    @FXML
    void btnHamburguerClicked(ActionEvent event) {
        vbMenu.setVisible(!vbMenu.isVisible());
        vbMenu.toFront();
        LogToFile.logToFile("INFO", "Usuario " + currentUser.getName() + " usó el menú hamburguesa.");
    }

    @FXML
    void hlLogoutClicked(ActionEvent event) {
        UserService.setCurrentUser(null);
        loadView(event, "/view/Login.fxml");
        LogToFile.logToFile("INFO", "Usuario cerró sesión.");
    }
}
