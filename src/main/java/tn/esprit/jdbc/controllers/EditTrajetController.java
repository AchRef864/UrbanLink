package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.jdbc.entities.Trajet;
import tn.esprit.jdbc.services.TrajetService;

import java.sql.SQLException;

public class EditTrajetController {

    @FXML
    private TextField departureField;

    @FXML
    private TextField destinationField;

    @FXML
    private TextField distanceField;

    @FXML
    private TextField durationHourField;

    @FXML
    private TextField durationMinuteField;

    @FXML
    private TextField departureHourField;

    @FXML
    private TextField departureMinuteField;

    @FXML
    private TextField arrivalHourField;

    @FXML
    private TextField arrivalMinuteField;

    @FXML
    private TextField priceField;

    private Trajet trajet;
    private boolean isUpdate = false; // Flag to indicate update or insert operation
    private TrajetService trajetService = new TrajetService(); // TrajetService instance
    private int trajetId;  // Store the retrieved trajetId

    // Method to initialize fields when editing an existing trajet
    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
        this.isUpdate = true;

        // Fill the fields with the current values from the trajet
        departureField.setText(trajet.getDeparture());
        destinationField.setText(trajet.getDestination());
        distanceField.setText(String.valueOf(trajet.getDistance()));

        // Split duration into hours and minutes
        String[] durationParts = trajet.getDuration().split(":");
        durationHourField.setText(durationParts[0]);
        durationMinuteField.setText(durationParts[1]);

        // Split departure time into hours and minutes
        String[] departureTimeParts = trajet.getDepartureTime().split(":");
        departureHourField.setText(departureTimeParts[0]);
        departureMinuteField.setText(departureTimeParts[1]);

        // Split arrival time into hours and minutes
        String[] arrivalTimeParts = trajet.getArrivalTime().split(":");
        arrivalHourField.setText(arrivalTimeParts[0]);
        arrivalMinuteField.setText(arrivalTimeParts[1]);

        priceField.setText(String.valueOf(trajet.getPrice()));

        // Retrieve trajetId as soon as the page is opened
        retrieveTrajetId();
    }

    // This method is called when the page is initialized (before saving changes)
    private void retrieveTrajetId() {
        try {
            // Retrieve the trajetId for the current trajet
            trajetId = trajetService.getTrajetId(trajet.getDeparture(), trajet.getDestination(), trajet.getDistance(),
                    trajet.getDuration(), trajet.getDepartureTime(), trajet.getArrivalTime(), trajet.getPrice(),
                    trajet.getTrajetType());

            System.out.println("Trajet ID retrieved: " + trajetId);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Une erreur est survenue lors de la récupération de l'ID du trajet: " + e.getMessage());
        }
    }

    // Function called when the user clicks "Save"
    @FXML
    public void saveChanges() {
        try {
            // Validate inputs
            if (!validateInputs()) {
                return; // Stop saving if validation fails
            }

            if (trajet == null) {
                trajet = new Trajet(); // Only create a new Trajet for new inserts
            }

            // Set updated values to trajet object
            trajet.setDeparture(departureField.getText());
            trajet.setDestination(destinationField.getText());
            trajet.setDistance(Double.parseDouble(distanceField.getText()));

            // Combine hours and minutes for duration
            String totalDuration = durationHourField.getText() + ":" + durationMinuteField.getText();
            trajet.setDuration(totalDuration);

            String departureTime = departureHourField.getText() + ":" + departureMinuteField.getText();
            trajet.setDepartureTime(departureTime);

            String arrivalTime = arrivalHourField.getText() + ":" + arrivalMinuteField.getText();
            trajet.setArrivalTime(arrivalTime);

            trajet.setPrice(Double.parseDouble(priceField.getText()));

            System.out.println("hillou " + trajet.toString());

            if (isUpdate) {
                if (trajetId != 0) { // Ensure we are updating an existing Trajet with a valid ID
                    // Create a Trajet with the ID and update it
                    Trajet updatedTrajet = new Trajet(trajetId, trajet.getDeparture(), trajet.getDestination(),
                            trajet.getDistance(), trajet.getDuration(), trajet.getDepartureTime(),
                            trajet.getArrivalTime(), trajet.getPrice(), trajet.getTrajetType());

                    trajetService.update(updatedTrajet);  // Update the existing trajet in the database
                    showAlert(Alert.AlertType.INFORMATION, "Trajet mis à jour", "Les informations du trajet ont été mises à jour avec succès.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de mettre à jour un trajet avec un ID de 0.");
                }
            } else {
                if (trajetId == 0) {  // Only insert if the ID is 0 (new trajet)
                    trajetService.insert(trajet);
                    showAlert(Alert.AlertType.INFORMATION, "Nouveau Trajet ajouté", "Le trajet a été ajouté avec succès.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ajouter un trajet avec un ID déjà défini.");
                }
            }

            // Close the window once changes are saved
            Stage stage = (Stage) departureField.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Une erreur est survenue lors de l'enregistrement: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Vérifiez les valeurs numériques (distance, prix, etc.).");
        }
    }

    // Validate inputs before saving
    private boolean validateInputs() {
        if (departureField.getText().isEmpty() || destinationField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de validation", "Les champs de départ et de destination ne doivent pas être vides.");
            return false;
        }
        try {
            Double.parseDouble(distanceField.getText());
            Double.parseDouble(priceField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de validation", "Distance et prix doivent être des nombres valides.");
            return false;
        }

        return true;
    }

    // Utility method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
