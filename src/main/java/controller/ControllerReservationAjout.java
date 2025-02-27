package controller;

import entity.reservation;
import entity.abonnement;
import services.reservationservice;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.Date;

public class ControllerReservationAjout {
    @FXML
    private TextField userIdField;
    @FXML
    private DatePicker dateDebutPicker;
    @FXML
    private DatePicker dateFinPicker;
    @FXML
    private TextField statutField;
    @FXML
    private TextField abonnementIdField;

    private reservationservice reservationService;

    public ControllerReservationAjout() {
        reservationService = new reservationservice();
    }

    @FXML
    private void handleAddReservation() {
        try {
            // Récupérer les valeurs du formulaire
            int userId = Integer.parseInt(userIdField.getText());
            java.sql.Date dateDebut = Date.valueOf(dateDebutPicker.getValue());
            java.sql.Date dateFin = Date.valueOf(dateFinPicker.getValue());
            String statut = statutField.getText();
            int abonnementId = Integer.parseInt(abonnementIdField.getText());

            // Créer un objet abonnement
            abonnement abonnement = new abonnement();
            abonnement.setid(abonnementId);

            // Créer une nouvelle réservation
            reservation reservation = new reservation(
                    0, // ID sera généré automatiquement
                    userId,
                    dateDebut,
                    dateFin,
                    statut,
                    abonnement
            );

            // Ajouter la réservation
            int id = reservationService.insert(reservation);
            if (id != -1) {
                showAlert("Succès", "Réservation ajoutée avec succès. ID: " + id, Alert.AlertType.INFORMATION);
                clearForm(); // Vider le formulaire après l'ajout
            } else {
                showAlert("Erreur", "Échec de l'ajout de la réservation.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Erreur", "Veuillez vérifier les données saisies.", Alert.AlertType.ERROR);
        }
    }

    private void clearForm() {
        userIdField.clear();
        dateDebutPicker.setValue(null);
        dateFinPicker.setValue(null);
        statutField.clear();
        abonnementIdField.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}