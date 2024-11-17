package com.example.controller;

import com.example.model.Budget;
import com.example.model.Category;
import com.example.service.BudgetService;
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
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class BudgetDashboardController implements ViewLoader {

    @FXML
    private Button btnBack;

    @FXML
    private Button btnCreateBudget;

    @FXML
    private TableColumn<Budget, Double> colAmount;

    @FXML
    private TableColumn<Budget, String> colCategory;

    @FXML
    private TableColumn<Budget, String> colID;

    @FXML
    private TableColumn<Budget, String> colName;

    @FXML
    private TableColumn<Budget, Double> colSpentAmount;

    @FXML
    private Label lbMessage;

    @FXML
    private PieChart pcAmountToSpend;

    @FXML
    private TextField tfAmount;

    @FXML
    private TextField tfName;

    @FXML
    private TableView<Budget> budgetTable;

    @FXML
    private ComboBox<String> cbCategory;

    @Override
    public void loadView(ActionEvent event, String view) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(view));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            LogToFile.logToFile("INFO", "Usuario cambió la vista a " + view);
        } catch (Exception e) {
            LogToFile.logToFile("ERROR", "Error al cargar la vista " + view + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        colID.setCellValueFactory(cellData -> cellData.getValue().budgetIdProperty());
        colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colAmount.setCellValueFactory(cellData -> cellData.getValue().totalAmountProperty().asObject());
        colSpentAmount.setCellValueFactory(cellData -> cellData.getValue().spentAmountProperty().asObject());
        colCategory.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        budgetTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updatePieChart(newSelection);
            }
        });
        budgetTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Budget selectedBudget = budgetTable.getSelectionModel().getSelectedItem();
                if (selectedBudget != null) {
                    openBudgetManagementScene(selectedBudget);
                }
            }
        });
        loadBudgets();
        loadCategories();
    }

    private void openBudgetManagementScene(Budget selectedBudget) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/BudgetManagement.fxml"));
            Parent root = loader.load();
            BudgetManagementController controller = loader.getController();
            controller.setBudgetData(selectedBudget);
            Stage currentStage = (Stage) budgetTable.getScene().getWindow();
            currentStage.close();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            lbMessage.setText("Error al abrir la escena de gestión de presupuesto: " + e.getMessage());
            e.printStackTrace();
        }
    }    

    public void loadBudgets() {
        ObservableList<Budget> budgets = FXCollections.observableArrayList(BudgetService.getAllBudgets());
        budgetTable.setItems(budgets);
        updatePieChart(null);
    }

    private void updatePieChart(Budget selectedBudget) {
        pcAmountToSpend.getData().clear();
        if (selectedBudget != null) {
            double remainingAmount = selectedBudget.getTotalAmount() - selectedBudget.getSpentAmount();
            pcAmountToSpend.getData().add(new PieChart.Data("Gastado", selectedBudget.getSpentAmount()));
            pcAmountToSpend.getData().add(new PieChart.Data("Disponible", remainingAmount));
        } else {
            lbMessage.setText("Seleccione un presupuesto para ver detalles.");
        }
    }

    private boolean validateInputs() {
        if (tfName.getText().trim().isEmpty() || tfAmount.getText().trim().isEmpty()) {
            lbMessage.setText("Por favor, complete todos los campos.");
            return false;
        }
        try {
            Double.parseDouble(tfAmount.getText().trim());
        } catch (NumberFormatException e) {
            lbMessage.setText("Por favor, ingrese valores válidos para el monto.");
            return false;
        }
        return true;
    }

    @FXML
    public void btnCreateBudgetClicked() {
        if (!validateInputs()) {
            return;
        }
        try {
            String name = tfName.getText().trim();
            double amount = Double.parseDouble(tfAmount.getText().trim());
            String category = cbCategory.getValue();
            if (category == null || category.trim().isEmpty()) {
                lbMessage.setText("Por favor, seleccione una categoría.");
                return;
            }
            Budget budget = new Budget("", name, amount, 0.0, category);
            BudgetService.createBudget(budget);
            loadBudgets();
            lbMessage.setText("Presupuesto creado con éxito.");
        } catch (Exception e) {
            lbMessage.setText("Error al crear el presupuesto: " + e.getMessage());
        }
    }

    @FXML
    public void deleteBudget() {
        Budget selectedBudget = budgetTable.getSelectionModel().getSelectedItem();
        if (selectedBudget != null) {
            boolean success = BudgetService.deleteBudget(selectedBudget.getBudgetId());
            if (success) {
                loadBudgets();
                lbMessage.setText("Presupuesto eliminado.");
            } else {
                lbMessage.setText("Error al eliminar el presupuesto.");
            }
        } else {
            lbMessage.setText("Seleccione un presupuesto para eliminar.");
        }
    }

    @FXML
    public void btnBackClicked(ActionEvent event) {
        loadView(event, "/view/UserDashboard.fxml");
    }

    private void loadCategories() {
        Category[] categories = Category.values();
        for (Category category : categories) {
            cbCategory.getItems().add(category.name());
        }
        LogToFile.logToFile("INFO", "Categorías cargadas desde el enum Category en ComboBox.");
    }
}