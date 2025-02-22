package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tn.esprit.jdbc.entities.Rating;
import tn.esprit.jdbc.services.RatingService;
import tn.esprit.jdbc.services.vehicleService;
import tn.esprit.jdbc.services.TaxiService;

import java.sql.SQLException;
import java.util.List;

public class RatingController {

    @FXML
    private ComboBox<String> transportTypeComboBox;

    @FXML
    private ComboBox<String> transportComboBox;

    @FXML
    private TextField ratingField;

    @FXML
    private TextArea commentField;

    private int userId; // ID of the user submitting the rating

    private final RatingService ratingService = new RatingService();
    private final vehicleService vehicleService = new vehicleService();
    private final TaxiService taxiService = new TaxiService();

    @FXML
    public void initialize() {
        // Populate transport type ComboBox
        transportTypeComboBox.getItems().addAll("Taxi", "Vehicle");
        transportTypeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                populateTransportComboBox(newValue);
            } catch (SQLException e) {
                showAlert("Error", "Failed to load transport data: " + e.getMessage());
            }
        });
    }

    // Set the user ID
    public void setUserId(int userId) {
        this.userId = userId;
        System.out.println("User ID set in RatingController: " + userId); // Debug log
    }

    // Populate the transport ComboBox based on the selected type
    private void populateTransportComboBox(String transportType) throws SQLException {
        transportComboBox.getItems().clear();
        if ("Taxi".equalsIgnoreCase(transportType)) {
            List<String> taxiImmatriculations = taxiService.getAllTaxiImmatriculations();
            transportComboBox.getItems().addAll(taxiImmatriculations);
        } else if ("Vehicle".equalsIgnoreCase(transportType)) {
            List<String> vehicleLicensePlates = vehicleService.getAllVehicleLicensePlates();
            transportComboBox.getItems().addAll(vehicleLicensePlates);
        }
    }

    @FXML
    public void handleSubmitRating() {
        try {
            // Validate input
            int rating = Integer.parseInt(ratingField.getText());
            if (rating < 1 || rating > 5) {
                showAlert("Invalid Rating", "Rating must be between 1 and 5.");
                return;
            }

            String comment = commentField.getText();
            String selectedTransport = transportComboBox.getSelectionModel().getSelectedItem();
            if (selectedTransport == null) {
                showAlert("Invalid Selection", "Please select a transport.");
                return;
            }

            // Get the transport ID based on the selected immatriculation or license plate
            String transportType = transportTypeComboBox.getSelectionModel().getSelectedItem();
            int vehicleId = 0;
            int taxiId = 0;

            if ("Taxi".equalsIgnoreCase(transportType)) {
                taxiId = taxiService.getTaxiIdByImmatriculation(selectedTransport);
            } else if ("Vehicle".equalsIgnoreCase(transportType)) {
                vehicleId = vehicleService.getVehicleIdByLicensePlate(selectedTransport);
            }

            if (taxiId == 0 && vehicleId == 0) {
                showAlert("Error", "Invalid transport selected.");
                return;
            }

            // Debug logs
            System.out.println("User ID: " + userId);
            System.out.println("Taxi ID: " + taxiId);
            System.out.println("Vehicle ID: " + vehicleId);
            System.out.println("Rating: " + rating);
            System.out.println("Comment: " + comment);

            // Create a new rating
            Rating newRating = new Rating(userId, vehicleId, taxiId, rating, comment);
            ratingService.addRating(newRating);

            showAlert("Success", "Rating submitted successfully!");
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Rating must be a number.");
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while submitting the rating: " + e.getMessage());
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