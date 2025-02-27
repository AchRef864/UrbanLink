package tn.esprit.jdbc.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import tn.esprit.jdbc.entities.Reservation;
import tn.esprit.jdbc.entities.Trajet;
import tn.esprit.jdbc.entities.User;
import tn.esprit.jdbc.services.ReservationService;
import tn.esprit.jdbc.services.TrajetService;
import tn.esprit.jdbc.services.UserService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ReservationController {

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

    private ReservationService reservationService = new ReservationService();
    private UserService userService = new UserService();
    private TrajetService trajetService = new TrajetService();

    @FXML
    public void initialize() {
        // Charger la liste des utilisateurs et trajets dans les ComboBoxes
        loadUsers();
        loadTrajets();
    }

    private void loadUsers() {
        try {
            List<User> users = userService.showAll();

            // Add users to the ComboBox
            userComboBox.getItems().addAll(users);

            // Set a StringConverter to display only the user's name
            userComboBox.setConverter(new StringConverter<>() {
                @Override
                public String toString(User user) {
                    return user.getName() + "-" +user.getEmail();
                }

                @Override
                public User fromString(String string) {
                    return null; // Not necessary for this case
                }
            });

        } catch (SQLException e) {
            showErrorAlert("Error", "Failed to load users: " + e.getMessage());
        }
    }


    private void loadTrajets() {
        try {
            List<Trajet> trajets = trajetService.showAll();
            trajetComboBox.getItems().addAll(trajets);

        } catch (SQLException e) {
            showErrorAlert("Error", "Failed to load trajets: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddReservation(ActionEvent event) {
        try {
            // Récupérer les valeurs des champs
            User selectedUser = userComboBox.getValue();
            Trajet selectedTrajet = trajetComboBox.getValue();
            LocalDate reservationDate = reservationDatePicker.getValue();
            int numberOfSeats = numberOfSeatsSpinner.getValue();
            double totalPrice = Double.parseDouble(totalPriceField.getText());

            // Validation simple
            if (selectedUser == null || selectedTrajet == null || reservationDate == null) {
                showErrorAlert("Validation Error", "Please fill all required fields.");
                return;
            }

            // Créer une nouvelle réservation
            Reservation reservation = new Reservation();
            reservation.setUser(selectedUser);
            reservation.setTrajet(selectedTrajet);
            reservation.setReservationDate(java.sql.Date.valueOf(reservationDate));
            reservation.setNumberOfSeats(numberOfSeats);
            reservation.setTotalPrice(totalPrice);

            // Appeler le service pour insérer la réservation
            reservationService.insert(reservation);
            showInfoAlert("Success", "Reservation added successfully!");

            // Réinitialiser le formulaire après l'ajout
            resetForm();
        } catch (SQLException e) {
            showErrorAlert("Error", "Failed to add reservation: " + e.getMessage());
        } catch (NumberFormatException e) {
            showErrorAlert("Validation Error", "Please enter a valid number for total price.");
        }
    }

    private void resetForm() {
        userComboBox.setValue(null);
        trajetComboBox.setValue(null);
        reservationDatePicker.setValue(null);
        numberOfSeatsSpinner.getValueFactory().setValue(1);
        totalPriceField.clear();
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
