package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception{
        URL fxmlURL=getClass().getResource("/views/main_page.fxml");
        FXMLLoader loader =new FXMLLoader(fxmlURL);
        Scene scene =new Scene(loader.load());
        addMainCss(scene);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/main_page.css")).toExternalForm());
        stage.setTitle("سمساری احمد و پارسا");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.show();
    }

    public   Scene addMainCss(Scene s){
        String cssPath= Objects.requireNonNull(getClass().getResource("/css/main.css")).toExternalForm();
        s.getStylesheets().add(cssPath);
        return s;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
