module com.example.noteencryptionapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;

    opens com.example.noteencryptionapp to javafx.fxml;
    exports com.example.noteencryptionapp;
}