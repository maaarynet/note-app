package com.example.noteencryptionapp;

import javax.crypto.SecretKey;

public class EncryptionDemo {
    public static void main(String[] args) throws Exception {
        String password = "password";  // Используемый пароль
        String textToEncrypt = "test"; // Тестовая строка для шифрования

        try {
            // Генерация ключа на основе пароля
            SecretKey key = EncryptionUtils.generateKeyFromPassword(password);

            // Шифрование строки
            String encryptedText = EncryptionUtils.encrypt(textToEncrypt, key);
            System.out.println("Зашифрованный текст: " + encryptedText);

            // Дешифровка строки
            String decryptedText = EncryptionUtils.decrypt(encryptedText, key);
            System.out.println("Расшифрованный текст: " + decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
