package app.controllers.mainPage;

import app.controllers.AdDetailController;
import app.models.entities.Advertisement;
import app.models.entities.City;
import app.models.entities.ProductCategory;
import app.services.AdService;
import app.services.CategoryService;
import app.services.CityService;
import app.utils.ControllersUtils;
import app.utils.PageChanger;
import app.utils.SessionManager;
import app.utils.enums.Pages;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MainController {

    // ===== FXML فیلدها =====
    @FXML private StackPane root;
    @FXML private VBox searchPanel;
    @FXML private VBox mainPage;
    @FXML private Button loginButton;
    @FXML private Button userPanel;
    @FXML private TilePane adsContainer;
    @FXML private Label emptyLabel;

    // ===== فیلدهای جستجوی پیشرفته =====
    @FXML private TextField keywordField;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private ComboBox<String> cityCombo;

    @FXML private Slider minPriceSlider;
    @FXML private Slider maxPriceSlider;
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;
    @FXML private Label minPriceLabel;
    @FXML private Label maxPriceLabel;
    @FXML private Label priceErrorLabel;

    // ===== افکت‌ها و لیست‌ها =====
    private final BoxBlur blurEffect = new BoxBlur(9, 9, 3);
    private final ObservableList<Advertisement> approvedAdsList = FXCollections.observableArrayList();

    // ===== مقداردهی اولیه =====
    @FXML
    public void initialize() {
        ControllersUtils.setRootFontSize(root);

        searchPanel.setVisible(false);
        searchPanel.setManaged(false);

        updateLoginStatus();

        // ===== تنظیم تعداد ستون‌های TilePane بر اساس عرض =====
        adsContainer.widthProperty().addListener((obs, old, newWidth) -> {
            double width = newWidth.doubleValue();
            int columns = (int) Math.floor(width / 370);
            adsContainer.setPrefColumns(Math.max(1, Math.min(columns, 4)));
        });

        // ===== تنظیم کنترل‌های قیمت =====
        setupPriceControls();

        // ===== بارگذاری داده‌ها =====
        loadApprovedAds();
        loadCategoriesAndCities();
    }

    // ============================================================
    // ===== بخش قیمت (اسلایدر + TextField + اعتبارسنجی) =====
    // ============================================================

    private void setupPriceControls() {
        // ۱. اسلایدر → TextField
        minPriceSlider.valueProperty().addListener((obs, old, val) -> {
            long value = val.longValue();
            minPriceField.setText(formatPrice(value));
            validatePriceRange();
        });

        maxPriceSlider.valueProperty().addListener((obs, old, val) -> {
            long value = val.longValue();
            maxPriceField.setText(formatPrice(value));
            validatePriceRange();
        });

        // ۲. TextField → اسلایدر
        minPriceField.textProperty().addListener((obs, old, val) -> {
            Long newValue = parsePrice(val);
            if (newValue != null && newValue >= 0 && newValue <= 1_000_000_000L) {
                minPriceSlider.setValue(newValue);
                validatePriceRange();
            }
        });

        maxPriceField.textProperty().addListener((obs, old, val) -> {
            Long newValue = parsePrice(val);
            if (newValue != null && newValue >= 0 && newValue <= 1_000_000_000L) {
                maxPriceSlider.setValue(newValue);
                validatePriceRange();
            }
        });

        // ۳. مقداردهی اولیه TextFieldها
        minPriceField.setText(formatPrice(0L));maxPriceField.setText(formatPrice(1_000_000_000L));
    }

    private void validatePriceRange() {
        long min = (long) minPriceSlider.getValue();
        long max = (long) maxPriceSlider.getValue();

        if (min > max) {
            priceErrorLabel.setText("❌ قیمت از نمی‌تواند از قیمت تا بیشتر باشد");
            priceErrorLabel.setVisible(true);
            priceErrorLabel.setManaged(true);

            // اصلاح خودکار: قیمت از را برابر قیمت تا قرار بده
            minPriceSlider.setValue(max);
            minPriceField.setText(formatPrice(max));
        } else {
            priceErrorLabel.setVisible(false);
            priceErrorLabel.setManaged(false);
        }
    }

    private String formatPrice(long value) {
        return String.format("%,d", value);
    }

    private Long parsePrice(String text) {
        if (text == null || text.trim().isEmpty()) return null;
        try {
            String clean = text.replace(",", "").trim();
            return Long.parseLong(clean);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private long getMinPrice() {
        return (long) minPriceSlider.getValue();
    }

    private long getMaxPrice() {
        return (long) maxPriceSlider.getValue();
    }

    // ============================================================
    // ===== بارگذاری داده‌ها (آگهی‌ها، دسته‌بندی‌ها، شهرها) =====
    // ============================================================

    private void loadApprovedAds() {
        new Thread(() -> {
            try {
                List<Advertisement> ads = AdService.sendGetApprovedAdsRequest();
                Platform.runLater(() -> {
                    approvedAdsList.clear();
                    approvedAdsList.addAll(ads);
                    renderAds();
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    System.err.println("❌ خطا در دریافت آگهی‌ها: " + e.getMessage());
                    showEmptyMessage("❌ خطا در دریافت آگهی‌ها");
                });
            }
        }).start();
    }

    private void loadCategoriesAndCities() {
        new Thread(() -> {
            try {
                List<ProductCategory> categories = CategoryService.sendGetAllCategories();
                List<City> cities = CityService.sendGetAllCities();

                Platform.runLater(() -> {
                    // ===== دسته‌بندی‌ها =====
                    categoryCombo.getItems().clear();
                    categoryCombo.getItems().add("همه دسته‌بندی‌ها");
                    for (ProductCategory c : categories) {
                        categoryCombo.getItems().add(c.getName());
                    }
                    categoryCombo.setValue("همه دسته‌بندی‌ها");

                    // ===== شهرها =====
                    cityCombo.getItems().clear();
                    cityCombo.getItems().add("همه شهرها");
                    for (City c : cities) {
                        cityCombo.getItems().add(c.getName());
                    }
                    cityCombo.setValue("همه شهرها");
                });

            } catch (Exception e) {
                System.err.println("❌ خطا در دریافت لیست‌ها: " + e.getMessage());
            }
        }).start();
    }

    // ============================================================
    // ===== رندر کارت‌های آگهی =====
    // ============================================================

    private void renderAds() {
        adsContainer.getChildren().clear();

        if (approvedAdsList.isEmpty()) {
            showEmptyMessage("❌ هیچ آگهی تأییدشده‌ای وجود ندارد.");
            return;
        }

        emptyLabel.setVisible(false);
        emptyLabel.setManaged(false);

        for (Advertisement ad : approvedAdsList) {
            VBox card = createAdCard(ad);
            adsContainer.getChildren().add(card);
        }
    }

    private VBox createAdCard(Advertisement ad) {
        VBox card = new VBox(10);
        card.setStyle("""
            -fx-background-color: white;-fx-background-radius: 12;
            -fx-padding: 15;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 4);
            -fx-border-color: #ecf0f1;
            -fx-border-radius: 12;
        """);
        card.setPrefWidth(600);
        card.setPrefHeight(450);
        card.setMaxWidth(Double.MAX_VALUE);

        Label titleLabel = new Label(ad.getTitle());
        titleLabel.setStyle("-fx-font-size: 1.2em; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(280);

        Label priceLabel = new Label(String.format("💰 %,d تومان", ad.getPrice()));
        priceLabel.setStyle("-fx-font-size: 1.1em; -fx-text-fill: #27ae60; -fx-font-weight: bold;");

        Label cityLabel = new Label("📍 " + ad.getCity());
        cityLabel.setStyle("-fx-font-size: 0.95em; -fx-text-fill: #7f8c8d;");

        Label categoryLabel = new Label("📂 " + ad.getCategory());
        categoryLabel.setStyle("-fx-font-size: 0.95em; -fx-text-fill: #7f8c8d;");

        Label sellerLabel = new Label("👤 " + ad.getSellerUsername());
        sellerLabel.setStyle("-fx-font-size: 0.95em; -fx-text-fill: #7f8c8d;");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button detailBtn = new Button("📋 مشاهده جزئیات");
        detailBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 0.5em 1.5em;");
        detailBtn.setOnAction(e -> openAdDetail(ad));

        card.getChildren().addAll(
                titleLabel, priceLabel, cityLabel, categoryLabel, sellerLabel,
                spacer, detailBtn
        );

        return card;
    }

    // ============================================================
    // ===== جستجوی پیشرفته =====
    // ============================================================

    @FXML
    private void handleAdvancedSearch() {
        String keyword = keywordField.getText().trim();
        String category = categoryCombo.getValue();
        String city = cityCombo.getValue();
        Long minPrice = getMinPrice();
        Long maxPrice = getMaxPrice();

        // ===== اعتبارسنجی نهایی =====
        if (minPrice > maxPrice) {
            showError("❌ قیمت از نمی‌تواند از قیمت تا بیشتر باشد");
            return;
        }

        new Thread(() -> {
            try {
                List<Advertisement> results = AdService.sendAdvancedSearchRequest(
                        keyword, category, city, minPrice, maxPrice
                );

                Platform.runLater(() -> {
                    approvedAdsList.clear();
                    approvedAdsList.addAll(results);
                    renderAds();
                    closeSearchBar();

                    if (results.isEmpty()) {
                        showEmptyMessage("🔍 هیچ آگهی با این شرایط یافت نشد.");
                    }
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    showEmptyMessage("❌ خطا در جستجو: " + e.getMessage());
                });
            }
        }).start();
    }

    // ============================================================
    // ===== ناوبری =====
    // ============================================================

    @FXML
    public void goToLoginPage() {
        try {
            PageChanger.changePage(Pages.LOGIN_PAGE, root);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void goToPanelPage() {
        try {
            PageChanger.changePage(Pages.PANEL_PAGE, root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ============================================================
    // ===== پنل جستجوی پیشرفته (باز/بسته) =====
    // ============================================================

    @FXML
    private void openSearchBar() {
        searchPanel.setVisible(true);
        searchPanel.setManaged(true);
        mainPage.setEffect(blurEffect);mainPage.setDisable(true);
    }

    @FXML
    private void closeSearchBar() {
        searchPanel.setVisible(false);
        searchPanel.setManaged(false);
        mainPage.setEffect(null);
        mainPage.setDisable(false);
    }

    // ============================================================
    // ===== جزئیات آگهی =====
    // ============================================================

    private void openAdDetail(Advertisement ad) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ad_detail.fxml"));
            Parent root = loader.load();

            AdDetailController controller = loader.getController();
            controller.setAdvertisement(ad);

            Stage stage = new Stage();
            stage.setTitle("جزئیات آگهی");
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/main.css")).toExternalForm());
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(adsContainer.getScene().getWindow());
            ControllersUtils.setPageSettings(stage, root);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ============================================================
    // ===== پیام‌ها و وضعیت‌ها =====
    // ============================================================

    private void showEmptyMessage(String msg) {
        adsContainer.getChildren().clear();
        emptyLabel.setText(msg);
        emptyLabel.setVisible(true);
        emptyLabel.setManaged(true);
    }

    private void showError(String msg) {
        // نمایش خطا در یک Alert ساده یا هر جای دیگر
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("خطا");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void refreshAds() {
        loadApprovedAds();
    }

    // ============================================================
    // ===== وضعیت ورود =====
    // ============================================================

    private void updateLoginStatus() {
        if (SessionManager.isLoggedIn()) {
            loginButton.setVisible(false);
            loginButton.setManaged(false);
            userPanel.setVisible(true);
            userPanel.setManaged(true);

            if (SessionManager.getCurrentUser().getRole().equals("MANAGER")) {
                // اگر مدیر بود، به صفحه‌ی مدیریت برود
                goToLoginPage();
            }
        } else {
            loginButton.setVisible(true);
            loginButton.setManaged(true);
            userPanel.setVisible(false);
            userPanel.setManaged(false);
        }
    }
}