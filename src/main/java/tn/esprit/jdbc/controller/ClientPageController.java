package tn.esprit.jdbc.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.sql.SQLException;

public class ClientPageController {

    private int userId; // Store the logged-in user's ID

    @FXML
    private VBox userAvisContainer;
    @FXML
    private void handleReclamationButton(ActionEvent event) {
        try {
            // Load the Reclamation page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation.fxml"));
            Parent root = loader.load();

            // Pass the user ID to the reclamation controller
            ReclamationController reclamationController = loader.getController();
            reclamationController.setUserId(userId);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while loading the reclamation page: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleLogoutButton(ActionEvent event) {
        // Logic for logging out and returning to login page
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("You have logged out successfully.");
        alert.showAndWait();

        // Code to return to the login screen or close the application
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCheckReclamationResponseButton(ActionEvent event) {
        try {
            // Load the reclamation response page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientReclamationResponse.fxml"));
            Parent root = loader.load();

            // Pass the user ID to the reclamation response controller
            ClientReclamationResponseController reclamationResponseController = loader.getController();
            reclamationResponseController.setUserId(userId);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Error", "An error occurred while loading the reclamation response page: " + e.getMessage());
        }
    }

    @FXML
    private void handleViewReviewsButton(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/UserAvisTable.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Container for the UserAvis view

    private UserAvisController userAvisController; // Reference to the UserAvisController


    // Setter for userId
    public void setUserId(int userId) {
        this.userId = userId;
        if (userAvisController != null) {
            userAvisController.setLoggedInUserId(userId); // Pass the user ID to UserAvisController
        }
    }

    // Method to set the UserAvisController
    public void setUserAvisController(UserAvisController userAvisController) {
        this.userAvisController = userAvisController;
        if (userId != 0) {
            userAvisController.setLoggedInUserId(userId); // Pass the user ID if it's already set
        }
    }

    //add getter to the container for Cannot resolve method 'getUserAvisContainer' in 'ClientPageController'

    public VBox getUserAvisContainer() {
        return userAvisContainer;
    }


}