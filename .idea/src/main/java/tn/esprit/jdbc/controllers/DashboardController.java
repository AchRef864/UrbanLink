package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class DashboardController {

    @FXML
    private StackPane contentArea;

    @FXML
    private Button btnHome, btnVehicle, btnMaintenance, btnReclamation, btnInsertUser, btnEditUser, btnLogout;

    @FXML
    public void initialize() {
        // Set actions for buttons
        btnHome.setOnAction(e -> loadPage("/Home.fxml")); // Load Home page
        btnVehicle.setOnAction(e -> loadPage("/ListerVehicle.fxml"));
        btnMaintenance.setOnAction(e -> loadPage("/ListerMaintenance.fxml"));
        btnReclamation.setOnAction(e -> loadPage("/AdminReclamationResponse.fxml"));
        btnInsertUser.setOnAction(e -> loadPage("/AjouterUser.fxml"));
        btnEditUser.setOnAction(e -> loadPage("/EditUser.fxml"));
        btnLogout.setOnAction(e -> logout());
    }

    private void loadPage(String fxml) {
        try {
            System.out.println("Loading page: " + fxml);
            Parent page = FXMLLoader.load(getClass().getResource(fxml));
            contentArea.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading page: " + fxml);
        }
    }

    private void logout() {
        System.out.println("User logged out.");
        // Implement logout logic (e.g., redirect to login page)
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setScene(new Scene(loginPage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}