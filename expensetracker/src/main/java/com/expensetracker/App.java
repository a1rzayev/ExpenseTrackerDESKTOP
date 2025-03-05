package com.expensetracker;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import com.expensetracker.models.Expense;
import com.expensetracker.views.ExpenseFormView;
import com.expensetracker.views.ExpenseView;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.sql.*;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class App extends Application {

    private Connection connection;
    private ObservableList<Expense> expensesList;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        expensesList = FXCollections.observableArrayList();

        // Connect to SQLite database
        connection = DriverManager.getConnection("jdbc:sqlite:expenses.db");
        createTableIfNotExists();

        // Load expenses from database
        loadExpenses();

        // Create TableView
        TableView<Expense> tableView = createTableView();
        
        // Buttons
        Button addButton = new Button("Add Expense");
        addButton.setOnAction(e -> openExpenseForm(null));

        Button editButton = new Button("Edit Expense");
        editButton.setOnAction(e -> {
            Expense selectedExpense = tableView.getSelectionModel().getSelectedItem();
            openExpenseForm(selectedExpense);
        });

        Button deleteButton = new Button("Delete Expense");
        deleteButton.setOnAction(e -> deleteExpense(tableView.getSelectionModel().getSelectedItem()));

        // Layout
        HBox buttonLayout = new HBox(10, addButton, editButton, deleteButton);
        VBox layout = new VBox(10, tableView, buttonLayout);
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Expense Tracker");
        primaryStage.show();
    }

    private void createTableIfNotExists() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS expenses (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "category TEXT, " +
                                "description TEXT, " +
                                "amount REAL, " +
                                "date TEXT)";
        Statement stmt = connection.createStatement();
        stmt.execute(createTableSQL);
    }

    private void loadExpenses() throws SQLException {
        String query = "SELECT * FROM expenses";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            int id = rs.getInt("id");
            String category = rs.getString("category");
            String description = rs.getString("description");
            double amount = rs.getDouble("amount");
            String date = rs.getString("date");

            expensesList.add(new Expense(id, category, description, amount, date));
        }
    }

    private TableView<Expense> createTableView() {
        TableView<Expense> tableView = new TableView<>();
        TableColumn<Expense, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));

        TableColumn<Expense, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        TableColumn<Expense, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getAmount()).asObject());

        TableColumn<Expense, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));

        tableView.getColumns().addAll(categoryColumn, descriptionColumn, amountColumn, dateColumn);
        tableView.setItems(expensesList);

        return tableView;
    }

    private void openExpenseForm(Expense expense) {
        ExpenseFormView formView = new ExpenseFormView(expense, connection, expensesList);
        Stage formStage = new Stage();
        formView.start(formStage);
    }

    private void deleteExpense(Expense expense) {
        if (expense != null) {
            try {
                String query = "DELETE FROM expenses WHERE id = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, expense.getId());
                stmt.executeUpdate();

                expensesList.remove(expense);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}