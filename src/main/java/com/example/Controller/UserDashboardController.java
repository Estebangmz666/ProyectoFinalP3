package com.example.controller;

import com.example.model.Account;
import com.example.model.User;
import com.example.model.ViewLoader;
import com.example.service.AccountService;
import com.example.service.UserService;

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
        } catch (Exception e) {
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
                        } catch (IOException e) {
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
    }

    @FXML
    void btnGoToBudgetsClicked(ActionEvent event) {
        loadView(event, "/view/BudgetDashboard.fxml");
    }

    @FXML
    void btnGoToRecentTransactionsClicked(ActionEvent event) {
        loadView(event, "/view/TransactionsDashboard.fxml");
    }

    @FXML
    void btnHamburguerClicked(ActionEvent event) {
        vbMenu.setVisible(!vbMenu.isVisible());
        vbMenu.toFront();
    }

    @FXML
    void hlLogoutClicked(ActionEvent event) {
        UserService.setCurrentUser(null);
        loadView(event, "/view/Login.fxml");
    }
}