package app.controllers.panel;

import app.models.entities.User;
import app.models.requests.commonUser.AddAdRequest;
import app.models.requests.commonUser.EditAdRequest;
import app.models.requests.commonUser.RemoveAdRequest;
import app.models.requests.manager.AddCategoryRequest;
import app.models.requests.manager.AddCityRequest;
import app.utils.ControllersUtils;
import app.utils.PageChanger;
import app.utils.SessionManager;
import app.utils.enums.Pages;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class PanelController {

    @FXML
    private BorderPane root;
    @FXML
    private StackPane contentContainer;
    @FXML
    private VBox commonUserPanelList;
    @FXML
    private VBox managerPanelList;


    private HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private ObjectMapper objectMapper = new ObjectMapper();

    private boolean isManager;

    @FXML
    private void goToMainPage() {
        try {
            PageChanger.changePage(Pages.MAIN_PAGE, root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void exitOfAccount() {
        SessionManager.logout();
        goToMainPage();
    }

    @FXML
    private void initialize() {
        ControllersUtils.setRootFontSize(root);
        setupAccessControlForUser();
        goToDashboard();
        test();

    }
    private void test(){
    sendAddCategoryRequest("لباس زیر");
    sendCityRequest("مشهد");
    sendAddAdRequest("1","2",1000,SessionManager.getCurrentUser().getUsername(),"لباس زیر","مشهد");
    sendEditAdRequest(1l,SessionManager.getCurrentUser().getUsername(),SessionManager.getToken(),"2","3",10000,"null","null");
    sendRemoveAdRequest(1l);
    }
    private void sendRemoveAdRequest(Long id){
        RemoveAdRequest removeAdRequest = new RemoveAdRequest(id,SessionManager.getCurrentUser().getUsername(),SessionManager.getToken());
        try{
            String json = objectMapper.writeValueAsString(removeAdRequest);
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/api/user/remove-ad"))
                    .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json))
                    .timeout(Duration.ofSeconds(10)).build();
            new Thread(() -> {
                try {
                    HttpResponse<String> httpResponse=httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());
                    if(httpResponse.statusCode()==200){
                        System.out.println("successful");
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }).start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private void sendEditAdRequest(Long id,String username,String token,String title,String description,long price,String city,String category){
        EditAdRequest request = new EditAdRequest(id,token,title,description,price,username,city,category);
        try{
            String json = objectMapper.writeValueAsString(request);
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/api/user/edit-ad"))
                    .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json))
                    .timeout(Duration.ofSeconds(10)).build();
            new Thread(() -> {
                try {
                    HttpResponse<String> httpResponse=httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());
                    if(httpResponse.statusCode()==200){
                        System.out.println("successful");
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }).start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void sendAddAdRequest(String title, String description, long price, String sellerUsername, String city , String category){
        AddAdRequest addAdRequest=new AddAdRequest(SessionManager.getToken(),title,description,price,sellerUsername,city,category);
        try{
            String json = objectMapper.writeValueAsString(addAdRequest);
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/api/user/add-ad"))
                    .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json))
                    .timeout(Duration.ofSeconds(10)).build();
            new Thread(() -> {
                try {
                    HttpResponse<String> httpResponse=httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());
                    if(httpResponse.statusCode()==200){
                        System.out.println("successful");
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }).start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void sendAddCategoryRequest(String categoryName) {
        AddCategoryRequest addCategoryRequest=new AddCategoryRequest(SessionManager.getCurrentUser().getUsername(),SessionManager.getToken(),categoryName);
        try{
            String json = objectMapper.writeValueAsString(addCategoryRequest);
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/api/manager/add-product-category"))
                    .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json))
                    .timeout(Duration.ofSeconds(10)).build();
            new Thread(() -> {
                try {
                    HttpResponse<String> httpResponse=httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());
                    if(httpResponse.statusCode()==200){
                        System.out.println("successful");
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }).start();

        }catch (Exception e){
            System.err.println(e.getMessage());
        }

    }
    private void sendEditCategoryRequest(){
    }

    private void sendCityRequest(String cityName){
        AddCityRequest addCityRequest=new AddCityRequest(SessionManager.getCurrentUser().getUsername(),SessionManager.getToken(),cityName);
        try{
            String json = objectMapper.writeValueAsString(addCityRequest);
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/api/manager/add-city"))
                    .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json))
                    .timeout(Duration.ofSeconds(10)).build();
            new Thread(() -> {
                try {
                    HttpResponse<String> httpResponse=httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());
                    if(httpResponse.statusCode()==200){
                        System.out.println("successful");
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }).start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    @FXML
    private void goToDashboard() {
        setContent("/views/panel/dashboard.fxml");
    }

    @FXML
    private void goToUsersList() {
        setContent("/views/panel/users_list.fxml");
    }

    private void setupAccessControlForUser() {
        User u = SessionManager.getCurrentUser();
        if (u == null) {
            System.err.println("error in get user role");
            return;
        }
        System.err.println(u.getRole());
        if (u.getRole().equals("COMMON_USER")) {
            isManager = false;
            managerPanelList.setVisible(false);
            managerPanelList.setManaged(false);
            commonUserPanelList.setVisible(true);
            commonUserPanelList.setManaged(true);
        } else {
            isManager = true;
            commonUserPanelList.setVisible(false);
            commonUserPanelList.setManaged(false);
            managerPanelList.setVisible(true);
            managerPanelList.setManaged(true);

        }
    }

    public void setContent(String fxmlpath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlpath));
            Node Content = loader.load();
            contentContainer.getChildren().clear();
            contentContainer.getChildren().add(Content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
