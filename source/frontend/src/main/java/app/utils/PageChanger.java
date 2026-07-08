package app.utils;

import app.utils.enums.Pages;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class PageChanger {
    public static void changePage(Pages page, Node node) throws IOException {
        switch (page){
            case MAIN_PAGE -> {
                goToMainPage(node);
                break;
            }
            case LOGIN_PAGE -> {
                goToLoginPage(node);
                break;}
            case REGISTER_PAGE -> {
                goToRegisterPage(node);
                break;}
        }
    }
    private static void goToLoginPage(Node node) throws IOException{
        FXMLLoader loader=new FXMLLoader(PageChanger.class.getResource("/views/login.fxml"));
        Stage stage=(Stage) node.getScene().getWindow();
        Scene scene= addMainCss(new Scene(loader.load()));
        String mainPageCssPath=Objects.requireNonNull(PageChanger.class.getResource("/css/login.css")).toExternalForm();
        scene.getStylesheets().add(mainPageCssPath);
        ControllersUtils.setPageSettings(stage,node);
        stage.setScene(scene);
        stage.show();
    }
    private static void goToMainPage(Node node) throws IOException {
        FXMLLoader loader=new FXMLLoader(PageChanger.class.getResource("/views/main_page.fxml"));
        Stage stage=(Stage) node.getScene().getWindow();
        Scene scene= addMainCss(new Scene(loader.load()));
        String mainPageCssPath=Objects.requireNonNull(PageChanger.class.getResource("/css/main_page.css")).toExternalForm();
        scene.getStylesheets().add(mainPageCssPath);
        ControllersUtils.setPageSettings(stage,node);
        stage.setScene(scene);
        stage.show();
    }private static void goToRegisterPage(Node node) throws IOException {
        FXMLLoader loader=new FXMLLoader(PageChanger.class.getResource("/views/register.fxml"));
        Stage stage=(Stage) node.getScene().getWindow();
        Scene scene= addMainCss(new Scene(loader.load()));
        String mainPageCssPath=Objects.requireNonNull(PageChanger.class.getResource("/css/register.css")).toExternalForm();
        scene.getStylesheets().add(mainPageCssPath);
        ControllersUtils.setPageSettings(stage,node);
        stage.setScene(scene);
        stage.show();
    }

    private static   Scene addMainCss(Scene s){
        String cssPath= Objects.requireNonNull(PageChanger.class.getResource("/css/main.css")).toExternalForm();
        s.getStylesheets().add(cssPath);
        return s;
    }
}
