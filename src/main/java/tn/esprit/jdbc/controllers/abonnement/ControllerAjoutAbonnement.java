package tn.esprit.jdbc.controllers.abonnement;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import tn.esprit.jdbc.entities.User;
import tn.esprit.jdbc.entities.abonnement;
import tn.esprit.jdbc.services.abonnementservices;

import java.sql.Date;
import java.sql.SQLException;

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

    private final abonnementservices abonnementService = new abonnementservices();

    @FXML
    public void initialize() {
        // Set default visibility for notification pane
        notificationPane.setVisible(false);

        // Update price based on selected type
        typeCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                updatePrice(newValue);
            }
        });
    }

    private void updatePrice(String type) {
        switch (type) {
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
                prixField.setText("0 TND");
        }
    }

    @FXML
    private void handleAddAbonnement() {
        try {
            String type = typeCombo.getValue();
            String prixText = prixField.getText().replace(" TND", "");
            double prix = Double.parseDouble(prixText);
            java.sql.Date dateDebut = Date.valueOf(dateDebutPicker.getValue());
            java.sql.Date dateFin = Date.valueOf(dateFinPicker.getValue());
            String etat = etatCombo.getValue();

            if (type == null || dateDebut == null || dateFin == null || etat == null) {
                showNotification("Erreur : Veuillez remplir tous les champs.", "#f44336");
                return;
            }

            if (dateFin.before(dateDebut)) {
                showNotification("Erreur : La date de fin doit être postérieure à la date de début.", "#f44336");
                return;
            }

            abonnement abonnement = new abonnement();
            abonnement.settype(type);
            abonnement.setprix(prix);
            abonnement.setdate_debut(dateDebut);
            abonnement.setdate_fin(dateFin);
            abonnement.setetat(etat);

            int result = abonnementService.insert(abonnement);
            if (result > 0) {
                showNotification("Abonnement ajouté avec succès !", "#4CAF50");
                clearForm();
            } else {
                showNotification("Erreur : Échec de l'ajout de l'abonnement.", "#f44336");
            }
        } catch (NumberFormatException e) {
            showNotification("Erreur : Le prix doit être un nombre valide.", "#f44336");
        } catch (SQLException e) {
            showNotification("Erreur SQL : " + e.getMessage(), "#f44336");
            e.printStackTrace();
        } catch (Exception e) {
            showNotification("Erreur inattendue : " + e.getMessage(), "#f44336");
            e.printStackTrace();
        }
    }

    private void showNotification(String message, String backgroundColor) {
        notificationText.setText(message);
        notificationPane.setStyle("-fx-background-color: " + backgroundColor + "; -fx-border-radius: 5; -fx-background-radius: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 2, 2);");
        notificationPane.setVisible(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> notificationPane.setVisible(false));
        pause.play();
    }

    private void clearForm() {
        typeCombo.setValue(null);
        prixField.setText("");
        dateDebutPicker.setValue(null);
        dateFinPicker.setValue(null);
        etatCombo.setValue("Suspendu");
    }

    private User loggedInUser;
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }
}