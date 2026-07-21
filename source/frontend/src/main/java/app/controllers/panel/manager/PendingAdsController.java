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

public class PendingAdsController {

    @FXML private TableView<Advertisement> pendingAdsTable;
    @FXML private TableColumn<Advertisement, Long> idColumn;
    @FXML private TableColumn<Advertisement, String> titleColumn;
    @FXML private TableColumn<Advertisement, Long> priceColumn;
    @FXML private TableColumn<Advertisement, String> sellerColumn;
    @FXML private TableColumn<Advertisement, String> cityColumn;
    @FXML private TableColumn<Advertisement, String> categoryColumn;
    @FXML private TableColumn<Advertisement, String> createdAtColumn;
    @FXML private TableColumn<Advertisement, Void> actionColumn;
    @FXML private Label infoLabel;

    private final ObservableList<Advertisement> pendingAdsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // تنظیم ستون‌ها
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        sellerColumn.setCellValueFactory(new PropertyValueFactory<>("sellerUsername"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final HBox buttonBox = new HBox(8);
            private final Button viewBtn = new Button("👁 مشاهده");
            private final Button approveBtn = new Button("✅ تأیید");
            private final Button rejectBtn = new Button("❌ رد");
            private final Button removeBtn = new Button("🗑️ حذف");

            {
                viewBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                approveBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
                rejectBtn.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");
                removeBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

                approveBtn.setOnAction(event -> {
                    Advertisement ad = getTableView().getItems().get(getIndex());
                    changeAdStatus(ad.getId(), "APPROVED", null);
                });

                rejectBtn.setOnAction(event -> {
                    Advertisement ad = getTableView().getItems().get(getIndex());
                    // طبق داک پروژه: مدیر می‌تواند هنگام رد، توضیح (اختیاری) بنویسد
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.getDialogPane().getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
                    dialog.setTitle("رد آگهی");
                    dialog.setHeaderText("دلیل رد آگهی «" + ad.getTitle() + "» (اختیاری)");
                    dialog.setContentText("توضیح:");
                    dialog.showAndWait().ifPresent(note -> changeAdStatus(ad.getId(), "REJECTED", note));
                });

                removeBtn.setOnAction(event -> {
                    Advertisement ad = getTableView().getItems().get(getIndex());
                    removeAd(ad.getId());
                });

                // دکمه مشاهده: پیش‌نمایش کامل آگهی و عکس‌ها قبل از تصمیم‌گیری
                viewBtn.setOnAction(event -> {
                    Advertisement ad = getTableView().getItems().get(getIndex());
                    AdPreviewWindow.open(getTableView().getScene().getWindow(), ad,
                            () -> changeAdStatus(ad.getId(), "APPROVED", null),
                            () -> {
                                TextInputDialog dialog = new TextInputDialog();
                                dialog.getDialogPane().getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
                                dialog.setTitle("رد آگهی");
                                dialog.setHeaderText("دلیل رد آگهی «" + ad.getTitle() + "» (اختیاری)");
                                dialog.setContentText("توضیح:");
                                dialog.showAndWait().ifPresent(note -> changeAdStatus(ad.getId(), "REJECTED", note));
                            });
                });

                buttonBox.getChildren().addAll(viewBtn, approveBtn, rejectBtn, removeBtn);
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

        pendingAdsTable.setItems(pendingAdsList);
        loadPendingAds();
    }

    private void loadPendingAds() {
        new Thread(() -> {
            try {
                ArrayList<Advertisement> ads = AdService.sendGetPendingAdRequest();

                Platform.runLater(() -> {
                    pendingAdsList.clear();
                    pendingAdsList.addAll(ads);
                    showInfo("✅ لیست آگهی‌ها به‌روز شد (" + ads.size() + " مورد)");
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    System.err.println(e);
                    showError("❌ خطا در دریافت لیست آگهی‌ها: " + e.getMessage());
                });
            }
        }).start();
    }

    private void changeAdStatus(Long adId, String newStatus, String note) {
        new Thread(() -> {
            try {
                AdService.changeAdStatus(adId, newStatus, note, SessionManager.getCurrentUser().getUsername(), SessionManager.getToken());

                Platform.runLater(() -> {
                    showInfo("✅ وضعیت آگهی با موفقیت تغییر کرد");
                    loadPendingAds(); // به‌روزرسانی لیست
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    showError("❌ خطا در تغییر وضعیت: " + e.getMessage());
                });
            }
        }).start();
    }

    private void removeAd(Long adId) {
        new Thread(() -> {
            try {
                AdService.removeAd(adId, SessionManager.getCurrentUser().getUsername(), SessionManager.getToken());

                Platform.runLater(() -> {
                    showInfo("🗑️ آگهی با موفقیت حذف شد");
                    loadPendingAds();
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
        loadPendingAds();
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