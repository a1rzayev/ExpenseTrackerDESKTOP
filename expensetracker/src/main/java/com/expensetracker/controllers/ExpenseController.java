package com.expensetracker.controllers;

import com.expensetracker.models.Expense;
import com.expensetracker.utils.SQLiteUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseController {

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS expenses (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "category TEXT," +
            "amount REAL," +
            "description TEXT," +
            "date TEXT)";

    public static void createTable() {
        try (Connection conn = SQLiteUtil.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TABLE_QUERY);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addExpense(Expense expense) {
        String query = "INSERT INTO expenses (category, amount, description, date) VALUES (?, ?, ?, ?)";
        try (Connection conn = SQLiteUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, expense.getCategory());
            pstmt.setDouble(2, expense.getAmount());
            pstmt.setString(3, expense.getDescription());
            pstmt.setString(4, expense.getDate());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        String query = "SELECT * FROM expenses";
        try (Connection conn = SQLiteUtil.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Expense expense = new Expense(
                        rs.getInt("id"),
                        rs.getString("category"),
                        rs.getDouble("amount"),
                        rs.getString("description"),
                        rs.getString("date")
                );
                expenses.add(expense);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    public static void deleteExpense(int id) {
        String query = "DELETE FROM expenses WHERE id = ?";
        try (Connection conn = SQLiteUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
