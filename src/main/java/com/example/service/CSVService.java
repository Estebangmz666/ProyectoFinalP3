package com.example.service;

import com.example.model.Transaction;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVService {

    public void exportToCSV(List<Transaction> transactions, Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("Transaction ID,Date,Amount,Description\n");
                for (Transaction transaction : transactions) {
                    writer.write(transaction.getTransactionId() + ","
                            + transaction.getDate() + ","
                            + transaction.getAmount() + ","
                            + transaction.getDescription() + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}