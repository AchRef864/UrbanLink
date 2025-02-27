package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import tn.esprit.jdbc.entities.User;

import java.io.IOException;

public class DashboardController {

    @FXML
    private StackPane contentArea;

    @FXML
    private Button btnHome, btnVehicle, btnMaintenance, btnLogout;

    @FXML
    public void initialize() {
        btnHome.setOnAction(e -> loadPage("/ListerVehicle.fxml"));
        btnVehicle.setOnAction(e -> loadPage("/ListerMaintenance.fxml"));
        btnMaintenance.setOnAction(e -> loadPage("/ListerVehicle.fxml"));
        btnLogout.setOnAction(e -> logout());
    }

    private void loadPage(String fxml) {
        System.out.println(getClass().getResource("/" + fxml));
        System.out.println(fxml);
        try {
            Parent page = FXMLLoader.load(getClass().getResource("" + fxml));
            contentArea.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        System.out.println("User logged out.");
        // Implement logout logic
    }

    public void setUser(User user) {

    }
}
