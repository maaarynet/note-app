package com.example.noteencryptionapp;

import javax.crypto.SecretKey;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DeleteNote {
    public static void deleteNoteByTitle(String title, SecretKey key) {
        String encryptedTitle;
        try {
            // Зашифруйте заголовок перед удалением, так как он хранится в зашифрованном виде
            encryptedTitle = EncryptionUtils.encrypt(title, key);

            String sql = "DELETE FROM notes WHERE title = ?";

            try (Connection conn = SQLiteConnection.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, encryptedTitle);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Заметка с заголовком '" + title + "' успешно удалена.");
                } else {
                    System.out.println("Заметка с заголовком '" + title + "' не найдена.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
