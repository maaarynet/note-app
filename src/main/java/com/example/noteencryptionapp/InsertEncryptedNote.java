package com.example.noteencryptionapp;

import javax.crypto.SecretKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertEncryptedNote {
    public static void insertEncryptedNote(String title, String content, SecretKey key) throws Exception {
        // Шифруем содержимое заметки
        String encryptedContent = EncryptionUtils.encrypt(content, key);

        String sql = "INSERT INTO notes(title, content) VALUES(?,?)";

        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, encryptedContent);
            pstmt.executeUpdate();
            System.out.println("Заметка успешно добавлена.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
