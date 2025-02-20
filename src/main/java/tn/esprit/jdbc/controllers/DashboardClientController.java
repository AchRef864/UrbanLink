package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;



import java.io.IOException;

public class DashboardClientController {
    @FXML
    private Label welcomeLabel; // Ensure this matches the fx:id in your FXML file

    private int userId;

    // Method to set the userId
    public void setUserId(int userId) {
        this.userId = userId;
        // Update the welcome label with the user ID
        welcomeLabel.setText("Welcome, User #" + userId);
    }

    @FXML
    private StackPane contentArea; // Ensure this matches the fx:id in the FXML file

    @FXML
    private Button btnHome, btnCreateReclamation, btnCheckResponse, btnLogout;

    @FXML
    public void initialize() {
        // Set actions for buttons
        btnHome.setOnAction(e -> loadPage("/HomeClient.fxml")); // Load Home page for client
        btnCreateReclamation.setOnAction(e -> loadPage("/Reclamation.fxml")); // Load Reclamation page
        btnCheckResponse.setOnAction(e -> loadPage("/ClientReclamationResponse.fxml")); // Load Response page
        btnLogout.setOnAction(e -> logout());
    }

    private void loadPage(String fxml) {
        try {
            // Load the FXML file for the selected page
            Parent page = FXMLLoader.load(getClass().getResource(fxml));
            // Clear the content area and set the new page
            contentArea.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading page: " + fxml);
        }
    }

    private void logout() {
        System.out.println("Client logged out.");
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