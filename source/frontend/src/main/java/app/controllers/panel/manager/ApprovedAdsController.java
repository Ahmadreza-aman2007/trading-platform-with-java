package app.controllers.panel.manager;

import app.models.entities.Advertisement;
import app.services.AdService;
import app.utils.AdPreviewWindow;
import app.utils.SessionManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class ApprovedAdsController {

    @FXML private TableView<Advertisement> approvedAdsTable;
    @FXML private TableColumn<Advertisement, Long> idColumn;
    @FXML private TableColumn<Advertisement, String> titleColumn;
    @FXML private TableColumn<Advertisement, Long> priceColumn;
    @FXML private TableColumn<Advertisement, String> sellerColumn;
    @FXML private TableColumn<Advertisement, String> cityColumn;
    @FXML private TableColumn<Advertisement, String> categoryColumn;
    @FXML private TableColumn<Advertisement, String> createdAtColumn;
    @FXML private TableColumn<Advertisement, Void> actionColumn;
    @FXML private Label infoLabel;

    private final ObservableList<Advertisement> approvedAdsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        sellerColumn.setCellValueFactory(new PropertyValueFactory<>("sellerUsername"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        // ستون عملیات: مشاهده کامل آگهی + حذف آگهی تأییدشده توسط مدیر
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final HBox buttonBox = new HBox(8);
            private final Button viewBtn = new Button("👁 مشاهده");
            private final Button removeBtn = new Button("🗑️ حذف");

            {
                viewBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                removeBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

                viewBtn.setOnAction(event -> {
                    Advertisement ad = getTableView().getItems().get(getIndex());
                    AdPreviewWindow.open(getTableView().getScene().getWindow(), ad, null, null);
                });

                // حذف با تأیید دومرحله‌ای تا اشتباهی پیش نیاید
                removeBtn.setOnAction(event -> {
                    Advertisement ad = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                            "آگهی «" + ad.getTitle() + "» برای همیشه حذف شود؟",
                            ButtonType.YES, ButtonType.NO);
                    confirm.getDialogPane().getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
                    confirm.setTitle("حذف آگهی");
                    confirm.setHeaderText(null);
                    confirm.showAndWait().ifPresent(bt -> {
                        if (bt == ButtonType.YES) removeAd(ad.getId());
                    });
                });

                buttonBox.getChildren().addAll(viewBtn, removeBtn);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonBox);
                }
            }
        });

        approvedAdsTable.setItems(approvedAdsList);
        loadApprovedAds();
    }

    private void loadApprovedAds() {
        new Thread(() -> {
            try {
                ArrayList<Advertisement> ads = AdService.sendGetApprovedAdsRequest();

                Platform.runLater(() -> {
                    approvedAdsList.clear();
                    approvedAdsList.addAll(ads);
                    showInfo("✅ لیست آگهی‌های تأییدشده به‌روز شد (" + ads.size() + " مورد)");
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    showError("❌ خطا در دریافت لیست آگهی‌ها: " + e.getMessage());
                });
            }
        }).start();
    }

    // حذف آگهی تأییدشده توسط مدیر
    private void removeAd(Long adId) {
        new Thread(() -> {
            try {
                AdService.removeAd(adId, SessionManager.getCurrentUser().getUsername(), SessionManager.getToken());

                Platform.runLater(() -> {
                    showInfo("🗑️ آگهی با موفقیت حذف شد");
                    loadApprovedAds();
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    showError("❌ خطا در حذف آگهی: " + e.getMessage());
                });
            }
        }).start();
    }

    @FXML
    private void refreshList() {
        loadApprovedAds();
    }

    private void showInfo(String msg) {
        infoLabel.setText(msg);
        infoLabel.setVisible(true);
        infoLabel.setStyle("-fx-text-fill: #2c3e50;");
    }

    private void showError(String msg) {
        infoLabel.setText(msg);
        infoLabel.setVisible(true);
        infoLabel.setStyle("-fx-text-fill: #e74c3c;");
    }
}