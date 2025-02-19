package tn.esprit.jdbc.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.esprit.jdbc.entities.Reclamation;
import tn.esprit.jdbc.services.ReclamationService;

import java.io.IOException;
import java.sql.SQLException;

public class AdminReclamationResponseController {

    @FXML
    private TableView<Reclamation> reclamationTable;

    @FXML
    private TableColumn<Reclamation, Integer> userIdColumn;

    @FXML
    private TableColumn<Reclamation, String> sujetColumn;

    @FXML
    private TableColumn<Reclamation, String> descriptionColumn;

    @FXML
    private TableColumn<Reclamation, String> responseColumn;

    @FXML
    private TextArea responseTextArea;

    private ReclamationService reclamationService = new ReclamationService();

    @FXML
    public void initialize() {
        // Set up the columns in the TableView
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        sujetColumn.setCellValueFactory(new PropertyValueFactory<>("sujet"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        responseColumn.setCellValueFactory(new PropertyValueFactory<>("reponseReclamation"));

        // Load data into the TableView
        loadReclamationData();
    }

    private void loadReclamationData() {
        try {
            // Fetch all reclamations from the database
            reclamationTable.setItems(FXCollections.observableArrayList(reclamationService.showAll()));
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while loading reclamations: " + e.getMessage());
        }
    }

    @FXML
    public void handleSubmitResponseButton() {
        Reclamation selectedReclamation = reclamationTable.getSelectionModel().getSelectedItem();

        if (selectedReclamation != null) {
            String response = responseTextArea.getText();

            if (response.isEmpty()) {
                showAlert("Error", "Response cannot be empty.");
                return;
            }

            // Update the reclamation with the response
            selectedReclamation.setReponseReclamation(response);

            try {
                reclamationService.update(selectedReclamation);
                showAlert("Success", "Response submitted successfully!");

                // Reload the data in the TableView
                loadReclamationData();

                // Clear the response field
                responseTextArea.clear();
            } catch (SQLException e) {
                showAlert("Error", "An error occurred while submitting the response: " + e.getMessage());
            }
        } else {
            showAlert("Error", "No reclamation selected.");
        }
    }

    @FXML
    public void handleBackButton() {
        try {
            // Navigate back to the admin page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HelloAdmin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) reclamationTable.getScene().getWindow();
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