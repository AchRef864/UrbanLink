package tn.esprit.jdbc.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.jdbc.entities.Course;
import tn.esprit.jdbc.services.ServiceCourse;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class ModifierCourseController {

    @FXML private TextField txtVilleDepart, txtVilleArrivee, txtDistance, txtMontant, txtTime;
    @FXML private DatePicker datePickerCourse;
    @FXML private ComboBox<String> comboStatut;
    @FXML private Button btnModifier;

    private int userId;
    private int taxiId;
    private final ServiceCourse serviceCourse = new ServiceCourse();
    private Course courseActuel;
    private Course courseModifie;

    @FXML
    void initialize() {
        initialiserComposants();
        configurerEcouteurs();
    }

    public Course getCourseModifiee() {
        return courseModifie;
    }

    private void initialiserComposants() {
        // Initialisation du combo pour le statut avec quelques exemples
        comboStatut.getItems().addAll("En attente", "Confirmée", "Annulée");
    }

    private void configurerEcouteurs() {
        List<TextField> champs = Arrays.asList(txtVilleDepart, txtVilleArrivee, txtDistance, txtMontant, txtTime);
        champs.forEach(champ -> champ.textProperty().addListener((observable, oldValue, newValue) -> mettreAJourBouton()));
        comboStatut.valueProperty().addListener((obs, oldVal, newVal) -> mettreAJourBouton());
        datePickerCourse.valueProperty().addListener((obs, oldVal, newVal) -> mettreAJourBouton());
    }

    private void mettreAJourBouton() {
        boolean champsValides = !txtVilleDepart.getText().isEmpty()
                && !txtVilleArrivee.getText().isEmpty()
                && !txtDistance.getText().isEmpty()
                && !txtMontant.getText().isEmpty()
                && !txtTime.getText().isEmpty()
                && comboStatut.getValue() != null
                && datePickerCourse.getValue() != null;
        btnModifier.setDisable(!champsValides);
    }

    /**
     * Remplit les champs du formulaire avec les données de la course existante.
     */
    public void setCourse(Course course) {
        this.courseActuel = course;
        if (course != null) {
            this.userId = course.getUser_id();  // Stocke l'ID utilisateur
            this.taxiId = course.getId_taxi();  // Stocke l'ID taxi

            datePickerCourse.setValue(course.getDate_course().toLocalDate());
            LocalTime time = course.getDate_course().toLocalTime();
            txtTime.setText(time.toString());
            txtVilleDepart.setText(course.getVille_depart());
            txtVilleArrivee.setText(course.getVille_arrivee());
            txtDistance.setText(String.valueOf(course.getDistance_km()));
            txtMontant.setText(String.valueOf(course.getMontant()));
            comboStatut.setValue(course.getStatut());
        }
    }

    /**
     * Appelé lors du clic sur le bouton Modifier.
     */
    @FXML
    void modifierCourse(ActionEvent event) {
        if (!validerFormulaire()) return;

        try {
            LocalDate date = datePickerCourse.getValue();
            LocalTime time = LocalTime.parse(txtTime.getText().trim());
            LocalDateTime dateTime = LocalDateTime.of(date, time);
            String villeDepart = txtVilleDepart.getText().trim();
            String villeArrivee = txtVilleArrivee.getText().trim();
            double distance = Double.parseDouble(txtDistance.getText().trim());
            double montant = Double.parseDouble(txtMontant.getText().trim());
            String statut = comboStatut.getValue();

            Course modifie = new Course(
                    courseActuel.getId_course(),
                    userId,      // Utilise l'ID stocké
                    taxiId,      // Utilise l'ID stocké
                    courseActuel.getDate_reservation(),
                    dateTime,
                    villeDepart,
                    villeArrivee,
                    distance,
                    montant,
                    statut
            );

            serviceCourse.update(modifie);
            afficherAlerte("Succès", "Course modifiée avec succès !", Alert.AlertType.INFORMATION);
            courseModifie = modifie;
            fermerFenetre();
        } catch (SQLException e) {
            afficherAlerte("Erreur SQL", "Problème de modification : " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (NumberFormatException e) {
            afficherAlerte("Erreur de format", "Veuillez vérifier les valeurs numériques", Alert.AlertType.WARNING);
        }
    }

    /**
     * Vérifie que les champs du formulaire sont correctement renseignés.
     */
    private boolean validerFormulaire() {
        // Vérification du format de l'heure
        String timeText = txtTime.getText().trim();
        try {
            LocalTime.parse(timeText);
        } catch (Exception e) {
            afficherAlerte("Format incorrect", "Le format de l'heure doit être HH:mm ou HH:mm:ss", Alert.AlertType.WARNING);
            return false;
        }

        // Vérifier que les villes de départ et d'arrivée ne sont pas vides
        if (txtVilleDepart.getText().trim().isEmpty() || txtVilleArrivee.getText().trim().isEmpty()) {
            afficherAlerte("Champ vide", "Les villes de départ et d'arrivée doivent être renseignées", Alert.AlertType.WARNING);
            return false;
        }

        // Validation de la distance
        try {
            double distance = Double.parseDouble(txtDistance.getText().trim());
            if (distance <= 0) {
                afficherAlerte("Valeur incorrecte", "La distance doit être supérieure à 0", Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            afficherAlerte("Format incorrect", "La distance doit être un nombre", Alert.AlertType.WARNING);
            return false;
        }

        // Validation du montant
        try {
            double montant = Double.parseDouble(txtMontant.getText().trim());
            if (montant < 0) {
                afficherAlerte("Valeur incorrecte", "Le montant ne peut pas être négatif", Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            afficherAlerte("Format incorrect", "Le montant doit être un nombre", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void afficherAlerte(String titre, String message, Alert.AlertType type) {
        Alert alerte = new Alert(type);
        alerte.setTitle(titre);
        alerte.setHeaderText(null);
        alerte.setContentText(message);
        alerte.showAndWait();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) btnModifier.getScene().getWindow();
        stage.close();
    }
}
