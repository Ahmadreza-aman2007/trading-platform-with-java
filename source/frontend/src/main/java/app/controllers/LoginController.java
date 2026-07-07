package app.controllers;

import app.models.User;
import app.utils.ControllersUtils;
import app.utils.InputValidator;
import app.models.LoginRequest;
import app.utils.SessionManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;

import static app.utils.ControllersUtils.clearLable;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passField;
    @FXML
    private Label usernameError;
    @FXML
    private Label passError;
    @FXML
    private StackPane root;

    private HttpClient httpClient=HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private ObjectMapper objectMapper=new ObjectMapper();
    @FXML
    public void login() {
        String username = usernameField.getText();
        String pass = passField.getText();
        boolean hasError=false;
        if (username.isEmpty()) {
            usernameError.setText("نام کاربری خود را وارد کنید");
            hasError=true;
        }
        if (pass.isEmpty()) {
            passError.setText("رمز را وارد کنید");
            hasError=true;
        }
        if (hasError){
            return;
        }
        sendLoginRequest(username,pass);
    }
    private void sendLoginRequest(String username,String password){
        LoginRequest loginRequest=new LoginRequest(username,password);
        try {
            String json=objectMapper.writeValueAsString(loginRequest);
            HttpRequest httpRequest= HttpRequest.newBuilder().uri(new URI("http://localhost:8080/api/auth/login")).header("Content-Type","application/json").POST(HttpRequest.BodyPublishers.ofString(json)).timeout(Duration.ofSeconds(10)).build();
            new Thread(()->{
                try{
                    HttpResponse<String> httpResponse=httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());
                    int statusCode=httpResponse.statusCode();
                    switch (statusCode){
                        case 200:{
                            JsonNode jsonNode=objectMapper.readTree(httpResponse.body());
                            String rToken =jsonNode.get("token").asText();
                            String rUsername=jsonNode.get("username").asText();
                            String rRole=jsonNode.get("role").asText();
                            String rFullname=jsonNode.get("fullname").asText();
                            String rPhoneNumber=jsonNode.get("phoneNumber").asText();
                            User u= new User(rUsername,rPhoneNumber,rRole,rFullname);
                            SessionManager.setCurrentUser(u);
                            SessionManager.setToken(rToken);
                            javafx.application.Platform.runLater(this::goToMainPage);
                            break;
                        }
                        case 404:{
                            javafx.application.Platform.runLater(()->usernameError.setText("نام کاربری یافت نشد."));
                            break;
                        }
                        case 401:{
                            javafx.application.Platform.runLater(()->passError.setText("رمز عبور اشنباه است"));
                            break;
                        }
                        case 500:{
                            javafx.application.Platform.runLater(()->passError.setText("خطای سرور"));
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }).start();
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }


    @FXML
    public void initialize() {
        ControllersUtils.setRootFontSize(root);
        setValidators();
        errorHandling();
    }

    @FXML
    public void goToRegisterPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/register.fxml"));
            Scene s = new Scene(loader.load());
            String mainCss = Objects.requireNonNull(getClass().getResource("/css/main.css")).toExternalForm();
            String registerCss = Objects.requireNonNull(getClass().getResource("/css/register.css")).toExternalForm();
            Stage stage = (Stage) root.getScene().getWindow();
            s.getStylesheets().addAll(mainCss, registerCss);
            stage.setScene(s);
            stage.show();
        } catch (Exception e) {
            System.err.println("error in go to register page    " + e.getMessage());
        }
    }

    public  void goToMainPage(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main_page.fxml"));
            Scene s = new Scene(loader.load());
            String mainCss = Objects.requireNonNull(getClass().getResource("/css/main.css")).toExternalForm();
            Stage stage = (Stage) root.getScene().getWindow();
            s.getStylesheets().addAll(mainCss);
            stage.setScene(s);
            stage.show();
        } catch (Exception e) {
            System.err.println("error in go to main page    " + e.getMessage());
        }
    }

    public void setValidators() {
        InputValidator.letterWithNumberWithDotUnderLine(usernameField);
        InputValidator.letterWithNumberWithDotUnderLine(passField);
    }

    public void errorHandling() {
        usernameField.textProperty().addListener(((observableValue, s, t1) -> clearLable(usernameError)));
        passField.textProperty().addListener(((observableValue, s, t1) -> clearLable(passError)));
    }
}
