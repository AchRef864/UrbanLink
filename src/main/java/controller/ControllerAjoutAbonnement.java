package controller;

import entity.abonnement;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import services.abonnementservices;

import java.sql.Date;

public class ControllerAjoutAbonnement {
    @FXML
    private TextField typeField;
    @FXML
    private TextField prixField;
    @FXML
    private DatePicker dateDebutPicker;
    @FXML
    private DatePicker dateFinPicker;
    @FXML
    private TextField etatField;

    private abonnementservices abonnementService;

    public ControllerAjoutAbonnement() {
        abonnementService = new abonnementservices();
    }

    @FXML
    private void handleAddAbonnement() {
        try {
            // Récupérer les valeurs du formulaire
            String type = typeField.getText();
            double prix = Double.parseDouble(prixField.getText());
            java.sql.Date dateDebut = Date.valueOf(dateDebutPicker.getValue());
            java.sql.Date dateFin = Date.valueOf(dateFinPicker.getValue());
            String etat = etatField.getText();

            // Créer un nouvel abonnement
            abonnement abonnement = new abonnement(
                    0, // ID sera généré automatiquement
                    type,
                    prix,
                    dateDebut,
                    dateFin,
                    etat
            );

            // Ajouter l'abonnement
            int id = abonnementService.insert(abonnement);
            if (id != -1) {
                showAlert("Succès", "Abonnement ajouté avec succès. ID: " + id, Alert.AlertType.INFORMATION);
                clearForm(); // Vider le formulaire après l'ajout
            } else {
                showAlert("Erreur", "Échec de l'ajout de l'abonnement.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Erreur", "Veuillez vérifier les données saisies.", Alert.AlertType.ERROR);
        }
    }

    private void clearForm() {
        typeField.clear();
        prixField.clear();
        dateDebutPicker.setValue(null);
        dateFinPicker.setValue(null);
        etatField.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}