package app.controllers.panel.commonUser;

import app.models.entities.Advertisement;
import app.models.entities.City;
import app.models.entities.ProductCategory;
import app.services.AdService;
import app.services.CategoryService;
import app.services.CityService;
import app.utils.ControllersUtils;
import app.utils.SessionManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.List;

public class MyAdsController {

    // ===== FXML فیلدها =====
    @FXML private TilePane adsContainer;
    @FXML private Label emptyLabel;

    @FXML private StackPane messageOverlay;
    @FXML private VBox messageBox;
    @FXML private Label messageIcon;
    @FXML private Label messageTitle;
    @FXML private Label messageContent;

    // ===== لیست‌ها =====
    private final ObservableList<Advertisement> myAdsList = FXCollections.observableArrayList();
    private ObservableList<ProductCategory> categories = FXCollections.observableArrayList();
    private ObservableList<City> cities = FXCollections.observableArrayList();

    // ===== مقداردهی اولیه =====
    @FXML
    public void initialize() {
        loadCategoriesAndCities();
        loadMyAds();
        ControllersUtils.setRootFontSize(messageBox);
    }

    // ===== دریافت دسته‌بندی‌ها و شهرها =====
    private void loadCategoriesAndCities() {
        new Thread(() -> {
            try {
                List<ProductCategory> catList = CategoryService.sendGetAllCategories();
                List<City> cityList = CityService.sendGetAllCities();

                Platform.runLater(() -> {
                    categories.clear();
                    categories.addAll(catList);
                    cities.clear();
                    cities.addAll(cityList);
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    showError("❌ خطا", "دریافت لیست شهرها/دسته‌بندی‌ها: " + e.getMessage());
                });
            }
        }).start();
    }

    // ===== بارگذاری آگهی‌های کاربر =====
    private void loadMyAds() {
        new Thread(() -> {
            try {
                List<Advertisement> ads = AdService.sendGetMyAdsRequest(
                        SessionManager.getCurrentUser().getUsername(),
                        SessionManager.getToken()
                );

                Platform.runLater(() -> {
                    myAdsList.clear();
                    myAdsList.addAll(ads);
                    renderAds();
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    System.out.println(e);
                    showError("❌ خطا", "دریافت آگهی‌ها: " + e.getMessage());
                });
            }
        }).start();
    }

    // ===== رندر کارت‌ها =====
    private void renderAds() {
        adsContainer.getChildren().clear();

        if (myAdsList.isEmpty()) {
            emptyLabel.setVisible(true);
            emptyLabel.setManaged(true);
            return;
        }

        emptyLabel.setVisible(false);
        emptyLabel.setManaged(false);

        for (Advertisement ad : myAdsList) {
            adsContainer.getChildren().add(createAdCard(ad));
        }
    }

