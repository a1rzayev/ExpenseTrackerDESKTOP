package com.expensetracker;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ExpenseController {
    private ObservableList<Expense> expenses;

    public ExpenseController() {
        this.expenses = FXCollections.observableArrayList();
    }

    public ObservableList<Expense> getExpenses() {
        return expenses;
    }

    public void addExpense(String name, double amount, String date) {
        Expense expense = new Expense(name, amount, LocalDate.parse(date));
        expenses.add(expense);
    }

    public double getTotalExpenses() {
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }
}
