package com.example.noteencryptionapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertNote {

    public static void insertNote(String title, String content) {
        String sql = "INSERT INTO notes(title, content) VALUES(?, ?)";

        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.executeUpdate();
            System.out.println("New note has been inserted.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        insertNote("First Note", "This is the content of the first note.");
    }
}
