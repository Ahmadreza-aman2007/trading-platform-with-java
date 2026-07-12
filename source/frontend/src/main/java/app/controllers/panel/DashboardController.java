package app.controllers.panel;

import app.models.entities.User;
import app.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class DashboardController {
    @FXML private Label username;
    @FXML private Label phoneNumber;
    @FXML private Label fullname;
    @FXML
    public void initialize() {
        User user= SessionManager.getCurrentUser();
        if(user!=null){
            username.setText(user.getUsername());
            phoneNumber.setText(user.getPhoneNumber());
            fullname.setText(user.getFullname());
        }
    }
}
