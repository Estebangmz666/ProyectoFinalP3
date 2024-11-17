package com.example.controller;

import com.example.model.Transaction;
import com.example.service.CSVService;
import com.example.service.PDFService;
import com.example.service.TransactionService;
import com.example.service.UserService;
import com.example.util.LogToFile;
import com.example.util.ViewLoader;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.List;

public class RecentTransactionsController implements ViewLoader {

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
            LogToFile.logToFile("ERROR", "Error cargando la vista " + view + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private Button btnBack;

    @FXML
    private TableView<Transaction> tableView;

    @FXML
    private TableColumn<Transaction, String> transactionIdColumn;

    @FXML
    private TableColumn<Transaction, String> dateColumn;

    @FXML
    private TableColumn<Transaction, String> amountColumn;

    @FXML
    private TableColumn<Transaction, String> descriptionColumn;

    @FXML
    private Button btnExportPDF;

    @FXML
    private Button btnExportCSV;

    @FXML
    private Label lbMessage;

    private int userId = UserService.getCurrentUser().getUserId();

    @FXML
    public void initialize() {
        transactionIdColumn.setCellValueFactory(cellData -> cellData.getValue().transactionIdProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty().asString());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asString());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        loadTransactions();
    }

    public void loadTransactions() {
        List<Transaction> transactions = TransactionService.getRecentTransactions(userId);
        if (transactions.isEmpty()) {
            LogToFile.logToFile("WARNING", "No se encontraron transacciones recientes para el usuario con ID: " + userId);
        } else {
            LogToFile.logToFile("INFO", "Transacciones cargadas correctamente para el usuario con ID: " + userId);
        }
        tableView.setItems(FXCollections.observableArrayList(transactions));
    }

    @FXML
    public void exportToPDF() {
        try {
            List<Transaction> transactions = tableView.getItems();
            PDFService pdfService = new PDFService();
            Stage stage = (Stage) btnExportPDF.getScene().getWindow();
            pdfService.generatePDFReport(transactions, stage);
            lbMessage.setText("Exportación a PDF exitosa.");
            LogToFile.logToFile("INFO", "Exportación a PDF realizada correctamente.");
        } catch (Exception e) {
            LogToFile.logToFile("ERROR", "Error al exportar a PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void exportToCSV() {
        try {
            List<Transaction> transactions = tableView.getItems();
            CSVService csvService = new CSVService();
            Stage stage = (Stage) btnExportCSV.getScene().getWindow();
            csvService.exportToCSV(transactions, stage);
            lbMessage.setText("Exportación a CSV exitosa.");
            LogToFile.logToFile("INFO", "Exportación a CSV realizada correctamente.");
        } catch (Exception e) {
            LogToFile.logToFile("ERROR", "Error al exportar a CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @FXML
    public void btnBackClicked(){
        loadView(null, "/view/Userdashboard.fxml");
    }
}