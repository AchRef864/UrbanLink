package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.jdbc.entities.Reservation;
import tn.esprit.jdbc.entities.Trajet;
import tn.esprit.jdbc.entities.User;
import tn.esprit.jdbc.services.ReservationService;
import tn.esprit.jdbc.services.UserService;
import tn.esprit.jdbc.services.TrajetService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EditReservationController {

    @FXML
    private ComboBox<User> userComboBox;

    @FXML
    private ComboBox<Trajet> trajetComboBox;

    @FXML
    private DatePicker reservationDatePicker;

    @FXML
    private Spinner<Integer> numberOfSeatsSpinner;

    @FXML
    private TextField totalPriceField;

    private Reservation reservation; // To store the reservation being edited
    private boolean isUpdate = false; // This flag indicates whether we are updating or creating a new reservation

    private ReservationService reservationService = new ReservationService();
    private UserService userService = new UserService();
    private TrajetService trajetService = new TrajetService();

    // Initialize the combo boxes with available users and trajets
    public void initialize() {
        try {
            List<User> users = userService.showAll();
            userComboBox.getItems().addAll(users);

            List<Trajet> trajets = trajetService.showAll();
            trajetComboBox.getItems().addAll(trajets);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors du chargement des données: " + e.getMessage());
        }
    }

    // Method to load reservation data into the form fields for editing
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        this.isUpdate = true; // Mark this as an update operation

        // Fill the fields with existing reservation data
        userComboBox.setValue(reservation.getUser());
        trajetComboBox.setValue(reservation.getTrajet());

        // Set the date picker value
        java.sql.Date sqlDate = (java.sql.Date) reservation.getReservationDate();
        if (sqlDate != null) {
            reservationDatePicker.setValue(sqlDate.toLocalDate());
        }

        numberOfSeatsSpinner.getValueFactory().setValue(reservation.getNumberOfSeats());
        totalPriceField.setText(String.valueOf(reservation.getTotalPrice()));
    }

    // Save changes to the reservation or create a new one
    @FXML
    public void handleUpdateReservation() {
        if (this.reservation == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucune réservation sélectionnée pour la mise à jour.");
            return; // Exit if no reservation is selected or passed to the form
        }

        try {
            // Update reservation data
            reservation.setUser(userComboBox.getValue());
            reservation.setTrajet(trajetComboBox.getValue());

            // Convert LocalDate from DatePicker back to java.sql.Date
            LocalDate localDate = reservationDatePicker.getValue();
            if (localDate != null) {
                reservation.setReservationDate(java.sql.Date.valueOf(localDate));
            }

            reservation.setNumberOfSeats(numberOfSeatsSpinner.getValue());
            reservation.setTotalPrice(Double.parseDouble(totalPriceField.getText()));

            // Call the update method in ReservationService
            if (isUpdate) {
                reservationService.update(reservation);
                showAlert(Alert.AlertType.INFORMATION, "Réservation mise à jour", "La réservation a été mise à jour avec succès.");
            } else {
                reservationService.insert(reservation);
                showAlert(Alert.AlertType.INFORMATION, "Réservation ajoutée", "La réservation a été ajoutée avec succès.");
            }

            // Close the window once the changes are saved
            Stage stage = (Stage) userComboBox.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la mise à jour: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez vérifier les valeurs numériques (nombre de sièges, prix).");
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
