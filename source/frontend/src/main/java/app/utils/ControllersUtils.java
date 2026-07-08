package app.utils;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ControllersUtils {
    public static void setRootFontSize(Node root) {
        double dpi = Screen.getPrimary().getDpi();
        double scale = dpi / 96;
        double baseSize = scale * 14;
        root.setStyle("-fx-font-size:" + baseSize + "px;");
    }
    public static void setPageFullScreen(Stage stage){
        stage.setMaximized(true);
        stage.setResizable(false);
    }
    public static void setPageSettings(Stage stage, Node node){
        setPageFullScreen(stage);
        setRootFontSize(node);
    }

    public static void clearLabel(Label label) {
        label.setText("");
    }

}
