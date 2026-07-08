package app.controllers;

import app.utils.ControllersUtils;
import app.utils.PageChanger;
import app.utils.enums.Pages;
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
            PageChanger.changePage(Pages.LOGIN_PAGE,root);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        ControllersUtils.setRootFontSize(root);
    }
}
