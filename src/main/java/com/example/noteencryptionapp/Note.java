package com.example.noteencryptionapp;

public class Note {
    private String encryptedTitle;
    private String encryptedDescription;

    public Note(String encryptedTitle, String encryptedDescription) {
        this.encryptedTitle = encryptedTitle;
        this.encryptedDescription = encryptedDescription;
    }

    public String getEncryptedTitle() {
        return encryptedTitle;
    }

    public String getEncryptedDescription() {
        return encryptedDescription;
    }
}
