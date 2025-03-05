package com.expensetracker;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Expense Tracker");

        BorderPane root = new BorderPane();
        root.setCenter(new Text("Welcome to Expense Tracker!"));
        
        Scene scene = new Scene(root, 300, 250);
        scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
