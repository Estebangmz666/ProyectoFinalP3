package com.example.service;

import com.example.model.Budget;
import com.example.util.PropertiesLoader;
import com.example.util.SerializeDeserialize;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BudgetService {
    private static List<Budget> budgets;

    static {
        budgets = loadBudgetsFromFile();
    }

    private static String generateBudgetId() {
        return "BDG" + System.currentTimeMillis();
    }

    public static void createBudget(Budget budget) {
        budget.setBudgetId(generateBudgetId());
        budgets.add(budget);
        SerializeDeserialize.saveBudget(UserService.getCurrentUser().getUserId(), budget);
    }

    public static Optional<Budget> getBudgetById(String budgetId) {
        return budgets.stream().filter(budget -> budget.getBudgetId().equals(budgetId)).findFirst();
    }

    public static List<Budget> getAllBudgets() {
        return budgets;
    }

    public static boolean updateBudget(String budgetId, Budget updatedBudget) {
        Optional<Budget> budgetOpt = getBudgetById(budgetId);
        if (budgetOpt.isPresent()) {
            Budget budget = budgetOpt.get();
            budget.setName(updatedBudget.getName());
            budget.setTotalAmount(updatedBudget.getTotalAmount());
            budget.setSpentAmount(updatedBudget.getSpentAmount());
            budget.setCategory(updatedBudget.getCategory());
            return true;
        }
        return false;
    }

    public static boolean deleteBudget(String budgetId) {
        Optional<Budget> budgetOpt = getBudgetById(budgetId);
        if (budgetOpt.isPresent()) {
            budgets.remove(budgetOpt.get());
            return true;
        }
        return false;
    }

    public static double getRemainingAmount(String budgetId) {
        Optional<Budget> budgetOpt = getBudgetById(budgetId);
        if (budgetOpt.isPresent()) {
            Budget budget = budgetOpt.get();
            return budget.getTotalAmount() - budget.getSpentAmount();
        }
        return 0;
    }

    public static double getCategorySpentAmount(String category) {
        return budgets.stream()
            .filter(budget -> budget.getCategory().equalsIgnoreCase(category))
            .mapToDouble(Budget::getSpentAmount)
            .sum();
    }    

    public static boolean isOverBudget(String budgetId) {
        Optional<Budget> budgetOpt = getBudgetById(budgetId);
        return budgetOpt.isPresent() && budgetOpt.get().getSpentAmount() > budgetOpt.get().getTotalAmount();
    }

    public static List<Budget> searchBudgetsByCategory(String category) {
        return budgets.stream().filter(budget -> budget.getCategory().equalsIgnoreCase(category)).collect(Collectors.toList());
    }

    private static List<Budget> loadBudgetsFromFile() {
        List<Budget> allBudgets = new ArrayList<>();
        List<Budget> userBudgets = SerializeDeserialize.loadBudgets(UserService.getCurrentUser().getUserId());
        allBudgets.addAll(userBudgets);
        return allBudgets;
    }
    

    public static void displayBudget(String budgetId) {
        Optional<Budget> budgetOpt = getBudgetById(budgetId);
        if (budgetOpt.isPresent()) {
            System.out.println(budgetOpt.get());
        } else {
            System.out.println("Budget not found!");
        }
    }

    public static void updateBudgetInFile(int userId, Budget updatedBudget, BigDecimal amountToDeduct) {
        String fileName = "User" + userId + "_budgets.txt";
        File file = new File(PropertiesLoader.getRutaFromProperties("budget_base_path") + fileName);
        File tempFile = new File(PropertiesLoader.getRutaFromProperties("budget_base_path") + "temp_" + fileName);
        boolean budgetUpdated = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] attributes = line.split("@@");
                if (attributes[0].equals(updatedBudget.getBudgetId())) {
                    // Calcula los nuevos montos
                    double newSpentAmount = updatedBudget.getSpentAmount() + amountToDeduct.doubleValue();
                    double newTotalAmount = updatedBudget.getTotalAmount() - amountToDeduct.doubleValue();
                    updatedBudget.setSpentAmount(newSpentAmount);
                    updatedBudget.setTotalAmount(newTotalAmount);
    
                    // Escribe el presupuesto actualizado
                    writer.write(updatedBudget.getBudgetId() + "@@" + updatedBudget.getName() + "@@" +
                                 updatedBudget.getTotalAmount() + "@@" + updatedBudget.getSpentAmount() + "@@" +
                                 updatedBudget.getCategory() + "@@");
                    writer.newLine();
                    budgetUpdated = true;
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error al actualizar el archivo de presupuestos: " + e.getMessage());
            e.printStackTrace();
        }
    
        if (budgetUpdated) {
            try {
                Files.deleteIfExists(file.toPath());
                Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Presupuesto actualizado en el archivo correctamente.");
            } catch (IOException e) {
                System.out.println("Error al reemplazar el archivo de presupuestos: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            tempFile.delete();
            System.out.println("No se encontró el presupuesto para actualizar.");
        }
    
        // Recargar la lista de presupuestos después de la actualización
        budgets = loadBudgetsFromFile();
    }
    
}