package tn.esprit.jdbc.controllers.abonnement;

import tn.esprit.jdbc.services.abonnementservices;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.jdbc.services.reservationservice;
import tn.esprit.jdbc.entities.abonnement;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ModifierAbonnementController {

    @FXML
    private TextField txtType;
    @FXML
    private TextField txtPrix;
    @FXML
    private DatePicker dateDebutPicker;
    @FXML
    private DatePicker dateFinPicker;
    @FXML
    private ComboBox<String> comboEtat;
    @FXML
    private Button btnModifier;

    private final abonnementservices abonnementService = new abonnementservices();
    private abonnement abonnementActuel;

    public void setAbonnement(abonnement abonnement) {
        this.abonnementActuel = abonnement;

        if (abonnement != null) {
            txtType.setText(abonnement.gettype());
            txtPrix.setText(String.valueOf(abonnement.getprix()));

            // Convertir les dates SQL en LocalDate pour DatePicker
            if (abonnement.getdate_debut() != null) {
                dateDebutPicker.setValue(new Date(abonnement.getdate_debut().getTime()).toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate());
            }
            if (abonnement.getdate_fin() != null) {
                dateFinPicker.setValue(new Date(abonnement.getdate_fin().getTime()).toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate());
            }

            comboEtat.setValue(abonnement.getetat());
        }
    }

    @FXML
    public void initialize() {
        // Initialisation de la liste des états possibles
        comboEtat.getItems().addAll("Actif", "Expiré", "Suspendu");
    }

    @FXML
    private void modifierAbonnement() {
        if (abonnementActuel == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun abonnement sélectionné !");
            return;
        }

        try {
            // Vérifier si les champs sont bien remplis
            String type = txtType.getText().trim();
            String prixStr = txtPrix.getText().trim();
            LocalDate dateDebut = dateDebutPicker.getValue();
            LocalDate dateFin = dateFinPicker.getValue();
            String etat = comboEtat.getValue();

            if (type.isEmpty() || prixStr.isEmpty() || dateDebut == null || dateFin == null || etat == null) {
                showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
                return;
            }

            double prix = Double.parseDouble(prixStr);
            Date dateDebutSql = Date.from(dateDebut.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date dateFinSql = Date.from(dateFin.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Mettre à jour l'abonnement
            abonnementActuel.settype(type);
            abonnementActuel.setprix(prix);
            abonnementActuel.setdate_debut(dateDebutSql);
            abonnementActuel.setdate_fin(dateFinSql);
            abonnementActuel.setetat(etat);

            abonnementService.update(abonnementActuel);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Abonnement modifié avec succès !");
            fermerFenetre();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Format invalide", "Veuillez entrer un prix valide.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de modifier l'abonnement.");
            e.printStackTrace();
        }
    }

    private void fermerFenetre() {
        Stage stage = (Stage) btnModifier.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
