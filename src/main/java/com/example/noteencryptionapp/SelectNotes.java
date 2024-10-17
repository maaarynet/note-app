package com.example.noteencryptionapp;

import com.example.noteencryptionapp.Note;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.SecretKey;

public class SelectNotes {
    public static List<Note> getAllNotes() throws Exception {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT title, content FROM notes";

        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            // Перебираем результаты и расшифровываем содержимое
            while (rs.next()) {
                String encryptedTitle = rs.getString("title");
                String encryptedContent = rs.getString("content");

                // Добавляем зашифрованные заметки в список
                notes.add(new Note(encryptedTitle, encryptedContent));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return notes;  // Возвращаем список заметок
    }
}
