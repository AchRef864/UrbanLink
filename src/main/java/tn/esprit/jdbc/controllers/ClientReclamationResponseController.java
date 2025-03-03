package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.jdbc.entities.Reclamation;
import tn.esprit.jdbc.services.ReclamationService;
import javafx.scene.control.Alert;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class ClientReclamationResponseController {

    @FXML
    private TextField sujetTextField;

    @FXML
    private TextField dateTextField;

    @FXML
    private TextArea responseTextArea;

    private int userId; // Store the logged-in user's ID

    public void setUserId(int userId) {
        this.userId = userId;
        loadLastReclamation();
    }

    private void loadLastReclamation() {
        ReclamationService reclamationService = new ReclamationService();
        try {
            // Fetch the last reclamation for the user
            Reclamation lastReclamation = reclamationService.getLastReclamationByUserId(userId);

            if (lastReclamation != null) {
                // Populate the fields with the reclamation data
                sujetTextField.setText(lastReclamation.getSujet());
                dateTextField.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastReclamation.getDateReclamation()));
                responseTextArea.setText(lastReclamation.getReponseReclamation());
            } else {
                sujetTextField.setText("No reclamation found.");
                dateTextField.setText("");
                responseTextArea.setText("");
            }
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while fetching the reclamation: " + e.getMessage());
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
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}