package com.expensetracker.views;

import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;
import com.expensetracker.controllers.ExpenseController;

public class ExpenseView {
    
    private VBox mainLayout;
    
    public ExpenseView() {
        mainLayout = new VBox(10);
        setupUI();
    }

    public VBox getMainLayout() {
        return mainLayout;
    }

    private void setupUI() {
        // Add buttons and UI components for adding/editing/deleting expenses
        // For example, you can use a PieChart to display the categorized expenses
        PieChart pieChart = new PieChart();
        pieChart.getData().add(new PieChart.Data("Food", 50));
        pieChart.getData().add(new PieChart.Data("Transport", 30));
        pieChart.getData().add(new PieChart.Data("Bills", 20));

        mainLayout.getChildren().add(pieChart);
    }
}
