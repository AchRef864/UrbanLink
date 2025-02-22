package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.jdbc.entities.User;

public class DashboardClientController {

    @FXML
    private StackPane contentArea;

    @FXML
    private Button btnHome, btnVehicle, btnMaintenance, btnInsertUser, btnEditUser, btnRateRide, btnLogout;

    private User currentUser; // Store the logged-in user

    @FXML
    public void initialize() {
        // Set actions for buttons
        btnHome.setOnAction(e -> loadPage("/Home.fxml"));
        btnVehicle.setOnAction(e -> loadPage("/ListerVehicle.fxml"));
        btnMaintenance.setOnAction(e -> loadPage("/ListerMaintenance.fxml"));
        btnInsertUser.setOnAction(e -> loadPage("/AjouterUser.fxml"));
        btnEditUser.setOnAction(e -> loadPage("/EditUser.fxml"));
        btnRateRide.setOnAction(e -> loadPage("/RatingForm.fxml")); // Load the rating form
        btnLogout.setOnAction(e -> logout());
    }

    /**
     * Sets the logged-in user and updates the UI based on their role.
     *
     * @param user The logged-in user.
     */
    public void setUser(User user) {
        this.currentUser = user;
        updateUIForRole(); // Update UI based on the user's role
    }

    /**
     * Updates the UI based on the user's role.
     * For example, hide/show buttons for admins and clients.
     */
    private void updateUIForRole() {
        if (currentUser != null) {
            if ("admin".equals(currentUser.getRole())) {
                // Admin-specific UI updates
                btnInsertUser.setVisible(true); // Show "Add User" button for admins
                btnEditUser.setVisible(true);   // Show "Edit User" button for admins
            } else if ("client".equals(currentUser.getRole())) {
                // Client-specific UI updates
                btnInsertUser.setVisible(false); // Hide "Add User" button for clients
                btnEditUser.setVisible(false);   // Hide "Edit User" button for clients
            }
        }
    }

    /**
     * Loads the specified FXML page into the content area.
     *
     * @param fxml The path to the FXML file.
     */
    private void loadPage(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent page = loader.load();

            // Pass the user ID to the RatingController if the loaded page is the rating form
            if (fxml.equals("/RatingForm.fxml")) {
                RatingController ratingController = loader.getController();
                ratingController.setUserId(currentUser.getUserId()); // Pass the user ID
            }

            contentArea.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading page: " + fxml);
        }
    }

    /**
     * Logs out the user and redirects to the login page.
     */
    private void logout() {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setScene(new Scene(loginPage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}