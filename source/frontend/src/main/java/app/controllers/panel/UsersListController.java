package app.controllers.panel;

import app.models.GetUsersRequest;
import app.models.User;
import app.models.UserResponseForManager;
import app.utils.SessionManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


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
    @FXML
    private TableView<UserResponseForManager> userTableView;
    @FXML
    private TableColumn<UserResponseForManager, Integer> idColumn;
    @FXML
    private TableColumn<UserResponseForManager, String> usernameColumn;
    @FXML
    private TableColumn<UserResponseForManager, Boolean> blockedColumn;
    @FXML
    private void initialize() {
        getUsers();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        blockedColumn.setCellValueFactory(new PropertyValueFactory<>("blocked"));
    }
    @FXML
    private void getUsers() {
        GetUsersRequest getUsersRequest = new GetUsersRequest(SessionManager.getCurrentUser().getUsername(),SessionManager.getToken());
        try{
            String json = mapper.writeValueAsString(getUsersRequest);
            HttpRequest httpRequest=HttpRequest.newBuilder().uri(new URI("http://localhost:8080/api/manager/get-all-users")).header("Content-Type","application/json").POST(HttpRequest.BodyPublishers.ofString(json)).timeout(Duration.ofSeconds(10)).build();
            new Thread(()->{
                try {
                    HttpResponse<String> response=httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());
                    int status=response.statusCode();
                    javafx.application.Platform.runLater(()->{
                     try {
                         if(status==200||status==204) {
                             ArrayList<UserResponseForManager> users = mapper.readValue(response.body(), new TypeReference<ArrayList<UserResponseForManager>>() {
                             });
                             usersList.addAll(users);
                             userTableView.setItems(usersList);

                         }else  {
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
