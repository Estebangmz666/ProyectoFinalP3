package co.edu.uniquindio.banco.bancouq.model;

import java.io.Serializable;

public class Budget implements Serializable{
    private String budgetId;
    private String name;
    private double totalAmount;
    private double spentAmount;
    private String category;
    
    public Budget(String budgetId, String name, double totalAmount, double spentAmount, String category) {
        this.budgetId = budgetId;
        this.name = name;
        this.totalAmount = totalAmount;
        this.spentAmount = spentAmount;
        this.category = category;
    }

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(double spentAmount) {
        this.spentAmount = spentAmount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}