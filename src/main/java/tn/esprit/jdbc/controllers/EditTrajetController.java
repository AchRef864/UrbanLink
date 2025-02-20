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
    private boolean isUpdate = false; // This flag indicates whether we are updating or creating a new trajet
    private TrajetService trajetService = new TrajetService(); // Instantiate the TrajetService to perform CRUD operations

    // Function to receive and initialize the trajet to edit
    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
        this.isUpdate = true; // When setting a trajet, it's for updating

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
    }

    // Function called when the user clicks "Save"
    @FXML
    public void saveChanges() {
        try {
            // Update the values of the trajet with the modified data from the user
            if (trajet == null) {
                trajet = new Trajet();
            }

            trajet.setDeparture(departureField.getText());
            trajet.setDestination(destinationField.getText());
            trajet.setDistance(Double.parseDouble(distanceField.getText()));

            // Combine hours and minutes for duration, departure time, and arrival time
            String duration = durationHourField.getText() + ":" + durationMinuteField.getText();
            trajet.setDuration(duration);

            String departureTime = departureHourField.getText() + ":" + departureMinuteField.getText();
            trajet.setDepartureTime(departureTime);

            String arrivalTime = arrivalHourField.getText() + ":" + arrivalMinuteField.getText();
            trajet.setArrivalTime(arrivalTime);

            trajet.setPrice(Double.parseDouble(priceField.getText()));

            // Save or update the trajet
            if (isUpdate) {
                trajetService.update(trajet);
                showAlert(Alert.AlertType.INFORMATION, "Trajet mis à jour", "Les informations du trajet ont été mises à jour avec succès.");
            } else {
                trajetService.insert(trajet);
                showAlert(Alert.AlertType.INFORMATION, "Nouveau Trajet ajouté", "Le trajet a été ajouté avec succès.");
            }

            // Close the window once the changes are saved
            Stage stage = (Stage) departureField.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'enregistrement: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez vérifier les valeurs numériques (distance, prix, etc.).");
        }
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
