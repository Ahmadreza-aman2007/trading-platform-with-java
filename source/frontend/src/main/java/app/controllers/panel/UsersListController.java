package app.controllers.panel;

import app.models.requests.manager.EditUserRequest;
import app.models.requests.manager.GetUsersRequest;
import app.models.response.manager.UserResponseForManager;
import app.utils.SessionManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;

public class UsersListController {
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private ObservableList<UserResponseForManager> usersList = FXCollections.observableArrayList();
    private int editUserId;
    private boolean blocked;
    @FXML
    private TableView<UserResponseForManager> userTableView;
    @FXML
    private TableColumn<UserResponseForManager, Integer> idColumn;
    @FXML
    private TableColumn<UserResponseForManager, String> usernameColumn;
    @FXML
    private TableColumn<UserResponseForManager, Boolean> blockedColumn;
    @FXML
    private TableColumn<UserResponseForManager, Void> actionColumn;
    @FXML
    private VBox usersListVbox;
    @FXML
    private VBox editPanelVbox;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label fullnameLabel;
    @FXML
    private Label phoneNumberLabel;
    @FXML
    private Label createdDateLabel;
    @FXML
    private Label errorMessage;
    @FXML
    private CheckBox blockCheckBox;

    @FXML
    private void initialize() {
        getUsers();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(100);
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameColumn.setPrefWidth(200);
        blockedColumn.setCellValueFactory(new PropertyValueFactory<>("blocked"));
        blockedColumn.setPrefWidth(400);
        actionColumn.setCellFactory(param -> new TableCell<UserResponseForManager, Void>() {
            private final Button button = new Button("ویرایش");
            {
                button.setOnAction(event -> {
                    UserResponseForManager user = (UserResponseForManager) getTableView().getItems().get(getIndex());
                    openEditUserPanel(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    button.setText("ویرایش");
                    setGraphic(button);
                }
            }
        });
        actionColumn.setPrefWidth(400);
    }

    @FXML
    private void backToUsersList() {
        usersList.clear();
        getUsers();
        usersListVbox.setManaged(true);
        usersListVbox.setVisible(true);
        editPanelVbox.setManaged(false);
        editPanelVbox.setVisible(false);
    }

    @FXML
    private void openEditUserPanel(UserResponseForManager user) {
        usersListVbox.setVisible(false);
        usersListVbox.setManaged(false);
        editPanelVbox.setVisible(true);
        editPanelVbox.setManaged(true);
        usernameLabel.setText(user.getUsername());
        fullnameLabel.setText(user.getFullname());
        phoneNumberLabel.setText(user.getPhoneNumber());
        createdDateLabel.setText(user.getCreatedDate());
        blocked = user.isBlocked();
        blockCheckBox.setSelected(user.isBlocked());
        editUserId = user.getId();
    }

    @FXML
    private void sendEditRequest() {
        EditUserRequest editUserRequest = new EditUserRequest(SessionManager.getCurrentUser().getUsername(),
                SessionManager.getToken(), editUserId, blocked);
        if (blockCheckBox.isSelected() == editUserRequest.isBlocked()) {
            errorMessage.setText("هیچ چیز تغییر داده نشد");
            return;
        }
        editUserRequest.setBlocked(blockCheckBox.isSelected());
        try {
            String json = mapper.writeValueAsString(editUserRequest);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/manager/editUser"))
                    .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json))
                    .timeout(Duration.ofSeconds(10)).build();
            new Thread(() -> {
                try {
                    HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                    int status = response.statusCode();
                    if (status == 200) {
                        backToUsersList();
                    } else {
                        // TODO:this is for other status codes
                    }
                } catch (IOException | InterruptedException e) {
                    System.err.println(e.getMessage());
                }
            }).start();
        } catch (Exception e) {
            errorMessage.setText(e.getMessage());
        }
    }

    @FXML
    private void getUsers() {
        GetUsersRequest getUsersRequest = new GetUsersRequest(SessionManager.getCurrentUser().getUsername(),
                SessionManager.getToken());
        try {
            String json = mapper.writeValueAsString(getUsersRequest);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/manager/get-all-users"))
                    .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json))
                    .timeout(Duration.ofSeconds(10)).build();
            new Thread(() -> {
                try {
                    HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                    int status = response.statusCode();
                    javafx.application.Platform.runLater(() -> {
                        try {
                            if (status == 200 || status == 204) {
                                ArrayList<UserResponseForManager> users = mapper.readValue(response.body(),
                                        new TypeReference<ArrayList<UserResponseForManager>>() {
                                        });
                                usersList.addAll(users);
                                userTableView.setItems(usersList);
                            } else {
                                // TODO:this is for other status codes
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    });
                } catch (IOException | InterruptedException e) {
                    System.out.println(e);
                }
            }).start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
