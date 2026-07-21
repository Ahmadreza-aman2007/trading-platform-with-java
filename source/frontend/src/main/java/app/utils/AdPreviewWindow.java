package app.utils;

import app.models.entities.Advertisement;
import app.services.AdService;
import javafx.application.Platform;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Base64;

// ===== پنجره پیش‌نمایش کامل آگهی (متن + عکس‌ها) برای بررسی مدیر =====
public class AdPreviewWindow {

    /**
     * نمایش جزئیات کامل آگهی در یک پنجره جداگانه.
     * اگر onApprove/onReject داده شوند، دکمه‌های تأیید/رد هم داخل پنجره نمایش داده می‌شود.
     */
    public static void open(Window owner, Advertisement ad, Runnable onApprove, Runnable onReject) {
        Stage stage = new Stage();
        stage.setTitle("پیش‌نمایش آگهی: " + ad.getTitle());
        if (owner != null) {
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(owner);
        }

        VBox root = new VBox(12);
        root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        root.setStyle("-fx-padding: 20; -fx-background-color: #f4f7f5;");

        Label title = new Label(ad.getTitle());
        title.setWrapText(true);
        title.setStyle("-fx-font-size: 1.4em; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label price = new Label(String.format("💰 %,d تومان", ad.getPrice()));
        price.setStyle("-fx-font-size: 1.1em; -fx-text-fill: #27ae60; -fx-font-weight: bold;");

        Label meta = new Label("📂 " + ad.getCategory() + "   |   📍 " + ad.getCity()
                + "   |   👤 " + ad.getSellerUsername() + "   |   🕓 " + ad.getCreatedAt());
        meta.setWrapText(true);
        meta.setStyle("-fx-text-fill: #7f8c8d;");

        Label descTitle = new Label("توضیحات:");
        descTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        Label desc = new Label(ad.getDescription() == null || ad.getDescription().isBlank()
                ? "(بدون توضیحات)" : ad.getDescription());
        desc.setWrapText(true);
        desc.setMaxWidth(Double.MAX_VALUE);
        desc.setStyle("-fx-text-fill: #2c3e50; -fx-background-color: white; -fx-padding: 10; -fx-background-radius: 8;");

        Label imagesTitle = new Label("عکس‌های آگهی:");
        imagesTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        FlowPane imagesPane = new FlowPane(10, 10);
        Label imagesStatus = new Label("در حال بارگذاری عکس‌ها...");
        imagesStatus.setStyle("-fx-text-fill: #95a5a6;");
        imagesPane.getChildren().add(imagesStatus);

        // بارگذاری عکس‌ها در پس‌زمینه تا پنجره سریع باز شود
        new Thread(() -> {
            try {
                ArrayList<String> imgs = AdService.getAdImages(ad.getId());
                Platform.runLater(() -> {
                    imagesPane.getChildren().clear();
                    if (imgs == null || imgs.isEmpty()) {
                        Label none = new Label("این آگهی عکسی ندارد.");
                        none.setStyle("-fx-text-fill: #95a5a6;");
                        imagesPane.getChildren().add(none);
                        return;
                    }
                    for (String b64 : imgs) {
                        try {
                            byte[] bytes = Base64.getDecoder().decode(b64);
                            Image img = new Image(new ByteArrayInputStream(bytes));
                            ImageView iv = new ImageView(img);
                            iv.setFitWidth(170);
                            iv.setFitHeight(130);
                            iv.setPreserveRatio(true);
                            iv.setStyle("-fx-cursor: hand;");
                            // با کلیک، عکس در اندازه بزرگ نمایش داده می‌شود
                            iv.setOnMouseClicked(e -> showFullImage(img));
                            imagesPane.getChildren().add(iv);
                        } catch (Exception ignored) { }
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    imagesPane.getChildren().clear();
                    Label err = new Label("خطا در دریافت عکس‌ها: " + e.getMessage());
                    err.setStyle("-fx-text-fill: #e74c3c;");
                    imagesPane.getChildren().add(err);
                });
            }
        }).start();

        VBox content = new VBox(10, title, price, meta, descTitle, desc, imagesTitle, imagesPane);
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(430);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER_LEFT);
        if (onApprove != null) {
            Button approveBtn = new Button("✅ تأیید آگهی");
            approveBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 0.5em 1.5em; -fx-cursor: hand;");
            approveBtn.setOnAction(e -> { stage.close(); onApprove.run(); });
            buttons.getChildren().add(approveBtn);
        }
        if (onReject != null) {
            Button rejectBtn = new Button("❌ رد آگهی");
            rejectBtn.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 0.5em 1.5em; -fx-cursor: hand;");
            rejectBtn.setOnAction(e -> { stage.close(); onReject.run(); });
            buttons.getChildren().add(rejectBtn);
        }
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button closeBtn = new Button("بستن");
        closeBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 0.5em 1.5em; -fx-cursor: hand;");
        closeBtn.setOnAction(e -> stage.close());
        buttons.getChildren().addAll(spacer, closeBtn);

        root.getChildren().addAll(scroll, buttons);

        Scene scene = new Scene(root, 620, 540);
        // استایل اصلی برنامه تا فونت با بقیه صفحه‌ها یکی باشد
        scene.getStylesheets().add(AdPreviewWindow.class.getResource("/css/main.css").toExternalForm());
        ControllersUtils.setRootFontSize(root);
        stage.setScene(scene);
        stage.show();
    }

    // نمایش عکس در اندازه بزرگ؛ با کلیک بسته می‌شود
    private static void showFullImage(Image img) {
        Stage stage = new Stage();
        ImageView iv = new ImageView(img);
        iv.setPreserveRatio(true);
        iv.setFitWidth(Math.min(img.getWidth(), 900));
        iv.setFitHeight(Math.min(img.getHeight(), 650));
        StackPane pane = new StackPane(iv);
        pane.setStyle("-fx-background-color: rgba(0,0,0,0.9); -fx-padding: 10; -fx-cursor: hand;");
        pane.setOnMouseClicked(e -> stage.close());
        stage.setScene(new Scene(pane));
        stage.setTitle("نمایش عکس");
        stage.show();
    }
}
