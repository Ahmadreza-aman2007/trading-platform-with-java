package app.controllers;

import app.services.RatingService;
import app.utils.SessionManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class RatingDialogController {

    @FXML private ComboBox<Integer> scoreCombo;
    @FXML private TextArea commentArea;
    @FXML private Label errorLabel;

    private Long adId;
    private Long sellerId;

    public void setAdData(Long adId, Long sellerId) {
        this.adId = adId;
        this.sellerId = sellerId;
    }

    @FXML
    private void submitRating() {
        Integer score = scoreCombo.getValue();
        String comment = commentArea.getText().trim();

        if (score == null) {
            showError("❌ لطفاً امتیاز را انتخاب کنید");
            return;
        }

        if (comment.length() > 500) {
            showError("❌ نظر نمی‌تواند بیشتر از ۵۰۰ کاراکتر باشد");
            return;
        }

        // غیرفعال کردن دکمه‌ها (اختیاری)
        // ...

        new Thread(() -> {
            try {
                RatingService.addRating(
                        SessionManager.getCurrentUser().getId(),
                        sellerId,
                        adId,
                        score,
                        comment,
                        SessionManager.getCurrentUser().getUsername(),
                        SessionManager.getToken()
                );

                Platform.runLater(() -> {
                    // ===== پیام موفقیت قبل از بستن =====
                    showSuccess("✅ امتیاز شما با موفقیت ثبت شد");
                    closeDialog();
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    showError("❌ " + e.getMessage());
                });
            }
        }).start();
    }

    @FXML
    private void closeDialog() {
        Stage stage = (Stage) scoreCombo.getScene().getWindow();
        stage.close();
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
        // حذف استایل قبلی
        errorLabel.setStyle("-fx-text-fill: red;");
    }

    private void showSuccess(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
        errorLabel.setStyle("-fx-text-fill: green;");
    }
}