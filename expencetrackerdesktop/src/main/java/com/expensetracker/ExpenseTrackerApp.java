package com.expensetracker;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ExpenseTrackerApp extends Application {

    private ExpenseController controller = new ExpenseController();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Expense Tracker");

        // Layouts
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));

        // TableView for expenses
        TableView<Expense> expenseTable = new TableView<>();
        TableColumn<Expense, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getName()));
        
        TableColumn<Expense, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(cellData -> Bindings.createDoubleBinding(() -> cellData.getValue().getAmount()).asObject());
    

        TableColumn<Expense, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getDate().toString()));

        expenseTable.getColumns().addAll(nameCol, amountCol, dateCol);

        // Adding data
        expenseTable.setItems(controller.getExpenses());

        // Input fields
        TextField nameField = new TextField();
        nameField.setPromptText("Expense Name");

        TextField amountField = new TextField();
        amountField.setPromptText("Amount");

        TextField dateField = new TextField();
        dateField.setPromptText("Date (yyyy-mm-dd)");

        Button addButton = new Button("Add Expense");
        addButton.setOnAction(e -> {
            String name = nameField.getText();
            double amount = Double.parseDouble(amountField.getText());
            String date = dateField.getText();
            controller.addExpense(name, amount, date);
            nameField.clear();
            amountField.clear();
            dateField.clear();
        });

        // Total label
        Text totalLabel = new Text("Total: 0.00");
        totalLabel.textProperty().bind(Bindings.format("Total: %.2f", controller.getTotalExpenses()));

        mainLayout.getChildren().addAll(
            new Label("Enter Expense Details"),
            nameField, amountField, dateField, addButton,
            expenseTable, totalLabel
        );

        Scene scene = new Scene(mainLayout, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
