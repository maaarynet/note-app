package com.example.noteencryptionapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateNote {

    public static void updateNote(int id, String newTitle, String newContent) {
        String sql = "UPDATE notes SET title = ?, content = ? WHERE id = ?";

        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Устанавливаем новые значения
            pstmt.setString(1, newTitle);
            pstmt.setString(2, newContent);
            pstmt.setInt(3, id);

            // Выполняем запрос
            pstmt.executeUpdate();
            System.out.println("Note has been updated.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        updateNote(1, "Updated Title", "Updated Content");
    }
}
