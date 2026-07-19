package app.controllers;

import app.models.entities.Advertisement;
import app.models.entities.Rating;
import app.models.entities.User;
import app.services.FavoriteService;
import app.services.RatingService;
import app.utils.ControllersUtils;
import app.utils.SessionManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class AdDetailController {

    @FXML private Label titleLabel;
    @FXML private Label priceLabel;
    @FXML private Label categoryLabel;
    @FXML private Label cityLabel;
    @FXML private Label statusLabel;
    @FXML private Label createdAtLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label sellerLabel;
    @FXML private Label ratingLabel;

    @FXML private Button favoriteBtn;
    @FXML private HBox actionButtons;
    @FXML private VBox commentsContainer;
    @FXML private VBox commentsSection;

    private Advertisement currentAd;
    private boolean isFavorite = false;
    private boolean isOwner = false;

    public void initialize(){
        ControllersUtils.setRootFontSize(titleLabel);
    }
    public void setAdvertisement(Advertisement ad) {
        this.currentAd = ad;
        displayAdDetails();
        checkIfOwner();
        checkFavoriteStatus();
        loadComments();
        loadRatings();
    }

    // ===== نمایش اطلاعات آگهی =====
    private void displayAdDetails() {
        if (currentAd == null) return;
        titleLabel.setText(currentAd.getTitle());
        priceLabel.setText(String.format("💰 %,d تومان", currentAd.getPrice()));
        categoryLabel.setText(currentAd.getCategory());
        cityLabel.setText(currentAd.getCity());
        statusLabel.setText(translateStatus(currentAd.getStatus()));
        createdAtLabel.setText(currentAd.getCreatedAt());
        descriptionLabel.setText(currentAd.getDescription());
        sellerLabel.setText("👤 " + currentAd.getSellerUsername());
    }

    // ===== بررسی اینکه کاربر صاحب آگهی است =====
    private void checkIfOwner() {
        User currentUser = SessionManager.getCurrentUser();
        if (currentUser != null && currentAd.getSellerUsername().equals(currentUser.getUsername())) {
            isOwner = true;
            actionButtons.setVisible(false);
            actionButtons.setManaged(

                    false);
            commentsSection.setVisible(false);
            commentsSection.setManaged(false);
            favoriteBtn.setVisible(false);
            favoriteBtn.setManaged(false);
        } else {
            isOwner = false;
            actionButtons.setVisible(true);
            actionButtons.setManaged(true);
            commentsSection.setVisible(true);
            commentsSection.setManaged(true);
            favoriteBtn.setVisible(true);
            favoriteBtn.setManaged(true);
        }
    }
    // ===== تنظیم آگهی =====

    // ===== بررسی وضعیت علاقه‌مندی =====
    private void checkFavoriteStatus() {
        if (isOwner) return;
        new Thread(() -> {
            try {
                boolean fav = FavoriteService.isFavorite(SessionManager.getCurrentUser().getId(), currentAd.getId());
                Platform.runLater(() -> updateFavoriteButton(fav));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // ===== به‌روزرسانی دکمه‌ی علاقه‌مندی =====
    private void updateFavoriteButton(boolean fav) {
        isFavorite = fav;
        if (isFavorite) {
            favoriteBtn.setText("⭐");
            favoriteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #f1c40f; -fx-font-size: 2em;");
        } else {
            favoriteBtn.setText("☆");
            favoriteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #bdc3c7; -fx-font-size: 2em;");
        }
    }

    // ===== تغییر وضعیت علاقه‌مندی =====
    @FXML
    private void toggleFavorite() {
        if (isOwner) return;
        new Thread(() -> {
            try {
                if (isFavorite) {
                    FavoriteService.removeFavorite(SessionManager.getCurrentUser().getId(), currentAd.getId());
                } else {
                    FavoriteService.addFavorite(SessionManager.getCurrentUser().getId(), currentAd.getId());
                }
                Platform.runLater(() -> updateFavoriteButton(!isFavorite));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // ===== بارگذاری کامنت‌ها =====
    private void loadComments() {
        // بعداً از سرور می‌گیریم
        commentsContainer.getChildren().clear();
        commentsContainer.getChildren().add(new Label("💬 نظر خود را ثبت کنید (در حال توسعه)"));
    }

    // ===== بارگذاری امتیازها =====
    private void loadRatings() {
        new Thread(() -> {
            try {
                double avg = RatingService.getAverageScore(currentAd.getSellerUsername());
                int count = RatingService.getRatingCount(currentAd.getSellerUsername());
                Platform.runLater(() -> {
                    ratingLabel.setText(String.format("⭐ %.1f (%d امتیاز)", avg, count));
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // ===== شروع چت =====
    @FXML
    private void startChat() {
        System.out.println("💬 شروع چت با فروشنده: " + currentAd.getSellerUsername());
        // بعداً پیاده‌سازی می‌شود
    }

    // ===== باز کردن دیالوگ امتیازدهی =====
    @FXML
    private void openRatingDialog() {
        System.out.println("⭐ باز کردن پنجره امتیازدهی برای آگهی: " + currentAd.getId());
        // بعداً پیاده‌سازی می‌شود
    }

    // ===== بازگشت =====
    @FXML
    private void goBack() {
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }

    // ===== ترجمه وضعیت =====
    private String translateStatus(String status) {
        if (status == null) return "نامشخص";
        return switch (status.toUpperCase()) {
            case "PENDING" -> "⏳ در انتظار بررسی";
            case "APPROVED" -> "✅ فعال";
            case "REJECTED" -> "❌ رد شده";
            case "SOLD" -> "💰 فروخته شده";
            case "DELETED" -> "🗑️ حذف شده";
            default -> status;
        };
    }
}