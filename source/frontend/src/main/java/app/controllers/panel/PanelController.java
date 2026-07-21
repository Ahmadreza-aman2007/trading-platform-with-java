package app.controllers.panel;

import app.models.entities.Advertisement;
import app.models.entities.User;
import app.models.requests.commonUser.AddAdRequest;
import app.models.requests.commonUser.EditAdRequest;
import app.models.requests.commonUser.RemoveAdRequest;
import app.models.requests.manager.AddCategoryRequest;
import app.models.requests.manager.AddCityRequest;
import app.models.requests.manager.GetPendingAdsRequest;
import app.utils.ControllersUtils;
import app.utils.PageChanger;
import app.utils.SessionManager;
import app.utils.enums.Pages;
import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.ArrayList;

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

    }
    @FXML
    private void goToDashboard() {
        setContent("/views/panel/dashboard.fxml");
    }
    @FXML
    private void goToCategories() {
        setContent("/views/panel/categories.fxml");
    }
    @FXML
    private void goToCities() {
        setContent("/views/panel/cities.fxml");
    }
    @FXML
    private void goToPendingAdvertisements() {
        setContent("/views/panel/pending_ads.fxml");
    }

    @FXML
    private void goToApprovedAdvertisements() {
        setContent("/views/panel/approved_ads.fxml");
    }
    @FXML
    private void goToMyAds() {
        setContent("/views/panel/my_ads.fxml");
    }
    @FXML
    private void goToFavorites() {
        setContent("/views/panel/favorites.fxml");
    }
    @FXML
    private void goToConversations() {
        setContent("/views/panel/conversations.fxml");
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
