package com.example.noteencryptionapp;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;

import javax.crypto.SecretKey;
import java.util.List;


public class NoteApp extends Application {
    private final String encryptedTestData = "заранее_зашифрованная_строка";
    private int noteCount = 0; // Изначально 0 заметок
    private Label noteCountLabel;
    private GridPane notesGrid;
    private SecretKey secretKey;
    private Scene mainScene;
    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        // Создаем таблицу, если она не существует
        CreateTable.createNewTable();

        // Отображаем экран для ввода пароля
        showPasswordScreen();
    }


    private void showPasswordScreen() {
        Label promptLabel = new Label("Enter Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Label errorMessage = new Label();
        errorMessage.setStyle("-fx-text-fill: red;");

        // Корректный зашифрованный тестовый текст
        String encryptedTest = "JAO+U1QlNqETy2bBu8CyUZUSoFNjkg2gRpr6PZk5xpo=";

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String password = passwordField.getText();
            if (!password.trim().isEmpty()) {
                try {
                    // Генерация ключа на основе введенного пароля
                    secretKey = EncryptionUtils.generateKeyFromPassword(password);

                    // Переход к главному экрану
                    showMainScreen(primaryStage);
                } catch (Exception ex) {
                    System.out.println("Ошибка при генерации ключа.");
                    ex.printStackTrace();
                }
            }
        });

        VBox passwordLayout = new VBox(10, promptLabel, passwordField, loginButton, errorMessage);
        passwordLayout.setAlignment(Pos.CENTER);
        passwordLayout.setPadding(new Insets(20));

        Scene passwordScene = new Scene(passwordLayout, 300, 200);
        primaryStage.setScene(passwordScene);
        primaryStage.setTitle("Login");
        primaryStage.show();
    }



    private void showMainScreen(Stage stage) {
        // Верхний блок с заголовком "All Notes"
        Label title = new Label("All Notes");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #001219;");

        // Текущее количество заметок
        noteCountLabel = new Label("Total notes: " + noteCount);
        noteCountLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic; -fx-text-fill: #001219;");

        VBox topBar = new VBox(title, noteCountLabel);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(10));
        topBar.setSpacing(5); // Пространство между заголовком и количеством заметок

        // Блок с заметками
        notesGrid = new GridPane();
        notesGrid.setPadding(new Insets(10));
        notesGrid.setHgap(10);
        notesGrid.setVgap(10);

        // Загружаем зашифрованные заметки из базы данных
        try {
            // Передаем ключ шифрования в метод getAllNotes
            List<Note> notes = SelectNotes.getAllNotes();
            for (Note note : notes) {
                // Расшифровка заметки
                String decryptedTitle = EncryptionUtils.decrypt(note.getEncryptedTitle(), secretKey);
                String decryptedDescription = EncryptionUtils.decrypt(note.getEncryptedDescription(), secretKey);

                // Отображаем заметку на экране
                addNoteToGrid(notesGrid, decryptedTitle, decryptedDescription, noteCount % 2, noteCount / 2);
                noteCount++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        // Нижняя панель с кнопкой добавления
        Button addButton = new Button("+");
        addButton.setStyle("-fx-font-size: 24px; -fx-background-color: #728291; -fx-text-fill: white;");
        addButton.setPrefSize(50, 50);
        addButton.setShape(new Circle(25));

        // Добавляем обработчик события для кнопки добавления заметки
        addButton.setOnAction(e -> showNoteCreationScreen());

        // Панель для фиксации кнопки "+" внизу
        StackPane bottomBar = new StackPane(addButton);
        bottomBar.setPadding(new Insets(10));
        bottomBar.setAlignment(Pos.BOTTOM_RIGHT); // Кнопка "+" выравнена в правом нижнем углу

        // Основной контейнер (BorderPane)
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topBar); // Верхняя панель с заголовком
        mainLayout.setCenter(notesGrid); // Основное содержимое - заметки
        mainLayout.setBottom(bottomBar); // Кнопка "+" фиксируется внизу
        mainLayout.setStyle("-fx-background-color: #d9f6ff;");  // Устанавливаем цвет фона

        // Создаем основную сцену (главную страницу)
        mainScene = new Scene(mainLayout, 335, 600);
        mainScene.setCursor(Cursor.DEFAULT);
        stage.setTitle("Note App");
        stage.setScene(mainScene);
        stage.show();
    }




    // Метод для адаптации макета в зависимости от размеров окна
    private void adaptLayoutBasedOnSize(Stage stage) {
        double width = stage.getWidth();
        double height = stage.getHeight();

        if (width < 600) {
            // Если окно меньше, чем 600px в ширину — уменьшаем размеры элементов
            notesGrid.setPrefWidth(width - 20);
            notesGrid.setMinWidth(width - 20);
        } else {
            // Если окно больше, чем 600px — устанавливаем нормальные размеры
            notesGrid.setPrefWidth(600);
        }

        if (height < 400) {
            // Аналогично, обрабатываем высоту окна
            notesGrid.setPrefHeight(height - 100);
        } else {
            notesGrid.setPrefHeight(500);
        }
    }

    // Показ экрана создания новой заметки (чистый лист)
    private void showNoteCreationScreen() {
        // Поле для заголовка
        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        titleField.setStyle(
                "-fx-background-color: #d9f6ff;" +
                        "-fx-border-color: transparent;" +
                        "-fx-prompt-text-fill: rgba(10, 147, 150, 0.5);" +
                        "-fx-text-fill: #0A9396;" +
                        "-fx-font-size: 20px;" +
                        "-fx-padding: 5;"
        );
        titleField.setFocusTraversable(false);

        // Поле для ввода описания
        TextArea noteArea = new TextArea();
        noteArea.setPromptText("Write your note here...");
        noteArea.setStyle(
                "-fx-background-color: #d9f6ff;" +
                        "-fx-control-inner-background: #d9f6ff;" +
                        "-fx-border-color: transparent;" +
                        "-fx-focus-color: transparent;" +
                        "-fx-faint-focus-color: transparent;" +
                        "-fx-highlight-fill: transparent;" +
                        "-fx-prompt-text-fill: rgba(10, 147, 150, 0.5);" +
                        "-fx-text-fill: #0A9396;" +
                        "-fx-font-size: 18px;" +
                        "-fx-padding: 0 0 0 0;"
        );
        noteArea.setWrapText(true);

        VBox.setVgrow(noteArea, Priority.ALWAYS);

        // Кнопка для сохранения заметки с иконкой
        Button saveButton = new Button();
        Image checkIcon = new Image(getClass().getResourceAsStream("/check.png"));
        ImageView checkView = new ImageView(checkIcon);
        checkView.setFitHeight(30);  // Устанавливаем высоту иконки
        checkView.setFitWidth(30);   // Устанавливаем ширину иконки
        saveButton.setGraphic(checkView); // Устанавливаем иконку вместо текста
        saveButton.setStyle("-fx-background-color: transparent; -fx-background-radius: 50%;");
        saveButton.setCursor(Cursor.HAND);

        saveButton.setOnAction(e -> {
            String title = titleField.getText();
            String description = noteArea.getText();
            if (!title.trim().isEmpty() && !description.trim().isEmpty()) {
                try {
                    if (secretKey != null) {
                        // Шифруем содержимое заметки
                        String encryptedTitle = EncryptionUtils.encrypt(title, secretKey);
                        String encryptedDescription = EncryptionUtils.encrypt(description, secretKey);

                        // Сохраняем зашифрованную заметку в базу данных
                        InsertEncryptedNote.insertEncryptedNote(encryptedTitle, encryptedDescription, secretKey);

                        // Возвращаемся на основной экран
                        primaryStage.setScene(mainScene);
                    } else {
                        System.out.println("Ключ шифрования не инициализирован.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        // Кнопка для отмены
        Button cancelButton = new Button();
        Image backArrowIcon = new Image(getClass().getResourceAsStream("/arrow.png"));
        ImageView backArrowView = new ImageView(backArrowIcon);
        backArrowView.setFitHeight(30);
        backArrowView.setFitWidth(30);
        cancelButton.setGraphic(backArrowView);
        cancelButton.setStyle("-fx-background-color: transparent; -fx-background-radius: 50%;");
        cancelButton.setCursor(Cursor.HAND);

        cancelButton.setOnAction(e -> {
            primaryStage.setScene(mainScene);

            // Попробуем сначала убрать курсор
            mainScene.setCursor(Cursor.NONE);

            // Принудительно установим задержку перед сбросом на DEFAULT
            PauseTransition pause = new PauseTransition(Duration.millis(100));
            pause.setOnFinished(event -> {
                mainScene.setCursor(Cursor.DEFAULT);
            });
            pause.play();
        });

        // Расположение кнопок с использованием BorderPane
        BorderPane buttonPane = new BorderPane();
        buttonPane.setLeft(cancelButton);
        BorderPane.setMargin(cancelButton, new Insets(10, 0, 10, 10));
        buttonPane.setRight(saveButton);
        BorderPane.setMargin(saveButton, new Insets(10, 10, 10, 0));

        // Основной layout для экрана создания заметки
        VBox noteCreationLayout = new VBox(10, titleField, noteArea, buttonPane);
        noteCreationLayout.setPadding(new Insets(20));
        noteCreationLayout.setAlignment(Pos.TOP_LEFT);
        noteCreationLayout.setSpacing(20);
        noteCreationLayout.setStyle("-fx-background-color: #d9f6ff;");

        Scene creationScene = new Scene(noteCreationLayout, 335, 600);
        creationScene.setCursor(Cursor.DEFAULT);
        primaryStage.setScene(creationScene);
    }

    // Метод для добавления заметок в сетку с кнопкой удаления в правом нижнем углу
    private void addNoteToGrid(GridPane grid, String title, String description, int col, int row) {
        VBox note = new VBox();
        note.setPadding(new Insets(10));
        note.setSpacing(5);
        note.setStyle("-fx-background-color: #A1B5D8; -fx-border-color: #B0C4DE; -fx-border-radius: 5; -fx-background-radius: 5;");
        note.setMinSize(150, 100);

        Label noteTitle = new Label(title);
        noteTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

        // Ограничиваем длину текста до 20 символов и добавляем многоточие, если текст длиннее
        String truncatedDescription;
        if (description.length() > 20) {
            truncatedDescription = description.substring(0, 20) + "...";
        } else {
            truncatedDescription = description;
        }

        Label noteDescription = new Label(truncatedDescription);
        noteDescription.setWrapText(true);

        // Устанавливаем выравнивание текста по левому краю
        VBox textBox = new VBox(noteTitle, noteDescription);
        textBox.setAlignment(Pos.TOP_LEFT);  // Выравнивание текста по левому краю

        // Добавляем кнопку удаления в правом нижнем углу
        Image trashIcon = new Image(getClass().getResourceAsStream("/recycle-bin.png"));
        ImageView trashView = new ImageView(trashIcon);
        trashView.setFitHeight(20);  // Устанавливаем высоту иконки
        trashView.setFitWidth(20);   // Устанавливаем ширину иконки

        Button deleteButton = new Button();
        deleteButton.setGraphic(trashView);
        deleteButton.setStyle("-fx-font-size: 14px; -fx-background-color: transparent;");
        deleteButton.setOnAction(e -> {
            // Удаляем заметку из пользовательского интерфейса
            notesGrid.getChildren().remove(note);  // Удаляем заметку из интерфейса
            noteCount--;
            updateNoteCount();

            // Удаляем заметку из базы данных, передаем заголовок и ключ
            DeleteNote.deleteNoteByTitle(title, secretKey);
        });


        // Используем BorderPane для размещения элементов
        BorderPane noteLayout = new BorderPane();
        noteLayout.setTop(textBox); // Текст размещается сверху
        noteLayout.setBottom(deleteButton); // Кнопка удаления внизу
        BorderPane.setAlignment(deleteButton, Pos.BOTTOM_RIGHT); // Выравниваем кнопку удаления вправо и вниз
        BorderPane.setMargin(deleteButton, new Insets(15, -15, -10, 0));

        // Добавляем обработчик события на блок заметки для редактирования
        note.setOnMouseClicked(e -> {
            showNoteEditScreen(title, description, noteTitle, noteDescription); // Открываем окно редактирования
        });

        // Добавляем все элементы в GridPane, noteLayout будет вложен в note
        note.getChildren().add(noteLayout);

        grid.add(note, col, row); // Добавляем заметку в сетку
    }

    // Метод для показа окна редактирования заметки
    private void showNoteEditScreen(String existingTitle, String existingDescription, Label noteTitle, Label noteDescription) {
        // Поле для заголовка
        TextField titleField = new TextField(existingTitle); // Предварительно заполняем заголовок
        titleField.setPromptText("Title");
        titleField.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: transparent;" +
                        "-fx-prompt-text-fill: rgba(10, 147, 150, 0.5);" +
                        "-fx-text-fill: #0A9396;" +
                        "-fx-font-size: 20px;"
        );
        titleField.setFocusTraversable(false);

        // Поле для ввода описания
        TextArea noteArea = new TextArea(existingDescription); // Предварительно заполняем описание
        noteArea.setPromptText("Write your note here...");
        noteArea.setStyle(
                "-fx-background-color: #d9f6ff;" +
                        "-fx-control-inner-background: #d9f6ff;" +
                        "-fx-border-color: transparent;" +
                        "-fx-focus-color: transparent;" +
                        "-fx-faint-focus-color: transparent;" +
                        "-fx-highlight-fill: transparent;" +
                        "-fx-prompt-text-fill: rgba(10, 147, 150, 0.5);" +
                        "-fx-text-fill: #0A9396;" +
                        "-fx-font-size: 18px;" +
                        "-fx-padding: 0 0 0 0;"
        );
        noteArea.setWrapText(true);

        VBox.setVgrow(noteArea, Priority.ALWAYS);

        // Кнопка для сохранения изменений с иконкой
        Button saveButton = new Button();
        Image checkIcon = new Image(getClass().getResourceAsStream("/check.png"));
        ImageView checkView = new ImageView(checkIcon);
        checkView.setFitHeight(30);  // Устанавливаем высоту иконки
        checkView.setFitWidth(30);   // Устанавливаем ширину иконки
        saveButton.setGraphic(checkView);
        saveButton.setStyle("-fx-background-color: transparent; -fx-background-radius: 50%;");
        saveButton.setCursor(Cursor.HAND);

        saveButton.setOnAction(e -> {
            String updatedTitle = titleField.getText();
            String updatedDescription = noteArea.getText();

            // Ограничиваем текст до 20 символов, если он длиннее
            String truncatedDescription;
            if (updatedDescription.length() > 20) {
                truncatedDescription = updatedDescription.substring(0, 20) + "...";
            } else {
                truncatedDescription = updatedDescription;
            }

            if (!updatedTitle.trim().isEmpty() && !updatedDescription.trim().isEmpty()) {
                // Обновляем заголовок и описание в оригинальной заметке
                noteTitle.setText(updatedTitle);
                noteDescription.setText(truncatedDescription); // Устанавливаем обрезанное описание
                primaryStage.setScene(mainScene); // Возвращаемся на основной экран
            }
        });

        // Кнопка для отмены
        Button cancelButton = new Button();
        Image backArrowIcon = new Image(getClass().getResourceAsStream("/arrow.png"));
        ImageView backArrowView = new ImageView(backArrowIcon);
        backArrowView.setFitHeight(30);
        backArrowView.setFitWidth(30);
        cancelButton.setGraphic(backArrowView);
        cancelButton.setStyle("-fx-background-color: transparent; -fx-background-radius: 50%;");
        cancelButton.setCursor(Cursor.HAND);

        cancelButton.setOnAction(e -> {
            primaryStage.setScene(mainScene);

            // Попробуем сначала убрать курсор
            mainScene.setCursor(Cursor.NONE);

            // Принудительно установим задержку перед сбросом на DEFAULT
            PauseTransition pause = new PauseTransition(Duration.millis(100));
            pause.setOnFinished(event -> {
                mainScene.setCursor(Cursor.DEFAULT);
            });
            pause.play();
        });

        // Расположение кнопок с использованием BorderPane
        BorderPane buttonPane = new BorderPane();
        buttonPane.setLeft(cancelButton);
        BorderPane.setMargin(cancelButton, new Insets(10, 0, 10, 10));
        buttonPane.setRight(saveButton);
        BorderPane.setMargin(saveButton, new Insets(10, 10, 10, 0));

        // Основной layout для экрана редактирования заметки
        VBox noteEditLayout = new VBox(10, titleField, noteArea, buttonPane);
        noteEditLayout.setPadding(new Insets(20));
        noteEditLayout.setAlignment(Pos.TOP_LEFT);
        noteEditLayout.setSpacing(20);
        noteEditLayout.setStyle("-fx-background-color: #d9f6ff;");

        Scene editScene = new Scene(noteEditLayout, 335, 600);
        editScene.setCursor(Cursor.DEFAULT);
        primaryStage.setScene(editScene);
    }






    // Метод для обновления счетчика заметок
    private void updateNoteCount() {
        noteCountLabel.setText("Total notes: " + noteCount);
    }

    public static void main(String[] args) {
        launch();
    }
}
