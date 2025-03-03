package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
    private HBox starContainer;

    @FXML

    private int userId; // ID of the user submitting the rating
    private int selectedRating = 0; // Stores the selected rating (1-5)

    private final RatingService ratingService = new RatingService();
    private final vehicleService vehicleService = new vehicleService();
    private final TaxiService taxiService = new TaxiService();

    @FXML
    public void initialize() {
        try {
            // Load star images
            Image goldStar = new Image(getClass().getResourceAsStream("/images/gold_star.png"));

            // Set star sizes
            star1.setImage(goldStar);
            star1.setFitWidth(30);
            star1.setFitHeight(30);

            star2.setImage(goldStar);
            star2.setFitWidth(30);
            star2.setFitHeight(30);

            star3.setImage(goldStar);
            star3.setFitWidth(30);
            star3.setFitHeight(30);

            star4.setImage(goldStar);
            star4.setFitWidth(30);
            star4.setFitHeight(30);

            star5.setImage(goldStar);
            star5.setFitWidth(30);
            star5.setFitHeight(30);
        } catch (Exception e) {
            System.err.println("Error loading star image: " + e.getMessage());
            e.printStackTrace();
        }

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

    // Handle star clicks
    @FXML
    public void handleStarClick(javafx.scene.input.MouseEvent event) {
        ImageView clickedStar = (ImageView) event.getSource();
        int rating = Integer.parseInt(clickedStar.getUserData().toString());

        // Update star opacity based on the selected rating
        for (int i = 1; i <= 5; i++) {
            ImageView star = (ImageView) starContainer.lookup("#star" + i);
            if (i <= rating) {
                star.setOpacity(1.0); // Fully visible
            } else {
                star.setOpacity(0.5); // Semi-transparent
            }
        }

        selectedRating = rating; // Store the selected rating
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
            if (selectedRating < 1 || selectedRating > 5) {
                showAlert("Invalid Rating", "Please select a rating between 1 and 5.");
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
            System.out.println("Rating: " + selectedRating);
            System.out.println("Comment: " + comment);

            // Create a new rating
            Rating newRating = new Rating(userId, vehicleId, taxiId, selectedRating, comment);
            ratingService.addRating(newRating);

            showAlert("Success", "Rating submitted successfully!");
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