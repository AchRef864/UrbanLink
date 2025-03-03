package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import tn.esprit.jdbc.entities.User;
import java.io.IOException;

public class DashboardClientController {

    @FXML
    private StackPane contentArea;

    @FXML
    private Button btnHome, btnVehicle, btnMaintenance, btnRateRide, btnLogout, btnListeTaxisEtCourses;

    @FXML
    private Button btnReviews, btnComplainings; // New buttons for Reviews & Complaints

    private User currentUser; // Store logged-in user information

    @FXML
    public void initialize() {
        btnHome.setOnAction(e -> loadPage("/Home.fxml"));
        btnVehicle.setOnAction(e -> loadPage("/ListerVehicle.fxml"));
        btnMaintenance.setOnAction(e -> loadPage("/ListerMaintenance.fxml"));
        btnRateRide.setOnAction(e -> loadPage("/RatingForm.fxml"));
        btnListeTaxisEtCourses.setOnAction(this::handleListeTaxisEtCourses);
        btnLogout.setOnAction(e -> logout());

        // New button actions
        btnReviews.setOnAction(e -> loadPage("/UserAvisTable.fxml"));

        // Modified action for the Complainings button
        btnComplainings.setOnAction(this::btnComplainingsAction); // Handles button click
    }

    /**
     * Sets the logged-in user and updates the UI accordingly.
     */
    public void setUser(User user) {
        this.currentUser = user;
    }

    /**
     * Loads the specified FXML page into the content area.
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
            showAlert("Error", "Failed to load page: " + fxml);
        }
    }

    /**
     * Handles the "Liste Taxis et Courses" button click.
     */
    @FXML
    private void handleListeTaxisEtCourses(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeTaxisEtCourses.fxml"));
            Parent page = loader.load();
            contentArea.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load Liste Taxis et Courses page.");
        }
    }

    /**
     * Handles the "Complainings" button click and passes the user ID to the ReclamationController.
     */
    @FXML
    private void btnComplainingsAction(ActionEvent event) {
        try {
            // Load the Reclamation FXML page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation.fxml"));
            Parent page = loader.load();

            // Get the ReclamationController and set the user ID
            ReclamationController reclamationController = loader.getController();
            if (currentUser != null) {
                reclamationController.setUserId(currentUser.getUserId()); // Pass userId from currentUser
            } else {
                System.out.println("No user logged in.");
            }

            // Add the page to the content area
            contentArea.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Reclamation page.");
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

    /**
     * Displays an alert with the specified title and message.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
