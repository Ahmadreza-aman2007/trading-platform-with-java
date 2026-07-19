package app.controllers.panel.manager;

import app.models.entities.City;
import app.services.CityService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.List;

public class Cities {

    @FXML
    private TableView<City> cityTableView;

    @FXML
    private TableColumn<City, Long> idColumn;

    @FXML
    private TableColumn<City, String> nameColumn;

    @FXML
    private VBox addCityPanel;

    @FXML
    private TextField cityNameField;

    @FXML
    private Label errorLabel;

    private final ObservableList<City> cityList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        cityTableView.setItems(cityList);

        loadCities();
    }


    @FXML
    private void openAddCityPanel() {
        addCityPanel.setVisible(true);
        addCityPanel.setManaged(true);
        cityNameField.clear();
        errorLabel.setVisible(false);
    }
    @FXML
    private void closeAddCityPanel() {
        addCityPanel.setVisible(false);
        addCityPanel.setManaged(false);
        errorLabel.setVisible(false);
    }


    @FXML
    private void addCity() {
        String name = cityNameField.getText().trim();
        if (name.isEmpty()) {
            showError("❌ نام شهر نمی‌تواند خالی باشد");
            return;
        }

        new Thread(() -> {
            try {
                CityService.sendAddCityRequest(name);

                javafx.application.Platform.runLater(() -> {
                    closeAddCityPanel();
                    loadCities();
                });

            } catch (Exception e) {

                javafx.application.Platform.runLater(() -> {
                    showError("❌ خطا در افزودن شهر: " + e.getMessage());
                });
            }
        }).start();
    }

    private void loadCities() {
        new Thread(() -> {
            try {
                List<City> cities = CityService.sendGetAllCities();

                javafx.application.Platform.runLater(() -> {
                    cityList.clear();
                    cityList.addAll(cities);
                });

            } catch (Exception e) {
                javafx.application.Platform.runLater(() -> {
                    showError("❌ خطا در دریافت لیست شهرها: " + e.getMessage());
                });
            }
        }).start();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
