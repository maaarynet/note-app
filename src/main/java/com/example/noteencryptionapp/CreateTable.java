package com.example.noteencryptionapp;

import java.sql.Connection;
import java.sql.Statement;

public class CreateTable {
    public static void createNewTable() {
        // SQL-запрос для создания таблицы
        String sql = "CREATE TABLE IF NOT EXISTS notes (\n"
                + " id integer PRIMARY KEY AUTOINCREMENT,\n"
                + " title text NOT NULL,\n"
                + " content text NOT NULL\n"
                + ");";

        try (Connection conn = SQLiteConnection.connect();
             Statement stmt = conn.createStatement()) {
            // создаем новую таблицу
            stmt.execute(sql);
            System.out.println("Таблица успешно создана.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
