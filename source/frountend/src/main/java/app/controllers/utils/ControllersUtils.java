package app.controllers.utils;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;

public class ControllersUtils {
    public static void setRootFontSize(StackPane root) {
        double dpi = Screen.getPrimary().getDpi();
        double scale = dpi / 96;
        double baseSize = scale * 14;
        root.setStyle("-fx-font-size:" + baseSize + "px;");
    }

    public static void clearLable(Label label) {
        label.setText("");
    }
}
