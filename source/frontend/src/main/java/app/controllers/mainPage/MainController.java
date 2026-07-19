package app.controllers.mainPage;

import app.controllers.AdDetailController;
import app.models.entities.Advertisement;
import app.services.AdService;
import app.utils.ControllersUtils;
import app.utils.PageChanger;
import app.utils.SessionManager;
import app.utils.enums.Pages;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MainController {
    @FXML
    private StackPane root;
    @FXML
    private VBox searchPanel;
    @FXML
    private VBox mainPage;
    @FXML
    private Button loginButton;
    @FXML
    private Button userPanel;
    @FXML
    private BoxBlur blurEffect=new BoxBlur(9,9,3);
    @FXML private TilePane adsContainer;
    @FXML private Label emptyLabel;

    private final ObservableList<Advertisement> approvedAdsList = FXCollections.observableArrayList();

    @FXML
    public void goToLoginPage() {
        try {
            PageChanger.changePage(Pages.LOGIN_PAGE, root);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    private void goToPanelPage(){
        try{
            PageChanger.changePage(Pages.PANEL_PAGE,root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void openSearchBar(){
        searchPanel.setVisible(true);
        searchPanel.setManaged(true);
        mainPage.setEffect(blurEffect);
        mainPage.setDisable(true);
    }
    @FXML
    private void closeSearchBar(){
        searchPanel.setVisible(false);
        searchPanel.setManaged(false);
        mainPage.setEffect(null);
        mainPage.setDisable(false);
    }
    @FXML
    public void initialize() {
        ControllersUtils.setRootFontSize(root);
        searchPanel.setVisible(false);
        searchPanel.setManaged(false);
        updateLoginStatus();
        adsContainer.widthProperty().addListener((obs, old, newWidth) -> {
            double width = newWidth.doubleValue();
            int columns = (int) Math.floor(width / 370);
            adsContainer.setPrefColumns(Math.max(1, Math.min(columns, 4)));
        });

        loadApprovedAds();
    }
    private void loadApprovedAds() {
        new Thread(() -> {
            try {
                List<Advertisement> ads = AdService.sendGetApprovedAdsRequest();

                Platform.runLater(() -> {
                    approvedAdsList.clear();
                    approvedAdsList.addAll(ads);
                    renderAds();
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    System.err.println("❌ خطا در دریافت آگهی‌ها: " + e.getMessage());
                    showEmptyMessage("❌ خطا در دریافت آگهی‌ها");
                });
            }
        }).start();
    }
    private void renderAds() {
        adsContainer.getChildren().clear();

        if (approvedAdsList.isEmpty()) {
            showEmptyMessage("❌ هیچ آگهی تأییدشده‌ای وجود ندارد.");
            return;
        }

        emptyLabel.setVisible(false);
        emptyLabel.setManaged(false);

        for (Advertisement ad : approvedAdsList) {
            VBox card = createAdCard(ad);
            adsContainer.getChildren().add(card);
        }
    }

    private VBox createAdCard(Advertisement ad) {
        VBox card = new VBox(10);
        card.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 12;
            -fx-padding: 15;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 4);
            -fx-border-color: #ecf0f1;
            -fx-border-radius: 12;
        """);
        card.setPrefWidth(600);
        card.setPrefHeight(450);
        card.setMaxWidth(Double.MAX_VALUE);

        Label titleLabel = new Label(ad.getTitle());
        titleLabel.setStyle("-fx-font-size: 1.2em; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(280);

        Label priceLabel = new Label(String.format("💰 %,d تومان", ad.getPrice()));
        priceLabel.setStyle("-fx-font-size: 1.1em; -fx-text-fill: #27ae60; -fx-font-weight: bold;");

        Label cityLabel = new Label("📍 " + ad.getCity());
        cityLabel.setStyle("-fx-font-size: 0.95em; -fx-text-fill: #7f8c8d;");

        Label categoryLabel = new Label("📂 " + ad.getCategory());
        categoryLabel.setStyle("-fx-font-size: 0.95em; -fx-text-fill: #7f8c8d;");

        Label sellerLabel = new Label("👤 " + ad.getSellerUsername());
        sellerLabel.setStyle("-fx-font-size: 0.95em; -fx-text-fill: #7f8c8d;");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button detailBtn = new Button("📋 مشاهده جزئیات");
        detailBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 0.5em 1.5em;");
        detailBtn.setOnAction(e -> {
            openAdDetail(ad);
        });

        card.getChildren().addAll(
                titleLabel, priceLabel, cityLabel, categoryLabel, sellerLabel,
                spacer, detailBtn
        );

        return card;
    }
    private void openAdDetail(Advertisement ad) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ad_detail.fxml"));
            Parent root = loader.load();

            AdDetailController controller = loader.getController();
            controller.setAdvertisement(ad);

            Stage stage = new Stage();
            stage.setTitle("جزئیات آگهی");
            Scene sence=new Scene(root);
            sence.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/main.css")).toExternalForm());
            stage.setScene(sence);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(adsContainer.getScene().getWindow());
            ControllersUtils.setPageSettings(stage,root);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showEmptyMessage(String msg) {
        adsContainer.getChildren().clear();
        emptyLabel.setText(msg);
        emptyLabel.setVisible(true);
        emptyLabel.setManaged(true);
    }
    @FXML
    private void refreshAds() {
        loadApprovedAds();
    }

    private void updateLoginStatus(){
       if(SessionManager.isLoggedIn()){
           loginButton.setVisible(false);
           userPanel.setVisible(true);
           loginButton.setManaged(false);
           if(SessionManager.getCurrentUser().getRole().equals("MANAGER")){
               goToLoginPage();
           }
       }
       else  {
           loginButton.setVisible(true);
           userPanel.setVisible(false);
           userPanel.setManaged(false);
       }
    }
}
