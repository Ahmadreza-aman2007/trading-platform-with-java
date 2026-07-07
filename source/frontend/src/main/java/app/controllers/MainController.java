package app.controllers;

import app.utils.ControllersUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Objects;

public class MainController {
    @FXML
    private StackPane root;

    @FXML
    public void goToLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Scene s = new Scene(loader.load());
            String mainCssPath = Objects.requireNonNull(getClass().getResource("/css/main.css")).toExternalForm();
            String loginCssPath = Objects.requireNonNull(getClass().getResource("/css/login.css")).toExternalForm();
            s.getStylesheets().addAll(mainCssPath, loginCssPath);
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(s);
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        ControllersUtils.setRootFontSize(root);
    }
}
