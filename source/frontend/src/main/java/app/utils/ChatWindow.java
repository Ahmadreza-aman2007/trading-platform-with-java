package app.utils;

import app.services.ChatService;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.application.Platform;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.List;

// ===== پنجره‌ی گفتگو (چت) با ظاهر هماهنگ با بقیه‌ی برنامه =====
public class ChatWindow {

    public static void open(Window owner, Long conversationId, String title) {
        Stage stage = new Stage();
        stage.setTitle("💬 " + title);
        if (owner != null) {
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(owner);
        }

        VBox root = new VBox(10);
        root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        root.setStyle("-fx-padding: 15; -fx-background-color: #f8f9fa;");

        Label header = new Label("💬 " + title);
        header.setStyle("-fx-font-size: 1.2em; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        VBox messagesBox = new VBox(8);
        messagesBox.setStyle("-fx-padding: 10;");
        ScrollPane scrollPane = new ScrollPane(messagesBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(380);
        scrollPane.setStyle("-fx-background: white; -fx-background-color: white; -fx-background-radius: 12;");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #e74c3c;");
        errorLabel.setVisible(false);

        TextField input = new TextField();
        input.setPromptText("پیام خود را بنویسید...");
        HBox.setHgrow(input, Priority.ALWAYS);
        input.setStyle("-fx-background-radius: 8; -fx-border-color: #dcdde1; -fx-border-radius: 8; -fx-padding: 0.5em 0.8em;");

        Button sendBtn = new Button("ارسال");
        sendBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 0.5em 1.5em; -fx-cursor: hand;");

        Button refreshBtn = new Button("🔄");
        refreshBtn.setStyle("-fx-background-color: #4a90e2; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 0.5em 0.8em; -fx-cursor: hand;");

        Runnable loadMessages = () -> new Thread(() -> {
            try {
                List<JsonNode> messages = ChatService.getMessages(conversationId);
                Platform.runLater(() -> {
                    messagesBox.getChildren().clear();
                    if (messages.isEmpty()) {
                        Label empty = new Label("هنوز پیامی رد و بدل نشده است.");
                        empty.setStyle("-fx-text-fill: #95a5a6;");
                        messagesBox.getChildren().add(empty);
                    }
                    for (JsonNode m : messages) {
                        boolean mine = m.path("senderId").asLong() == SessionManager.getCurrentUser().getId();
                        VBox bubble = new VBox(2);
                        Label sender = new Label(m.path("senderUsername").asText());
                        sender.setStyle("-fx-font-size: 0.8em; -fx-text-fill: #7f8c8d;");
                        Label content = new Label(m.path("content").asText());
                        content.setWrapText(true);
                        content.setStyle("-fx-text-fill: #2c3e50;");
                        Label time = new Label(m.path("time").asText());
                        time.setStyle("-fx-font-size: 0.75em; -fx-text-fill: #95a5a6;");
                        bubble.getChildren().addAll(sender, content, time);
                        bubble.setMaxWidth(320);
                        bubble.setStyle(mine
                                ? "-fx-background-color: #d5f5e3; -fx-background-radius: 10; -fx-padding: 8;"
                                : "-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-padding: 8; -fx-border-color: #ecf0f1; -fx-border-radius: 10;");
                        HBox row = new HBox(bubble);
                        row.setAlignment(mine ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
                        messagesBox.getChildren().add(row);
                    }
                    errorLabel.setVisible(false);
                    scrollPane.setVvalue(1.0);
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    errorLabel.setText("❌ خطا در دریافت پیام‌ها: " + e.getMessage());
                    errorLabel.setVisible(true);
                });
            }
        }).start();

        // ارسال پیام و رفرش لیست بعد از موفقیت
        Runnable send = () -> {
            String text = input.getText() == null ? "" : input.getText().trim();
            if (text.isEmpty()) return;
            sendBtn.setDisable(true);
            new Thread(() -> {
                try {
                    ChatService.sendMessage(conversationId, text);
                    Platform.runLater(() -> {
                        input.clear();
                        sendBtn.setDisable(false);
                        loadMessages.run();
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        sendBtn.setDisable(false);
                        errorLabel.setText("❌ " + e.getMessage());
                        errorLabel.setVisible(true);
                    });
                }
            }).start();
        };

        sendBtn.setOnAction(e -> send.run());
        input.setOnAction(e -> send.run());
        refreshBtn.setOnAction(e -> loadMessages.run());

        HBox inputRow = new HBox(8, input, sendBtn, refreshBtn);
        inputRow.setAlignment(Pos.CENTER);

        root.getChildren().addAll(header, scrollPane, errorLabel, inputRow);

        Scene scene = new Scene(root, 520, 560);
        // استایل اصلی برنامه تا فونت با بقیه صفحه ها یکی باشد
        scene.getStylesheets().add(ChatWindow.class.getResource("/css/main.css").toExternalForm());
        ControllersUtils.setRootFontSize(root);
        stage.setScene(scene);
        loadMessages.run();
        stage.show();
    }
}
