module com.expensetracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.expensetracker to javafx.fxml;
    exports com.expensetracker;
    exports com.expensetracker.models;
    exports com.expensetracker.view;
    exports com.expensetracker.controllers;
    exports com.expensetracker.utils;
}
