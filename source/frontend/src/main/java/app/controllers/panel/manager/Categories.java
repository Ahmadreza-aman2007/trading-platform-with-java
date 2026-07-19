package app.controllers.panel.manager;

import app.models.entities.ProductCategory;
import app.services.CategoryService;
import app.utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.List;

public class Categories {

    @FXML
    private TableView<ProductCategory> categoryTableView;

    @FXML
    private TableColumn<ProductCategory, Long> idColumn;

    @FXML
    private TableColumn<ProductCategory, String> nameColumn;

    @FXML
    private VBox addCategoryPanel;

    @FXML
    private TextField categoryNameField;

    @FXML
    private Label errorLabel;

    private final ObservableList<ProductCategory> categoryList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // تنظیم ستون‌ها
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // اتصال لیست به TableView
        categoryTableView.setItems(categoryList);

        // بارگذاری دسته‌بندی‌ها
        loadCategories();
    }

    // ===== باز کردن پنل افزودن =====
    @FXML
    private void openAddCategoryPanel() {
        addCategoryPanel.setVisible(true);
        addCategoryPanel.setManaged(true);
        categoryNameField.clear();
        errorLabel.setVisible(false);
    }

    // ===== بستن پنل افزودن =====
    @FXML
    private void closeAddCategoryPanel() {
        addCategoryPanel.setVisible(false);
        addCategoryPanel.setManaged(false);
        errorLabel.setVisible(false);
    }

    // ===== افزودن دسته‌بندی جدید =====
    @FXML
    private void addCategory() {
        String name = categoryNameField.getText().trim();
        if (name.isEmpty()) {
            showError("❌ نام دسته‌بندی نمی‌تواند خالی باشد");
            return;
        }

        new Thread(() -> {
            try {
                CategoryService.sendAddCategoryRequest(name);

                javafx.application.Platform.runLater(() -> {
                    closeAddCategoryPanel();
                    loadCategories();
                });

            } catch (Exception e) {
                javafx.application.Platform.runLater(() -> {
                    showError("❌ خطا در افزودن دسته‌بندی: " + e.getMessage());
                });
            }
        }).start();
    }

    private void loadCategories() {
        new Thread(() -> {
            try {
                List<ProductCategory> categories = CategoryService.sendGetAllCategories();

                javafx.application.Platform.runLater(() -> {
                    categoryList.clear();
                    categoryList.addAll(categories);
                });

            } catch (Exception e) {
                javafx.application.Platform.runLater(() -> {
                    showError("❌ خطا در دریافت لیست دسته‌بندی‌ها: " + e.getMessage());
                });
            }
        }).start();
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}

