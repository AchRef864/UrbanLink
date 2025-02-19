package tn.esprit.controllers;

import tn.esprit.models.Course;
import tn.esprit.services.ServiceCourse;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.sql.SQLException;

public class AjoutCourseController {

    // Composants FXML
    @FXML private ComboBox<String> comboStatut;
    @FXML private DatePicker datePickerCourse;
    @FXML private TextField txtUserId, txtTaxiId, txtVilleDepart, txtVilleArrivee, txtDistance, txtMontant;
    @FXML private Button btnAjouter;

    private final ServiceCourse serviceCourse = new ServiceCourse();
    private static boolean isAdmin = false; // Variable pour vérifier si l'utilisateur est admin

    // Méthode pour définir le statut admin
    public static void setIsAdmin(boolean adminStatus) {
        isAdmin = adminStatus;
    }

    @FXML
    void initialize() {
        initialiserComposants();
        configurerDatePicker();
        configurerEcouteurs();

        // Désactiver le bouton si l'utilisateur n'est pas admin
        if (!isAdmin) {
            btnAjouter.setDisable(true);
            afficherAlerte("Accès refusé", "Seuls les administrateurs peuvent ajouter une course.", Alert.AlertType.WARNING);
        }
    }

    private void initialiserComposants() {
        // Initialisation des ComboBox
        comboStatut.getItems().addAll("En attente", "Confirmée", "Annulée");
        comboStatut.getSelectionModel().selectFirst(); // Sélection par défaut
    }

    private void configurerDatePicker() {
        datePickerCourse.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isBefore(LocalDate.now())); // Désactiver les dates passées
            }
        });
    }

    private void configurerEcouteurs() {
        // Écouteurs pour les champs texte
        List<TextField> champs = Arrays.asList(
                txtUserId, txtTaxiId, txtVilleDepart, txtVilleArrivee, txtDistance, txtMontant
        );

        champs.forEach(champ -> champ.textProperty().addListener(
                (observable, ancienneValeur, nouvelleValeur) -> mettreAJourBouton()
        ));

        // Écouteurs pour les ComboBox
        comboStatut.valueProperty().addListener((obs, oldVal, newVal) -> mettreAJourBouton());

        // Écouteur pour le DatePicker
        datePickerCourse.valueProperty().addListener((obs, oldVal, newVal) -> mettreAJourBouton());
    }

    private void mettreAJourBouton() {
        boolean champsValides = !txtUserId.getText().isEmpty()
                && !txtTaxiId.getText().isEmpty()
                && !txtVilleDepart.getText().isEmpty()
                && !txtVilleArrivee.getText().isEmpty()
                && !txtDistance.getText().isEmpty()
                && !txtMontant.getText().isEmpty()
                && comboStatut.getValue() != null
                && datePickerCourse.getValue() != null;

        btnAjouter.setDisable(!champsValides || !isAdmin); // Désactiver si les champs ne sont pas valides ou si l'utilisateur n'est pas admin
    }

    @FXML
    void ajouterCourse(ActionEvent event) {
        if (!isAdmin) {
            afficherAlerte("Accès refusé", "Seuls les administrateurs peuvent ajouter une course.", Alert.AlertType.WARNING);
            return;
        }

        if (!validerFormulaire()) return;

        try {
            Course nouvelleCourse = creerCourseDepuisFormulaire();
            serviceCourse.ajouter(nouvelleCourse);
            afficherAlerte("Succès", "Course ajoutée avec succès !", Alert.AlertType.INFORMATION);
            reinitialiserFormulaire();
        } catch (SQLException e) {
            gererErreurSQL(e);
        } catch (Exception e) {
            afficherAlerte("Erreur", "Erreur inattendue : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private Course creerCourseDepuisFormulaire() {
        return new Course(
                0, // id_course sera généré automatiquement par la base de données
                Integer.parseInt(txtUserId.getText().trim()),
                Integer.parseInt(txtTaxiId.getText().trim()),
                LocalDateTime.now(), // date_reservation (maintenant)
                datePickerCourse.getValue().atStartOfDay(), // date_course
                txtVilleDepart.getText().trim(),
                txtVilleArrivee.getText().trim(),
                Double.parseDouble(txtDistance.getText().trim()),
                Double.parseDouble(txtMontant.getText().trim()),
                comboStatut.getValue()
        );
    }

    private boolean validerFormulaire() {
        // Validation des champs numériques
        try {
            int userId = Integer.parseInt(txtUserId.getText().trim());
            int taxiId = Integer.parseInt(txtTaxiId.getText().trim());
            double distance = Double.parseDouble(txtDistance.getText().trim());
            double montant = Double.parseDouble(txtMontant.getText().trim());

            if (userId <= 0 || taxiId <= 0 || distance <= 0 || montant <= 0) {
                afficherAlerte("Valeur invalide", "Les valeurs doivent être positives.", Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            afficherAlerte("Format incorrect", "Veuillez saisir des valeurs numériques valides.", Alert.AlertType.WARNING);
            return false;
        }

        // Validation des champs texte
        if (txtVilleDepart.getText().trim().isEmpty() || txtVilleArrivee.getText().trim().isEmpty()) {
            afficherAlerte("Champs manquants", "Veuillez remplir tous les champs.", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void gererErreurSQL(SQLException e) {
        String message = "Erreur lors de l'ajout : ";
        if (e.getMessage().contains("user_id")) {
            message += "L'utilisateur n'existe pas !";
        } else if (e.getMessage().contains("id_taxi")) {
            message += "Le taxi n'existe pas !";
        } else {
            message += e.getMessage();
        }
        afficherAlerte("Erreur SQL", message, Alert.AlertType.ERROR);
    }

    private void afficherAlerte(String titre, String message, Alert.AlertType type) {
        Alert alerte = new Alert(type);
        alerte.setTitle(titre);
        alerte.setHeaderText(null);
        alerte.setContentText(message);
        alerte.showAndWait();
    }

    private void reinitialiserFormulaire() {
        // Réinitialisation des champs
        txtUserId.clear();
        txtTaxiId.clear();
        txtVilleDepart.clear();
        txtVilleArrivee.clear();
        txtDistance.clear();
        txtMontant.clear();

        // Réinitialisation des sélections
        comboStatut.getSelectionModel().selectFirst();
        datePickerCourse.setValue(null);
    }
}