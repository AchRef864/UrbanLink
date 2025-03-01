package tn.esprit.jdbc.controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
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

    // Twilio Credentials (Replace with your own credentials)
    private static final String ACCOUNT_SID = "xxxxxxxxx";
    private static final String AUTH_TOKEN = "xxxxxxxxx";
    private static final String TWILIO_NUMBER = "+xxxxxxx"; // Your Twilio phone number

    @FXML
    public void initialize() {
        // Set up the columns in the TableView
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        sujetColumn.setCellValueFactory(new PropertyValueFactory<>("sujet"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        responseColumn.setCellValueFactory(new PropertyValueFactory<>("reponseReclamation"));

        // Load data into the TableView
        loadReclamationData();

        // Initialize Twilio
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
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

                // Send SMS notification to the user
                sendSMSNotification(selectedReclamation);

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

    private void sendSMSNotification(Reclamation reclamation) {
        // Fetch user's phone number from the database
        String userPhoneNumber = reclamationService.getUserPhoneNumber(reclamation.getUserId());

        if (userPhoneNumber == null || userPhoneNumber.isEmpty()) {
            showAlert("Error", "User phone number not found.");
            return;
        }

        // Construct the SMS message
        String messageText = String.format(
                "Hello, your complaint regarding '%s' has been reviewed. Our response: %s",
                reclamation.getSujet(),
                reclamation.getReponseReclamation()
        );

        // Send SMS via Twilio
        try {
            Message.creator(
                    new PhoneNumber(userPhoneNumber),   // Recipient
                    new PhoneNumber(TWILIO_NUMBER),    // Twilio Sender Number (FIXED)
                    messageText
            ).create();

            showAlert("Success", "SMS notification sent to the user!");
        } catch (Exception e) {
            showAlert("Error", "Failed to send SMS: " + e.getMessage());
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
