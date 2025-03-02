package tn.esprit.jdbc.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import tn.esprit.jdbc.entities.Course;
import tn.esprit.jdbc.entities.Taxi;
import tn.esprit.jdbc.services.ServiceCourse;
import tn.esprit.jdbc.services.ServiceTaxi;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class AjoutCourseController {

    // Composants FXML
    @FXML private ComboBox<Taxi> comboBoxTaxi;
    @FXML private ComboBox<String> comboVilleDepart;
    @FXML private ComboBox<String> comboVilleArrivee;
    @FXML private DatePicker datePickerCourse;
    @FXML private TextField txtHeureCourse;
    @FXML private TextField txtDistance;
    @FXML private TextField txtMontant;
    @FXML private ComboBox<String> comboStatut;
    @FXML private Button btnAjouter;

    // Services
    private final ServiceCourse serviceCourse = new ServiceCourse();
    private final ServiceTaxi serviceTaxi = new ServiceTaxi();
    private List<Course> coursesList;
    private int userId;

    private final List<String> zones = Arrays.asList(
            "Tunis", "Sousse", "Sfax", "Nabeul", "Bizerte",
            "Gabès", "Ariana", "Ben Arous", "Manouba", "Mahdia"
    );

    @FXML
    public void initialize() {
        configurerComposants();
        configurerEcouteurs();
    }

    public void setCoursesList(List<Course> courses) {
        this.coursesList = courses;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public void setTaxiEtUtilisateur(Taxi taxi, int userId) {
        comboBoxTaxi.setValue(taxi);
        this.userId = userId;
    }


    private void chargerTaxis() {
        try {
            List<Taxi> taxis = serviceTaxi.showAll();
            ObservableList<Taxi> observableTaxis = FXCollections.observableArrayList(taxis);
            comboBoxTaxi.setItems(observableTaxis);
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Chargement des taxis échoué", Alert.AlertType.ERROR);
        }
    }

    private void configurerEcouteurs() {
        // Calcul automatique du montant
        comboBoxTaxi.valueProperty().addListener((obs, oldVal, newVal) -> calculerMontant());
        txtDistance.textProperty().addListener((obs, oldVal, newVal) -> calculerMontant());

        // Désactivation du bouton si formulaire incomplet
        btnAjouter.disableProperty().bind(
                comboBoxTaxi.valueProperty().isNull()
                        .or(comboVilleDepart.valueProperty().isNull())
                        .or(comboVilleArrivee.valueProperty().isNull())
                        .or(datePickerCourse.valueProperty().isNull())
                        .or(txtHeureCourse.textProperty().isEmpty())
                        .or(txtDistance.textProperty().isEmpty())
                        .or(comboStatut.valueProperty().isNull())
        );
    }

    private void calculerMontant() {
        Taxi taxi = comboBoxTaxi.getValue();
        String distanceText = txtDistance.getText().trim().replace(",", ".");

        if (taxi != null && !distanceText.isEmpty()) {
            try {
                double distance = Double.parseDouble(distanceText);
                double tarif = taxi.getTarifBase();
                txtMontant.setText(String.format("%.3f", distance * tarif));
            } catch (NumberFormatException e) {
                txtMontant.clear();
            }
        } else {
            txtMontant.clear();
        }
    }

    @FXML
    private void ajouterCourse() {
        if (!validerFormulaire()) return;

        try {
            Course course = creerCourseDepuisFormulaire();
            serviceCourse.insert(course);

            // Mise à jour du statut du taxi
            Taxi selectedTaxi = comboBoxTaxi.getValue();
            selectedTaxi.setStatut("En réservation");
            serviceTaxi.update(selectedTaxi);

            afficherAlerte("Succès", "Course ajoutée avec succès et taxi réservé !", Alert.AlertType.INFORMATION);
            fermerFenetre();
        } catch (SQLException e) {
            gererErreurSQL(e);
        } catch (Exception e) {
            afficherAlerte("Erreur", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private Course creerCourseDepuisFormulaire() throws DateTimeParseException {
        LocalDate date = datePickerCourse.getValue();
        LocalTime heure = LocalTime.parse(txtHeureCourse.getText().trim());

        double distance = Double.parseDouble(txtDistance.getText().trim().replace(",", "."));
        double montant = Double.parseDouble(txtMontant.getText().trim().replace(",", "."));

        return new Course(
                userId,
                comboBoxTaxi.getValue().getIdTaxi(),
                LocalDateTime.of(date, heure),
                comboVilleDepart.getValue(),
                comboVilleArrivee.getValue(),
                distance,
                montant,
                comboStatut.getValue()
        );
    }

    private boolean validerFormulaire() {
        if (comboVilleDepart.getValue().equals(comboVilleArrivee.getValue())) {
            afficherAlerte("Validation", "Les villes doivent être différentes", Alert.AlertType.WARNING);
            return false;
        }

        try {
            LocalDateTime dateCourse = LocalDateTime.of(datePickerCourse.getValue(), LocalTime.parse(txtHeureCourse.getText()));
            if (dateCourse.isBefore(LocalDateTime.now())) {
                afficherAlerte("Validation", "La date doit être dans le futur", Alert.AlertType.WARNING);
                return false;
            }
        } catch (DateTimeParseException e) {
            afficherAlerte("Format", "Format de l'heure invalide (HH:mm)", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void gererErreurSQL(SQLException e) {
        afficherAlerte("Erreur BD", "Problème avec la base de données : " + e.getMessage(), Alert.AlertType.ERROR);
    }

    private void afficherAlerte(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void configurerComposants() {
        comboVilleDepart.getItems().addAll(zones);
        comboVilleArrivee.getItems().addAll(zones);
        comboStatut.getItems().addAll("En attente");
        comboStatut.setValue("En attente");

        if (comboBoxTaxi != null) {
            comboBoxTaxi.setCellFactory(param -> new ListCell<Taxi>() {
                @Override
                protected void updateItem(Taxi taxi, boolean empty) {
                    super.updateItem(taxi, empty);
                    setText(empty || taxi == null ? "" :
                            taxi.getImmatriculation() + " - " + taxi.getMarque() + " " + taxi.getModele());
                }
            });

            comboBoxTaxi.setConverter(new StringConverter<Taxi>() {
                @Override
                public String toString(Taxi taxi) {
                    return taxi != null ? taxi.getImmatriculation() + " - " + taxi.getMarque() + " " + taxi.getModele() : "";
                }
                @Override
                public Taxi fromString(String string) {
                    return null;
                }
            });

            chargerTaxis();
        } else {
            System.out.println("comboBoxTaxi est null !");
        }
    }

    private void fermerFenetre() {
        Stage stage = (Stage) btnAjouter.getScene().getWindow();
        stage.close();
    }
}
