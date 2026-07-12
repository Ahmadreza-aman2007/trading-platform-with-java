package app.controllers.registerPage;

import app.utils.ControllersUtils;
import app.utils.InputValidator;
import app.models.requests.shared.RegisterRequest;
import app.utils.PageChanger;
import app.utils.enums.Pages;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static app.utils.ControllersUtils.clearLabel;

public class RegisterController {
    @FXML
    private StackPane root;
    @FXML
    private TextField fullnameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField rPasswordField;
    @FXML
    private Label fullnameError;
    @FXML
    private Label usernameError;
    @FXML
    private Label phoneNumberError;
    @FXML
    private Label passError;
    @FXML
    private Label rPassError;
    @FXML
    private Label messageLabel;
    @FXML
    private Label errorLabel;
    private HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    public void initialize() {
        ControllersUtils.setRootFontSize(root);
        addValidators();
        errorHandling();
    }

    @FXML
    public void resetFields() {
        fullnameField.setText("");
        phoneNumberField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        rPasswordField.setText("");
    }

    @FXML
    public void register() {
        String fullname = fullnameField.getText();
        String username = usernameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String pass = passwordField.getText();
        String rPass = rPasswordField.getText();
        String emptyError = "این فیلد نمیتواند خالی باشد";
        boolean hasError = false;
        if (fullname.isEmpty()) {
            fullnameError.setText(emptyError);
            hasError = true;
        }
        if (username.isEmpty()) {
            usernameError.setText(emptyError);
            hasError = true;
        }
        if (phoneNumber.isEmpty()) {
            phoneNumberError.setText(emptyError);
            hasError = true;
        }
        if (pass.isEmpty()) {
            passError.setText(emptyError);
            hasError = true;
        }
        if (rPass.isEmpty()) {
            rPassError.setText(emptyError);
            hasError = true;
        }
        if (hasError) {
            return;
        }
        if (!InputValidator.isThisAPhoneNumber(phoneNumber)) {
            phoneNumberError.setText("شماره تلفن باید 11 رقم باشد و با 09 شروع شود");
            return;
        }
        if (!pass.equals(rPass)) {
            rPassError.setText("همخوانی ندارد");
            return;
        }
        sendRegisterRequest(fullname, username, phoneNumber, pass);
    }

    private void sendRegisterRequest(String fullname, String username, String phoneNumber, String pass) {
        RegisterRequest requestBody = new RegisterRequest(fullname, username, phoneNumber, pass);
        try {
            String json = objectMapper.writeValueAsString(requestBody);
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/api/auth/register"))
                    .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json))
                    .timeout(Duration.ofSeconds(10)).build();
            new Thread(() -> {
                try {
                    HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                    int statusCode = response.statusCode();
                    String responseBody = response.body();
                    javafx.application.Platform.runLater(() -> {
                        if (statusCode == 200 || statusCode == 201) {
                            showMessage("ثبت نام با موفقیت انجام شد");
                            goToLoginPage();
                        } else if (statusCode == 409) {
                            showMessage("");
                            showError("این نام کاربری یا شماره تلفن قبلا ثبت نام شده است");
                        } else {
                            showError("خطا در ثبت نام" + responseBody);
                        }
                    });
                } catch (Exception e) {
                    javafx.application.Platform.runLater(() -> {
                        showError("خطا در ارتباط با سرور" + e.getMessage());
                    });
                }
            }).start();
        } catch (Exception e) {
            showError(e.getMessage());
        }

    }

    private void showMessage(String text) {
        messageLabel.setText(text);
        messageLabel.setVisible(true);
    }

    private void showError(String text) {
        errorLabel.setText(text);
        errorLabel.setVisible(true);
    }

    @FXML
    public void goToLoginPage() {
        try {
            PageChanger.changePage(Pages.LOGIN_PAGE, root);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addValidators() {
        InputValidator.letterWithSpace(fullnameField);
        InputValidator.numberOnly(phoneNumberField);
        InputValidator.letterWithNumberWithDotUnderLine(usernameField);
        InputValidator.letterWithNumberWithDotUnderLine(passwordField);
        InputValidator.letterWithNumberWithDotUnderLine(rPasswordField);
    }

    public void errorHandling() {
        fullnameField.textProperty().addListener((observableValue, s, t1) -> {
            clearLabel(fullnameError);
        });
        phoneNumberField.textProperty().addListener(((observableValue, s, t1) -> clearLabel(phoneNumberError)));
        usernameField.textProperty().addListener(((observableValue, s, t1) -> clearLabel(usernameError)));
        passwordField.textProperty().addListener(((observableValue, s, t1) -> clearLabel(passError)));
        rPasswordField.textProperty().addListener(((observableValue, s, t1) -> clearLabel(rPassError)));
    }
}
