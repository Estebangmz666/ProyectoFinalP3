package com.example.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Budget implements Serializable{
    private String budgetId;
    private String name;
    private double totalAmount;
    private double spentAmount;
    private String category;
}