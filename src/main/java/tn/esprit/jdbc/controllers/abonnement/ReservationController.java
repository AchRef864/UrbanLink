package tn.esprit.jdbc.controllers.abonnement;

import javafx.fxml.FXML;
import javafx.scene.control.*;


import tn.esprit.jdbc.entities.reservation;
import tn.esprit.jdbc.services.reservationservice;

import javafx.fxml.FXML;


import java.sql.SQLException;
import java.util.Date;

public class ReservationController {

    @FXML
    private TextField userIdField;
    @FXML
    private DatePicker dateDebutPicker;
    @FXML
    private DatePicker dateFinPicker;
    @FXML
    private ComboBox<String> statutComboBox;
    @FXML
    private TextField abonnementIdField;
    @FXML
    private Button modifierButton;

    private reservation currentReservation;
    private final reservationservice reservationService;

    public ReservationController() {
        this.reservationService = new reservationservice();
    }

    public ReservationController(reservationservice reservationService) {
        this.reservationService = reservationService;
    }

    // Méthode pour initialiser les champs avec les données de la réservation sélectionnée
    public void setReservation(reservation reservation) {
        this.currentReservation = reservation;

        // Remplir les champs du formulaire avec les données de la réservation
        userIdField.setText(String.valueOf(reservation.getuserId()));
        dateDebutPicker.setValue(reservation.getdateDebut().toLocalDate());
        dateFinPicker.setValue(reservation.getdateFin().toLocalDate());
        statutComboBox.setValue(reservation.getstatut());

        abonnementIdField.setText(String.valueOf(reservation.getabonnement().getid()));
    }

    @FXML
    public void initialize() {
        // Initialisation de la liste des états possibles
        statutComboBox.getItems().addAll("en attente ", "Annulée", "Confirmer");
    }

    // Méthode pour enregistrer les modifications
    @FXML
    private void modifierReservation() {
        try {
            // Récupérer les nouvelles valeurs du formulaire
            int userId = Integer.parseInt(userIdField.getText());
            Date dateDebut = java.sql.Date.valueOf(dateDebutPicker.getValue());
            Date dateFin = java.sql.Date.valueOf(dateFinPicker.getValue());
            String statut = statutComboBox.getValue();
            int abonnementId = Integer.parseInt(abonnementIdField.getText());

            // Créer un nouvel objet réservation avec les données modifiées
            reservation updatedReservation = new reservation(
                    currentReservation.getId(),
                    userId,
                    dateDebut,
                    dateFin,
                    statut,
                    currentReservation.getabonnement()  // Garder le même abonnement
            );

            // Appeler le service pour mettre à jour la réservation dans la base de données
            int result = reservationService.update(updatedReservation);
            if (result > 0) {
                showAlert("Succès", "Réservation mise à jour avec succès !");
            } else {
                showAlert("Erreur", "Erreur lors de la mise à jour de la réservation.");
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur de base de données : " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs valides.");
        }
    }

    // Méthode pour afficher des alertes
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
