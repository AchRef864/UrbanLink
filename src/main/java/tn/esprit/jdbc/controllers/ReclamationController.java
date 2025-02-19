package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.jdbc.entities.Reclamation;
import tn.esprit.jdbc.services.ReclamationService;

import java.io.IOException;
import java.sql.SQLException;

public class ReclamationController {

    @FXML
    private TextField sujetTextField;

    @FXML
    private TextArea descriptionTextArea;

    private int userId; // Store the logged-in user's ID

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @FXML
    public void handleSubmitButton() {
        String sujet = sujetTextField.getText();
        String description = descriptionTextArea.getText();

        // Input validation
        if (sujet.isEmpty() || description.isEmpty()) {
            showAlert("Error", "Sujet and Description cannot be empty.");
            return;
        }

        // Create a new reclamation
        Reclamation reclamation = new Reclamation(userId, sujet, description);

        ReclamationService reclamationService = new ReclamationService();
        try {
            reclamationService.insert(reclamation);
            showAlert("Success", "Reclamation submitted successfully!");

            // Clear the fields
            sujetTextField.clear();
            descriptionTextArea.clear();
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while submitting the reclamation: " + e.getMessage());
        }
    }

    @FXML
    public void handleBackButton() {
        try {
            // Navigate back to the client page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientPage.fxml"));
            Parent root = loader.load();

            // Pass the user ID to the client page controller
            ClientPageController clientPageController = loader.getController();
            clientPageController.setUserId(userId);

            Stage stage = (Stage) sujetTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Error", "An error occurred while navigating back: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}