    // ===== ساخت کارت آگهی =====
    private VBox createAdCard(Advertisement ad) {
        VBox card = new VBox(10);
        card.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 12;
            -fx-padding: 15;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 4);
            -fx-border-color: #ecf0f1;
            -fx-border-radius: 12;
        """);
        card.setPrefWidth(800);
        card.setPrefHeight(1000);

        Label titleLabel = new Label(ad.getTitle());
        titleLabel.setStyle("-fx-font-size: 1.2em; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");titleLabel.setWrapText(true);

        Label priceLabel = new Label(String.format("💰 %,d تومان", ad.getPrice()));
        priceLabel.setStyle("-fx-font-size: 1.1em; -fx-text-fill: #27ae60; -fx-font-weight: bold;");

        Label cityLabel = new Label("📍 " + ad.getCity());
        cityLabel.setStyle("-fx-font-size: 0.95em; -fx-text-fill: #7f8c8d;");

        Label categoryLabel = new Label("📂 " + ad.getCategory());
        categoryLabel.setStyle("-fx-font-size: 0.95em; -fx-text-fill: #7f8c8d;");

        Label statusLabel = new Label("وضعیت: " + translateStatus(ad.getStatus()));
        statusLabel.setStyle("-fx-font-size: 0.95em; -fx-text-fill: " + getStatusColor(ad.getStatus()) + ";");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button editBtn = new Button("✏️ ویرایش");
        editBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 0.4em 1.2em;");
        editBtn.setOnAction(e -> openEditAdDialog(ad));

        Button deleteBtn = new Button("🗑️ حذف");
        deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 0.4em 1.2em;");
        deleteBtn.setOnAction(e -> deleteAd(ad));

        buttonBox.getChildren().addAll(editBtn, deleteBtn);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        card.getChildren().addAll(titleLabel, priceLabel, cityLabel, categoryLabel, statusLabel, spacer, buttonBox);
        return card;
    }

    // ===== ترجمه وضعیت =====
    private String translateStatus(String status) {
        if (status == null) return "نامشخص";
        return switch (status.toUpperCase()) {
            case "PENDING" -> "⏳ در انتظار بررسی";
            case "ACTIVE" -> "✅ فعال";
            case "REJECTED" -> "❌ رد شده";
            case "SOLD" -> "💰 فروخته شده";
            case "DELETED" -> "🗑️ حذف شده";
            default -> status;
        };
    }

    private String getStatusColor(String status) {
        if (status == null) return "#7f8c8d";
        return switch (status.toUpperCase()) {
            case "PENDING" -> "#f39c12";
            case "ACTIVE" -> "#27ae60";
            case "REJECTED" -> "#e74c3c";
            case "SOLD" -> "#2980b9";
            case "DELETED" -> "#95a5a6";
            default -> "#7f8c8d";
        };
    }

    // ===== حذف آگهی =====
    private void deleteAd(Advertisement ad) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("حذف آگهی");
        confirm.setHeaderText(null);
        confirm.setContentText("آیا از حذف آگهی «" + ad.getTitle() + "» اطمینان دارید؟");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                new Thread(() -> {
                    try {
                        AdService.sendRemoveAdRequest(ad.getId());
                        Platform.runLater(() -> {
                            showInfo("🗑️ حذف موفق", "آگهی «" + ad.getTitle() + "» با موفقیت حذف شد.");
                            loadMyAds();
                        });
                    } catch (Exception e) {
                        Platform.runLater(() -> {
                            showError("❌ خطا", "حذف آگهی: " + e.getMessage());
                        });
                    }
                }).start();
            }
        });
    }

    // ===== دیالوگ ثبت آگهی جدید =====
    @FXML
    private void openAddAdDialog() {
        showAdDialog(null);
    }

    // ===== دیالوگ ویرایش آگهی =====
    private void openEditAdDialog(Advertisement ad) {
        showAdDialog(ad);
    }

    // ===== دیالوگ اصلی ثبت/ویرایش (با اندازه‌ی مناسب) =====
    private void showAdDialog(Advertisement existingAd) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);dialog.initOwner(adsContainer.getScene().getWindow());
        dialog.setTitle(existingAd == null ? "📝 ثبت آگهی جدید" : "✏️ ویرایش آگهی");

        VBox root = new VBox(15);

        // ===== تنظیم فونت پایه با ControllersUtils =====
        root.setStyle("-fx-padding: 2.5em; -fx-background-color: #f8f9fa;");

        // ===== عنوان =====
        Label titleLabel = new Label(existingAd == null ? "ثبت آگهی جدید" : "ویرایش آگهی");
        titleLabel.setStyle("-fx-font-size: 1.8em; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // ===== فیلدها =====
        TextField titleField = new TextField();
        titleField.setPromptText("عنوان آگهی");
        titleField.setStyle("-fx-font-size: 1.1em; -fx-padding: 0.6em 0.8em; -fx-background-radius: 8; -fx-border-color: #dcdde1; -fx-border-radius: 8;");
        if (existingAd != null) titleField.setText(existingAd.getTitle());

        TextArea descArea = new TextArea();
        descArea.setPromptText("توضیحات");
        descArea.setPrefRowCount(3);
        descArea.setStyle("-fx-font-size: 1.1em; -fx-padding: 0.6em 0.8em; -fx-background-radius: 8; -fx-border-color: #dcdde1; -fx-border-radius: 8;");
        if (existingAd != null) descArea.setText(existingAd.getDescription());

        TextField priceField = new TextField();
        priceField.setPromptText("قیمت (تومان)");
        priceField.setStyle("-fx-font-size: 1.1em; -fx-padding: 0.6em 0.8em; -fx-background-radius: 8; -fx-border-color: #dcdde1; -fx-border-radius: 8;");
        if (existingAd != null) priceField.setText(String.valueOf(existingAd.getPrice()));

        ComboBox<ProductCategory> categoryCombo = new ComboBox<>(categories);
        categoryCombo.setPromptText("دسته‌بندی");
        categoryCombo.setStyle("-fx-font-size: 1.1em; -fx-padding: 0.4em 0.8em; -fx-background-radius: 8; -fx-border-color: #dcdde1; -fx-border-radius: 8;");
        if (existingAd != null) {
            categoryCombo.getItems().stream()
                    .filter(c -> c.getName().equals(existingAd.getCategory()))
                    .findFirst()
                    .ifPresent(categoryCombo::setValue);
        }

        ComboBox<City> cityCombo = new ComboBox<>(cities);
        cityCombo.setPromptText("شهر");
        cityCombo.setStyle("-fx-font-size: 1.1em; -fx-padding: 0.4em 0.8em; -fx-background-radius: 8; -fx-border-color: #dcdde1; -fx-border-radius: 8;");
        if (existingAd != null) {
            cityCombo.getItems().stream()
                    .filter(c -> c.getName().equals(existingAd.getCity()))
                    .findFirst()
                    .ifPresent(cityCombo::setValue);
        }

        // ===== لیبل خطا =====
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 1em;");
        errorLabel.setVisible(false);

        // ===== دکمه‌ها =====
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveBtn = new Button(existingAd == null ? "💾 ثبت" : "💾 ویرایش");
        saveBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 1.1em; -fx-background-radius: 8; -fx-padding: 0.6em 2em;");

        Button cancelBtn = new Button("❌ انصراف");
        cancelBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-size: 1.1em; -fx-background-radius: 8; -fx-padding: 0.6em 2em;");

        buttonBox.getChildren().addAll(saveBtn, cancelBtn);

        // ===== افزودن همه به root =====
        root.getChildren().addAll(
                titleLabel,
                new Label("عنوان:"), titleField,
                new Label("توضیحات:"), descArea,
                new Label("قیمت:"), priceField,
                new Label("دسته‌بندی:"), categoryCombo,
                new Label("شهر:"), cityCombo,
                errorLabel, buttonBox
        );

        // ===== اندازه‌ی Scene بر اساس صفحه =====

        Scene scene = new Scene(root);
        ControllersUtils.setPageFullScreen(dialog);
        ControllersUtils.setRootFontSize(root);
        dialog.setScene(scene);
        dialog.setResizable(false);

        cancelBtn.setOnAction(e -> dialog.close());

        saveBtn.setOnAction(e -> {
            // ===== گرفتن مقادیر =====
            String title = titleField.getText().trim();
            String description = descArea.getText().trim();
            String priceText = priceField.getText().trim();
            ProductCategory selectedCategory = categoryCombo.getValue();
            City selectedCity = cityCombo.getValue();

            // ===== اعتبارسنجی =====
            if (title.isEmpty()) {
                showErrorLabel(errorLabel, "❌ عنوان نمی‌تواند خالی باشد");
                return;
            }
            if (description.isEmpty()) {
                showErrorLabel(errorLabel, "❌ توضیحات نمی‌تواند خالی باشد");
                return;
            }
            if (priceText.isEmpty()) {
                showErrorLabel(errorLabel, "❌ قیمت را وارد کنید");
                return;
            }
            long price;
            try {
                price = Long.parseLong(priceText);
                if (price <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                showErrorLabel(errorLabel, "❌ قیمت باید یک عدد مثبت باشد");
                return;
            }
            if (selectedCategory == null) {
                showErrorLabel(errorLabel, "❌ دسته‌بندی را انتخاب کنید");
                return;
            }
            if (selectedCity == null) {
                showErrorLabel(errorLabel, "❌ شهر را انتخاب کنید");
                return;
            }

            errorLabel.setVisible(false);
            saveBtn.setDisable(true);
            saveBtn.setText("⏳ در حال ارسال...");

            // ===== ارسال به سرور =====
            new Thread(() -> {
                try {
                    if (existingAd == null) {
                        AdService.sendAddAdRequest(
                                title, description, price,
                                SessionManager.getCurrentUser().getUsername(),
                                selectedCity.getName(),
                                selectedCategory.getName()
                        );
                    } else {
                        AdService.sendEditAdRequest(
                                existingAd.getId(),
                                SessionManager.getCurrentUser().getUsername(),
                                SessionManager.getToken(),
                                title, description, price,
                                selectedCity.getName(),
                                selectedCategory.getName()
                        );
                    }

                    Platform.runLater(() -> {
                        dialog.close();
                        showInfo("✅ موفقیت", existingAd == null ? "آگهی با موفقیت ثبت شد" : "آگهی با موفقیت ویرایش شد");
                        loadMyAds();
                    });

                } catch (Exception ex) {
                    Platform.runLater(() -> {
                        showErrorLabel(errorLabel, "❌ خطا: " + ex.getMessage());
                        saveBtn.setDisable(false);
                        saveBtn.setText(existingAd == null ? "💾 ثبت" : "💾 ویرایش");
                    });
                }
            }).start();
        });

        dialog.showAndWait();
    }

    private void showErrorLabel(Label label, String msg) {
        label.setText(msg);
        label.setVisible(true);
    }

    // ===== نمایش پیام در پنل داخلی =====
    private void showInfo(String title, String content) {
        messageIcon.setText("✅");
        messageIcon.setStyle("-fx-text-fill: #27ae60;");
        messageTitle.setText(title);
        messageTitle.setStyle("-fx-text-fill: #27ae60;");
        messageContent.setText(content);
        messageBox.setStyle("-fx-border-color: #27ae60; -fx-border-width: 2; -fx-border-radius: 12;");
        messageOverlay.setVisible(true);
        messageOverlay.setManaged(true);
    }

    private void showError(String title, String content) {
        messageIcon.setText("❌");
        messageIcon.setStyle("-fx-text-fill: #e74c3c;");
        messageTitle.setText(title);
        messageTitle.setStyle("-fx-text-fill: #e74c3c;");
        messageContent.setText(content);
        messageBox.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2; -fx-border-radius: 12;");
        messageOverlay.setVisible(true);
        messageOverlay.setManaged(true);
    }

    @FXML
    private void closeMessagePanel() {
        messageOverlay.setVisible(false);
        messageOverlay.setManaged(false);
    }
}