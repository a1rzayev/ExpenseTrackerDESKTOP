package com.expensetracker.views;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.expensetracker.models.Expense;

public class ExpenseFormView {

    private Expense expense;
    private Connection connection;
    private ObservableList<Expense> expensesList;
    private Stage primaryStage;

    // Constructor
    public ExpenseFormView(Expense expense, Connection connection, ObservableList<Expense> expensesList) {
        this.expense = expense;
        this.connection = connection;
        this.expensesList = expensesList;
    }

    // Create the form UI and handle save operation
    public void start(Stage primaryStage) {
        // Form Fields
        TextField categoryField = new TextField();
        TextField descriptionField = new TextField();
        TextField amountField = new TextField();
        TextField dateField = new TextField();
        this.primaryStage = primaryStage;

        // If editing an existing expense, populate the fields
        if (expense != null) {
            categoryField.setText(expense.getCategory());
            descriptionField.setText(expense.getDescription());
            amountField.setText(String.valueOf(expense.getAmount()));
            dateField.setText(expense.getDate());
        }

        // Buttons
        Button submitButton = new Button("Save Expense");
        submitButton.setOnAction(e -> saveExpense(categoryField, descriptionField, amountField, dateField));

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> primaryStage.close());

        // Layout
        VBox layout = new VBox(10, categoryField, descriptionField, amountField, dateField, submitButton, cancelButton);
        layout.setPadding(new Insets(10));

        // Scene
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Expense Form");
        primaryStage.show();
    }

    // Save or update expense in database and update TableView
    private void saveExpense(TextField categoryField, TextField descriptionField, TextField amountField, TextField dateField) {
        String category = categoryField.getText();
        String description = descriptionField.getText();
        double amount = Double.parseDouble(amountField.getText());
        String date = dateField.getText();

        if (expense == null) {
            try {
                // Insert new expense into database
                String query = "INSERT INTO expenses (category, description, amount, date) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, category);
                stmt.setString(2, description);
                stmt.setDouble(3, amount);
                stmt.setString(4, date);
                stmt.executeUpdate();

                // Add new expense to the ObservableList
                Expense newExpense = new Expense(0, category, description, amount, date);
                expensesList.add(newExpense);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // Update existing expense
                String query = "UPDATE expenses SET category = ?, description = ?, amount = ?, date = ? WHERE id = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, category);
                stmt.setString(2, description);
                stmt.setDouble(3, amount);
                stmt.setString(4, date);
                stmt.setInt(5, expense.getId());
                stmt.executeUpdate();

                // Update the expense in the ObservableList
                expense.setCategory(category);
                expense.setDescription(description);
                expense.setAmount(amount);
                expense.setDate(date);
                expensesList.set(expensesList.indexOf(expense), expense);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Close form
        primaryStage.close();
    }
}
