package com.example.noteencryptionapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {
    public static Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:notes.db";  // База данных SQLite будет создана локально в проекте
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
