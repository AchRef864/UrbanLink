package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import tn.esprit.jdbc.entities.Course;
import tn.esprit.jdbc.entities.Taxi;
import tn.esprit.jdbc.services.ServiceCourse;
import tn.esprit.jdbc.services.ServiceTaxi;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class UpdateCourseStatusController {

    @FXML
    private ComboBox<String> comboStatus;
    @FXML
    private Button btnValider;
    @FXML
    private Button btnAnnuler;

    private Course course;
    private Taxi taxi;

    private final ServiceCourse serviceCourse = new ServiceCourse();
    private final ServiceTaxi serviceTaxi = new ServiceTaxi();

    // Statuts disponibles pour la mise à jour
    private final List<String> statuses = Arrays.asList("Confirmée", "Annulée");

    @FXML
    public void initialize() {
        comboStatus.getItems().clear();
        comboStatus.getItems().addAll(statuses);
    }

    /**
     * Permet de transmettre la course et le taxi concernés par la modification.
     */
    public void setCourseAndTaxi(Course course, Taxi taxi) {
        this.course = course;
        this.taxi = taxi;
    }

    /**
     * Méthode appelée lors du clic sur le bouton Valider.
     */
    @FXML
    private void handleValider() {
        if (course == null || taxi == null) {
            afficherAlerte("Erreur", "Données manquantes pour la course ou le taxi.", Alert.AlertType.ERROR);
            return;
        }
        if (!"En attente".equals(course.getStatut())) {
            afficherAlerte("Erreur", "La course n'est pas en attente.", Alert.AlertType.WARNING);
            return;
        }
        if (!"En réservation".equals(taxi.getStatut())) {
            afficherAlerte("Erreur", "Le taxi n'est pas en réservation.", Alert.AlertType.WARNING);
            return;
        }
        String selectedStatus = comboStatus.getValue();
        if (selectedStatus == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner un statut.", Alert.AlertType.WARNING);
            return;
        }
        try {
            // Mise à jour du statut de la course
            course.setStatut(selectedStatus);
            serviceCourse.update(course);

            // Mise à jour du taxi selon le choix effectué
            if ("Confirmée".equals(selectedStatus)) {
                taxi.setStatut("En service");
            } else if ("Annulée".equals(selectedStatus)) {
                taxi.setStatut("Disponible");
            }
            serviceTaxi.update(taxi);

            afficherAlerte("Succès", "La course et le taxi ont été mis à jour.", Alert.AlertType.INFORMATION);
            fermerFenetre();
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Erreur lors de la mise à jour : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Ferme la fenêtre.
     */
    @FXML
    private void handleAnnuler() {
        fermerFenetre();
    }

    private void afficherAlerte(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) btnValider.getScene().getWindow();
        stage.close();
    }
}
