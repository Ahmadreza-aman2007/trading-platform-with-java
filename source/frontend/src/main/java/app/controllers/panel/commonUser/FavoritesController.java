package app.controllers.panel.commonUser;

import app.models.entities.Advertisement;
import app.services.FavoriteService;
import app.utils.SessionManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.List;

// تب آگهی های نشان شده
public class FavoritesController {

    @FXML private TilePane adsContainer;
    @FXML private Label emptyLabel;

    @FXML
    public void initialize() {
        loadFavorites();
    }

    @FXML
    private void refreshList() {
        loadFavorites();
    }

    private void loadFavorites() {
        new Thread(() -> {
            try {
                List<Advertisement> ads = FavoriteService.getUserFavorites(SessionManager.getCurrentUser().getId());
                Platform.runLater(() -> render(ads));
            } catch (Exception e) {
                Platform.runLater(() -> {
                    adsContainer.getChildren().clear();
                    showMessage("❌ خطا در دریافت علاقه‌مندی‌ها: " + e.getMessage());
                });
            }
        }).start();
    }

    private void render(List<Advertisement> ads) {
        adsContainer.getChildren().clear();
        if (ads.isEmpty()) {
            showMessage("⭐ هنوز آگهی‌ای به علاقه‌مندی‌ها اضافه نکرده‌اید.");
            return;
        }
        emptyLabel.setVisible(false);
        emptyLabel.setManaged(false);
        for (Advertisement ad : ads) {
            adsContainer.getChildren().add(createCard(ad));
        }
    }

    private VBox createCard(Advertisement ad) {
        VBox card = new VBox(10);
        card.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 12;
            -fx-padding: 15;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 4);
            -fx-border-color: #ecf0f1;
            -fx-border-radius: 12;
        """);
        card.setPrefWidth(320);

        Label titleLabel = new Label(ad.getTitle());
        titleLabel.setStyle("-fx-font-size: 1.2em; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        titleLabel.setWrapText(true);

        Label priceLabel = new Label(String.format("💰 %,d تومان", ad.getPrice()));
        priceLabel.setStyle("-fx-font-size: 1.1em; -fx-text-fill: #27ae60; -fx-font-weight: bold;");

        Label cityLabel = new Label("📍 " + ad.getCity());
        cityLabel.setStyle("-fx-font-size: 0.95em; -fx-text-fill: #7f8c8d;");

        Label categoryLabel = new Label("📂 " + ad.getCategory());
        categoryLabel.setStyle("-fx-font-size: 0.95em; -fx-text-fill: #7f8c8d;");

        Label sellerLabel = new Label("👤 " + ad.getSellerUsername());
        sellerLabel.setStyle("-fx-font-size: 0.95em; -fx-text-fill: #7f8c8d;");

        Button removeBtn = new Button("💔 حذف از علاقه‌مندی‌ها");
        removeBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 0.4em 1.2em; -fx-cursor: hand;");
        removeBtn.setOnAction(e -> removeFavorite(ad));

        HBox buttons = new HBox(10, removeBtn);
        buttons.setAlignment(Pos.CENTER);

        card.getChildren().addAll(titleLabel, priceLabel, cityLabel, categoryLabel, sellerLabel, buttons);
        return card;
    }

    private void removeFavorite(Advertisement ad) {
        new Thread(() -> {
            try {
                FavoriteService.removeFavorite(ad.getId());
                Platform.runLater(this::loadFavorites);
            } catch (Exception e) {
                Platform.runLater(() -> showMessage("❌ خطا در حذف: " + e.getMessage()));
            }
        }).start();
    }

    private void showMessage(String msg) {
        emptyLabel.setText(msg);
        emptyLabel.setVisible(true);
        emptyLabel.setManaged(true);
    }
}
