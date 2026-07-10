package app.controllers;

import app.models.User;
import app.utils.ControllersUtils;
import app.utils.PageChanger;
import app.utils.SessionManager;
import app.utils.enums.Pages;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class PanelController {

    @FXML
    private BorderPane root;
    @FXML
    private StackPane contentContainer;
    @FXML
    private VBox commonUserPanelList;
    @FXML
    private VBox managerPanelList;

    private boolean isManager;
    @FXML
    private void goToMainPage() {
        try{
            PageChanger.changePage(Pages.MAIN_PAGE,root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void exitOfAccount(){
        SessionManager.logout();
        goToMainPage();
    }
    @FXML
    private void initialize(){
        ControllersUtils.setRootFontSize(root);
        setupAccessControlForUser();
        goToDashboard();
    }
    @FXML
    private void goToDashboard(){
        setContent("/views/panel/dashboard.fxml");
    }
    @FXML
    private  void goToUsersList(){setContent("/views/panel/users_list.fxml");}
    private void  setupAccessControlForUser(){
        User u=SessionManager.getCurrentUser();
        if (u==null){
            System.err.println("error in get user role");
            return;
        }
        System.err.println(u.getRole());
        if (u.getRole().equals("COMMON_USER")){
            isManager=false;
            managerPanelList.setVisible(false);
            managerPanelList.setManaged(false);
            commonUserPanelList.setVisible(true);
            commonUserPanelList.setManaged(true);
        }
        else{ isManager=true;
            commonUserPanelList.setVisible(false);
            commonUserPanelList.setManaged(false);
            managerPanelList.setVisible(true);
            managerPanelList.setManaged(true);

        }
    }
    public void setContent(String fxmlpath){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlpath));
            Node Content= loader.load();
            contentContainer.getChildren().clear();
            contentContainer.getChildren().add(Content);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
