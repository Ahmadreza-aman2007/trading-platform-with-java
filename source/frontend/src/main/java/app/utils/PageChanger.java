package app.utils;

import app.utils.enums.Pages;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class PageChanger {
    public static void changePage(Pages page, Node node) throws IOException {
        switch (page) {
            case MAIN_PAGE -> setPage(node, "/views/main_page.fxml", "/css/main_page.css");
            case LOGIN_PAGE -> setPage(node, "/views/login.fxml", "/css/login.css");
            case REGISTER_PAGE -> setPage(node, "/views/register.fxml", "/css/register.css");
            case PANEL_PAGE -> setPage(node, "/views/user_panel.fxml", "/css/user_panel.css");
        }
    }

    // نکته (ویندوز): به جای ساختن Scene جدید برای هر صفحه، فقط root همان Scene فعلی
    // عوض می‌شود. ساختن Scene جدید روی پنجره‌ی Maximized در ویندوز باعث
    // مینیمایز/غیب شدن پنجره هنگام جابه‌جایی بین صفحات می‌شد.
    private static void setPage(Node node, String fxmlPath, String cssPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(PageChanger.class.getResource(fxmlPath));
        Parent newRoot = loader.load();
        Stage stage = (Stage) node.getScene().getWindow();
        Scene scene = stage.getScene();
        scene.getStylesheets().clear();
        scene.getStylesheets().add(Objects.requireNonNull(PageChanger.class.getResource("/css/main.css")).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(PageChanger.class.getResource(cssPath)).toExternalForm());
        scene.setRoot(newRoot);
        ControllersUtils.setPageSettings(stage, newRoot);
        stage.show();
    }
}
