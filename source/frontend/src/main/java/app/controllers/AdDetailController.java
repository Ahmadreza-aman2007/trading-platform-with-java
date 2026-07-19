package app.controllers;

import app.models.entities.Advertisement;
import app.models.entities.Rating;
import app.models.entities.User;
import app.services.FavoriteService;
import app.services.RatingService;
import app.services.UserService;
import app.utils.ControllersUtils;
import app.utils.SessionManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
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

    public void initialize() {
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

    private void checkIfOwner() {
        User currentUser = SessionManager.getCurrentUser();
        if (currentUser != null && currentAd.getSellerUsername().equals(currentUser.getUsername())) {
            isOwner = true;
            actionButtons.setVisible(false);
            actionButtons.setManaged(false);  // ← رفع اشکال (قبلاً دو خط بود)
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

    private void checkFavoriteStatus() {
        if (isOwner || SessionManager.getCurrentUser() == null) return;
        new Thread(() -> {
            try {
                boolean fav = FavoriteService.isFavorite(SessionManager.getCurrentUser().getId(), currentAd.getId());
                Platform.runLater(() -> updateFavoriteButton(fav));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

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

    @FXML
    private void toggleFavorite() {
        if (isOwner || SessionManager.getCurrentUser() == null) return;
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

    private void loadComments() {
        commentsContainer.getChildren().clear();

        new Thread(() -> {
            try {
                List<Rating> ratings = RatingService.getSellerRatingsByUsername(currentAd.getSellerUsername(),currentAd.getId());

                Platform.runLater(() -> {
                    if (ratings.isEmpty()) {
                        commentsContainer.getChildren().add(new Label("💬 هنوز نظری ثبت نشده است."));
                    } else {
                        for (Rating r : ratings) {
                            VBox commentBox = createCommentBox(r);
                            commentsContainer.getChildren().add(commentBox);
                        }
                    }
                });
            } catch (Exception e) {
                System.err.println("❌ خطا در دریافت نظرات: " + e.getMessage());
                e.printStackTrace();
                Platform.runLater(() -> {
                    commentsContainer.getChildren().add(new Label("❌ خطا در دریافت نظرات: " + e.getMessage()));
                });
            }
        }).start();
    }

    private VBox createCommentBox(Rating rating) {
        VBox box = new VBox(5);
        box.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 8; -fx-padding: 10;");

        // ===== نمایش raterId به همراه یک برچسب بهتر (چون raterUsername در مدل وجود ندارد) =====
        // در صورت اضافه شدن raterUsername به مدل، می‌توانید این بخش را تغییر دهید.
        String displayName = "کاربر شماره " + rating.getRaterId();
        Label userLabel = new Label("👤 " + displayName);
        userLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 0.9em;");

        Label scoreLabel = new Label("⭐ " + rating.getScore() + " از ۵");
        scoreLabel.setStyle("-fx-text-fill: #f39c12;");

        Label commentLabel = new Label(rating.getComment() != null ? rating.getComment() : "نظری ثبت نشده");
        commentLabel.setStyle("-fx-font-size: 0.9em; -fx-text-fill: #2c3e50;");
        commentLabel.setWrapText(true);

        box.getChildren().addAll(userLabel, scoreLabel, commentLabel);
        return box;
    }

    private void loadRatings() {
        new Thread(() -> {
            try {
                List<Rating> ratings = RatingService.getSellerRatingsByUsername(currentAd.getSellerUsername(),currentAd.getId());

                Platform.runLater(() -> {
                    if (ratings.isEmpty()) {
                        ratingLabel.setText("⭐ بدون امتیاز");
                    } else {
                        double avg = ratings.stream().mapToInt(Rating::getScore).average().orElse(0.0);
                        ratingLabel.setText(String.format("⭐ %.1f (%d امتیاز)", avg, ratings.size()));
                    }
                });
            } catch (Exception e) {
                System.err.println("❌ خطا در بارگذاری امتیازات: " + e.getMessage());
                e.printStackTrace();
                Platform.runLater(() -> ratingLabel.setText("⭐ بدون امتیاز"));
            }
        }).start();
    }

    @FXML
    private void startChat() {
        if (SessionManager.getCurrentUser() == null) {
            showTemporaryMessage("❌ لطفاً ابتدا وارد شوید");
            return;
        }
        System.out.println("💬 شروع چت با فروشنده: " + currentAd.getSellerUsername());
        // بعداً پیاده‌سازی می‌شود
    }

    @FXML
    private void openRatingDialog() {
        if (SessionManager.getCurrentUser() == null) {
            showTemporaryMessage("❌ لطفاً ابتدا وارد شوید");
            return;
        }

        new Thread(() -> {
            try {
                Long sellerId = UserService.getUserIdByUsername(currentAd.getSellerUsername());

                Platform.runLater(() -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/rating_dialog.fxml"));
                        Parent root = loader.load();

                        RatingDialogController controller = loader.getController();
                        controller.setAdData(currentAd.getId(), sellerId);

                        Stage stage = new Stage();
                        stage.setTitle("⭐ امتیاز دهی");
                        stage.setScene(new Scene(root, 400, 450));
                        stage.initModality(Modality.WINDOW_MODAL);
                        stage.initOwner(favoriteBtn.getScene().getWindow());
                        stage.showAndWait();

                        loadRatings(); // بارگذاری مجدد امتیازات بعد از بستن دیالوگ

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    System.err.println("❌ خطا در دریافت sellerId: " + e.getMessage());
                });
            }
        }).start();
    }

    @FXML
    private void goBack() {
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }

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

    // ===== متد کمکی برای نمایش پیام موقت (مثلاً در startChat) =====
    private void showTemporaryMessage(String msg) {
        // می‌توانید از یک Alert یا Toast استفاده کنید، فعلاً فقط چاپ در کنسول
        System.out.println(msg);
    }
}
