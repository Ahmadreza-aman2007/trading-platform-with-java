package app.controllers.panel.commonUser;

import app.services.ChatService;
import app.utils.ChatWindow;
import app.utils.SessionManager;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.util.List;

public class ConversationsController {

    @FXML private ListView<JsonNode> conversationsList;
    @FXML private Label infoLabel;

    private final ObservableList<JsonNode> conversations = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        conversationsList.setItems(conversations);
        conversationsList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(JsonNode item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    return;
                }
                long myId = SessionManager.getCurrentUser().getId();
                String role = item.path("buyerId").asLong() == myId ? "خریدار" : "فروشنده";
                setText("💬 گفتگو #" + item.path("id").asLong()
                        + "  |  آگهی #" + item.path("adId").asLong()
                        + "  |  نقش شما: " + role);
            }
        });
        conversationsList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                JsonNode selected = conversationsList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    ChatWindow.open(conversationsList.getScene().getWindow(),
                            selected.path("id").asLong(),
                            "آگهی #" + selected.path("adId").asLong());
                }
            }
        });
        refreshList();
    }

    @FXML
    private void refreshList() {
        new Thread(() -> {
            try {
                List<JsonNode> list = ChatService.getMyConversations();
                Platform.runLater(() -> {
                    conversations.setAll(list);
                    infoLabel.setVisible(true);
                    infoLabel.setText(list.isEmpty()
                            ? "هنوز گفتگویی ندارید. برای شروع، از صفحه‌ی جزئیات یک آگهی روی «شروع چت» بزنید."
                            : "برای باز کردن هر گفتگو روی آن دوبار کلیک کنید.");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    infoLabel.setVisible(true);
                    infoLabel.setText("❌ خطا در دریافت گفتگوها: " + e.getMessage());
                });
            }
        }).start();
    }
}
