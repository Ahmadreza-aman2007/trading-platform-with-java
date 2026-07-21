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
    @FXML private VBox imagesSection;
    @FXML private HBox imagesContainer;

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
        loadImages();
    }

    // بارگذاری عکس‌های آگهی از سرور و نمایش در صفحه جزئیات
    private void loadImages() {
        if (currentAd == null || imagesContainer == null) return;
        new Thread(() -> {
            try {
                java.util.ArrayList<String> images = app.services.AdService.getAdImages(currentAd.getId());
                Platform.runLater(() -> {
                    imagesContainer.getChildren().clear();
                    if (images == null || images.isEmpty()) {
                        if (imagesSection != null) {
                            imagesSection.setVisible(false);
                            imagesSection.setManaged(false);
                        }
                        return;
                    }
                    if (imagesSection != null) {
                        imagesSection.setVisible(true);
                        imagesSection.setManaged(true);
                    }
                    for (String base64 : images) {
                        try {
                            byte[] bytes = java.util.Base64.getDecoder().decode(base64);
                            javafx.scene.image.Image image = new javafx.scene.image.Image(new java.io.ByteArrayInputStream(bytes));
                            javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(image);
                            imageView.setFitWidth(240);
                            imageView.setFitHeight(180);
                            imageView.setPreserveRatio(true);
                            imageView.setStyle("-fx-cursor: hand;");
                            imageView.setOnMouseClicked(ev -> showFullImage(image));
                            imagesContainer.getChildren().add(imageView);
                        } catch (Exception ignored) {
                            // عکس خراب نادیده گرفته می‌شود
                        }
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    if (imagesSection != null) {
                        imagesSection.setVisible(false);
                        imagesSection.setManaged(false);
                    }
                });
            }
        }).start();
    }

    // نمایش عکس در اندازه بزرگ در پنجره جدا (بستن با کلیک یا Esc)
    private void showFullImage(javafx.scene.image.Image image) {
        javafx.stage.Stage stage = new javafx.stage.Stage();
        javafx.scene.image.ImageView big = new javafx.scene.image.ImageView(image);
        big.setPreserveRatio(true);
        javafx.geometry.Rectangle2D bounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        big.setFitWidth(Math.min(image.getWidth() * 2, bounds.getWidth() * 0.85));
        big.setFitHeight(Math.min(image.getHeight() * 2, bounds.getHeight() * 0.85));
        javafx.scene.layout.StackPane root = new javafx.scene.layout.StackPane(big);
        root.setStyle("-fx-background-color: black; -fx-padding: 10; -fx-cursor: hand;");
        javafx.scene.Scene scene = new javafx.scene.Scene(root);
        root.setOnMouseClicked(ev -> stage.close());
        scene.setOnKeyPressed(ev -> {
            if (ev.getCode() == javafx.scene.input.KeyCode.ESCAPE) stage.close();
        });
        stage.setTitle("مشاهده عکس");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
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
                boolean fav = FavoriteService.isFavorite(
                        currentAd.getId());
                Platform.runLater(() -> updateFavoriteButton(fav));
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> updateFavoriteButton(false));
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
                    FavoriteService.removeFavorite(
                            currentAd.getId()
                    );
                } else {
                    FavoriteService.addFavorite(
                            SessionManager.getCurrentUser().getId(),
                            currentAd.getId()
                    );
                }
                Platform.runLater(() -> updateFavoriteButton(!isFavorite));
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    // نمایش پیام خطا
                    System.err.println("❌ خطا در تغییر وضعیت علاقه‌مندی: " + e.getMessage());
                });
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
        if (isOwner) {
            showTemporaryMessage("❌ امکان گفتگو روی آگهی خودتان وجود ندارد");
            return;
        }
        new Thread(() -> {
            try {
                Long sellerId = UserService.getUserIdByUsername(currentAd.getSellerUsername());
                com.fasterxml.jackson.databind.JsonNode conversation =
                        app.services.ChatService.startOrGetConversation(currentAd.getId(), sellerId);
                Long conversationId = conversation.path("id").asLong();
                Platform.runLater(() -> app.utils.ChatWindow.open(
                        titleLabel.getScene().getWindow(),
                        conversationId,
                        "گفتگو با " + currentAd.getSellerUsername()));
            } catch (Exception e) {
                Platform.runLater(() -> showTemporaryMessage("❌ خطا در شروع گفتگو: " + e.getMessage()));
            }
        }).start();
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
                        Scene ratingScene = new Scene(root, 400, 450);
                        ratingScene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
                        stage.setScene(ratingScene);
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
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("پیام");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
