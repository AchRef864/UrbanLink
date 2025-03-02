package tn.esprit.jdbc.controllers.abonnement;

import tn.esprit.jdbc.entities.abonnement;
import tn.esprit.jdbc.services.abonnementservices;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;


import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class ControllerAjoutAbonnement {

    @FXML
    private ComboBox<String> typeCombo;
    @FXML
    private TextField prixField;
    @FXML
    private DatePicker dateDebutPicker;
    @FXML
    private DatePicker dateFinPicker;
    @FXML
    private ComboBox<String> etatCombo;

    @FXML
    private Pane notificationPane;
    @FXML
    private Text notificationText;

    private abonnementservices abonnementService;

    public ControllerAjoutAbonnement() {
        abonnementService = new abonnementservices();
    }

    @FXML
    private void initialize() {
        // Hide notification panel by default
        notificationPane.setVisible(false);

        // Set default état to "Suspendu" and make it non-editable
        etatCombo.setValue("Suspendu");
        etatCombo.setEditable(false);

        // Listener pour mettre à jour le prix en fonction du type sélectionné
        typeCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (newValue) {
                    case "Annuel":
                        prixField.setText("100 TND");
                        break;
                    case "Mensuel":
                        prixField.setText("20 TND");
                        break;
                    case "Semestriel":
                        prixField.setText("60 TND");
                        break;
                    default:
                        prixField.setText("");
                        break;
                }
            } else {
                prixField.setText("");
            }
        });
    }

    @FXML
    private void handleAddAbonnement() {
        try {
            // Récupérer les valeurs du formulaire
            String type = typeCombo.getValue();
            String prixText = prixField.getText().replace(" EUR", ""); // Retirer " TND" pour parsing
            LocalDate dateDebut = dateDebutPicker.getValue();
            LocalDate dateFin = dateFinPicker.getValue();
            String etat = etatCombo.getValue();

            // Validation des champs
            if (type == null || prixText.isEmpty() || dateDebut == null || dateFin == null || etat == null) {
                showNotification("Erreur", "Veuillez remplir tous les champs.", "#f44336"); // Red for error
                return;
            }

            double prix;
            try {
                prix = Double.parseDouble(prixText);
                if (prix <= 0) {
                    showNotification("Erreur", "Le prix doit être positif.", "#f44336");
                    return;
                }
            } catch (NumberFormatException e) {
                showNotification("Erreur", "Le prix doit être un nombre valide.", "#f44336");
                return;
            }

            if (dateFin.isBefore(dateDebut)) {
                showNotification("Erreur", "Date de fin doit être après date de début.", "#f44336");
                return;
            }

            // Convertir LocalDate en java.sql.Date
            Date sqlDateDebut = Date.valueOf(dateDebut);
            Date sqlDateFin = Date.valueOf(dateFin);

            // Vérifier l'expiration par rapport à la date actuelle
            LocalDate currentDate = LocalDate.now();
            long daysUntilExpiration = dateFin.toEpochDay() - currentDate.toEpochDay();

            if (daysUntilExpiration < 0) {
                showNotification("Avertissement", "Abonnement expiré (Date Fin: " + dateFin + ")", "#ff9800"); // Orange for warning
            } else if (daysUntilExpiration <= 1) {
                showNotification("Avertissement", "Expire dans < 1 jour (Date Fin: " + dateFin + ")", "#ff9800");
            }

            // Créer un nouvel abonnement
            abonnement abonnement = new abonnement(
                    0, // ID sera généré automatiquement
                    type,
                    prix,
                    sqlDateDebut,
                    sqlDateFin,
                    etat
            );

            // Ajouter l'abonnement
            int id = abonnementService.insert(abonnement);
            if (id != -1) {
                showNotification("Succès", "Abonnement ajouté avec succès. ID: " + id, "#4CAF50"); // Green for success
                clearForm(); // Vider le formulaire après l'ajout
            } else {
                showNotification("Erreur", "Échec de l'ajout de l'abonnement.", "#f44336");
            }
        } catch (SQLException e) {
            showNotification("Erreur", "Erreur SQL : " + e.getMessage(), "#f44336");
            e.printStackTrace();
        }
    }

    private void clearForm() {
        typeCombo.getSelectionModel().clearSelection();
        prixField.clear();
        dateDebutPicker.setValue(null);
        dateFinPicker.setValue(null);
        etatCombo.setValue("Suspendu"); // Reset to default
    }

    private void showNotification(String title, String message, String color) {
        notificationPane.setStyle("-fx-background-color: " + color + "; -fx-border-radius: 5; -fx-background-radius: 5;");
        notificationText.setText(title + "\n" + message);
        notificationPane.setVisible(true);

        // Hide the notification after 5 seconds
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        javafx.application.Platform.runLater(() -> notificationPane.setVisible(false));
                    }
                },
                5000
        );
    }
}