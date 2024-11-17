package com.example.controller;

import java.math.BigDecimal;

import com.example.model.Budget;
import com.example.service.BudgetService;
import com.example.service.UserService;
import com.example.util.LogToFile;
import com.example.util.SerializeDeserialize;
import com.example.util.ViewLoader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BudgetManagementController implements ViewLoader{

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
            lbMessage.setText("Error cargando la vista: " + view);
            LogToFile.logToFile("ERROR", "Error cargando la vista " + view + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private TextField tfAmount;

    @FXML
    private TextField tfName;

    @FXML
    private Label lbMessage;

    @FXML
    private Button btnBack;

    private Budget budget;

    public void setBudgetData(Budget budget) {
        this.budget = budget;
        tfName.setText(budget.getName());
        tfAmount.setText(String.valueOf(budget.getTotalAmount()));
    }

    @FXML
    public void btnBackClicked(ActionEvent event){
        loadView(event, "/view/BudgetDashboard.fxml");
    }

    @FXML
    public void updateBudget() {
        try {
            String name = tfName.getText().trim();
            double amount = Double.parseDouble(tfAmount.getText().trim());
            budget.setName(name);
            budget.setTotalAmount(amount);
            BudgetService.updateBudgetInFile(UserService.getCurrentUser().getUserId(), budget, BigDecimal.ZERO);
            lbMessage.setText("Presupuesto actualizado con éxito.");
        } catch (Exception e) {
            lbMessage.setText("Error al actualizar el presupuesto: " + e.getMessage());
        }
    }

    @FXML
    public void deleteBudget() {
        try {
            boolean success = BudgetService.deleteBudget(budget.getBudgetId());
            if (success) {
                SerializeDeserialize.deleteBudget(UserService.getCurrentUser().getUserId(), budget.getBudgetId()); // Eliminar del archivo
                lbMessage.setText("Presupuesto eliminado.");
            } else {
                lbMessage.setText("Error al eliminar el presupuesto.");
            }
        } catch (Exception e) {
            lbMessage.setText("Error al eliminar el presupuesto: " + e.getMessage());
        }
    }
}