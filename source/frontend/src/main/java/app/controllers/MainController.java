package app.controllers;

import app.utils.ControllersUtils;
import app.utils.PageChanger;
import app.utils.enums.Pages;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainController {
    @FXML
    private StackPane root;
    @FXML
    private VBox searchPanel;
    @FXML
    private VBox mainPage;

    private BoxBlur blurEffect=new BoxBlur(9,9,3);
    @FXML
    public void goToLoginPage() {
        try {
            PageChanger.changePage(Pages.LOGIN_PAGE, root);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    private void openSearchBar(){
        searchPanel.setVisible(true);
        mainPage.setEffect(blurEffect);
        mainPage.setDisable(true);
    }
    @FXML
    private void closeSearchBar(){
        searchPanel.setVisible(false);
        mainPage.setEffect(null);
        mainPage.setDisable(false);
    }
    @FXML
    public void initialize() {
        ControllersUtils.setRootFontSize(root);
        searchPanel.setVisible(false);
    }
}
