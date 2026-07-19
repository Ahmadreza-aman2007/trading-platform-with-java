package app.controllers.panel.manager;

import app.models.entities.Advertisement;
import app.services.AdService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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