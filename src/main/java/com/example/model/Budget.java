package com.example.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Budget implements Serializable {

    private StringProperty budgetId;
    private StringProperty name;
    private DoubleProperty totalAmount;
    private DoubleProperty spentAmount;
    private StringProperty category;

    public Budget(String budgetId, String name, double totalAmount, double spentAmount, String category) {
        this.budgetId = new SimpleStringProperty(budgetId);
        this.name = new SimpleStringProperty(name);
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
        this.spentAmount = new SimpleDoubleProperty(spentAmount);
        this.category = new SimpleStringProperty(category);
    }

    public String getBudgetId() {
        return budgetId.get();
    }

    public void setBudgetId(String budgetId) {
        this.budgetId.set(budgetId);
    }

    public StringProperty budgetIdProperty() {
        return budgetId;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public double getTotalAmount() {
        return totalAmount.get();
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount.set(totalAmount);
    }

    public DoubleProperty totalAmountProperty() {
        return totalAmount;
    }

    public double getSpentAmount() {
        return spentAmount.get();
    }

    public void setSpentAmount(double spentAmount) {
        this.spentAmount.set(spentAmount);
    }

    public DoubleProperty spentAmountProperty() {
        return spentAmount;
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public StringProperty categoryProperty() {
        return category;
    }
}