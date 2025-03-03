package tn.esprit.jdbc.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.esprit.jdbc.entities.Course;
import tn.esprit.jdbc.entities.Taxi;
import tn.esprit.jdbc.services.ServiceCourse;
import tn.esprit.jdbc.services.ServiceTaxi;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public class ListeCoursesController {

    @FXML private TableView<Course> tableView;
    @FXML private TableColumn<Course, LocalDateTime> colDateCourse;
    @FXML private TableColumn<Course, String> colVilleDepart;
    @FXML private TableColumn<Course, String> colVilleArrivee;
    @FXML private TableColumn<Course, Double> colDistance;
    @FXML private TableColumn<Course, Double> colMontant;
    @FXML private TableColumn<Course, String> colStatut;

    private final ServiceCourse serviceCourse = new ServiceCourse();
    private final ServiceTaxi serviceTaxi = new ServiceTaxi();

    @FXML
    public void initialize() {
        configurerColonnes();
        chargerDonnees();
    }

    private void configurerColonnes() {
        colDateCourse.setCellValueFactory(new PropertyValueFactory<>("date_course"));
        colVilleDepart.setCellValueFactory(new PropertyValueFactory<>("ville_depart"));
        colVilleArrivee.setCellValueFactory(new PropertyValueFactory<>("ville_arrivee"));
        colDistance.setCellValueFactory(new PropertyValueFactory<>("distance_km"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }

    private void chargerDonnees() {
        try {
            tableView.getItems().clear();
            tableView.setItems(FXCollections.observableArrayList(serviceCourse.showAll()));
        } catch (Exception e) {
            afficherAlerte("Erreur de chargement", "Impossible de charger les données : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void modifierCourse() {
        Course selectedCourse = tableView.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierCourse.fxml"));
                Parent root = loader.load();
                ModifierCourseController controller = loader.getController();
                controller.setCourse(selectedCourse);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier la Course");
                stage.setOnHidden(event -> {
                    Course courseModifiee = controller.getCourseModifiee();
                    if (courseModifiee != null) {
                        int index = tableView.getItems().indexOf(selectedCourse);
                        tableView.getItems().set(index, courseModifiee);
                    }
                });
                stage.show();
            } catch (IOException e) {
                afficherAlerte("Erreur", "Impossible d'ouvrir la fenêtre de modification.");
                e.printStackTrace();
            }
        } else {
            afficherAlerte("Sélection requise", "Veuillez sélectionner une course à modifier.");
        }
    }

    @FXML
    private void supprimerCourse() {
        Course selectedCourse = tableView.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation de suppression");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cette course ?");
            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    serviceCourse.delete(selectedCourse.getId_course());
                    chargerDonnees();
                } catch (Exception e) {
                    afficherAlerte("Erreur", "Erreur lors de la suppression de la course : " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            afficherAlerte("Sélection requise", "Veuillez sélectionner une course à supprimer.");
        }
    }

    /**
     * Méthode pour mettre à jour le statut de la course et du taxi associé.
     */
    @FXML
    private void updateCourseStatus() {
        Course selectedCourse = tableView.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            // Vérifie que la course est en "En attente"
            if (!"En attente".equals(selectedCourse.getStatut())) {
                afficherAlerte("Erreur", "La course n'est pas en attente et ne peut pas être mise à jour.", Alert.AlertType.WARNING);
                return;
            }
            try {
                // Récupération du taxi associé (on suppose que la course possède un champ id_taxi accessible via getId_taxi())
                Taxi associatedTaxi = serviceTaxi.getTaxiById(selectedCourse.getId_taxi());
                if (associatedTaxi == null) {
                    afficherAlerte("Erreur", "Taxi associé non trouvé.", Alert.AlertType.ERROR);
                    return;
                }
                if (!"En réservation".equals(associatedTaxi.getStatut())) {
                    afficherAlerte("Erreur", "Le taxi associé n'est pas en réservation.", Alert.AlertType.WARNING);
                    return;
                }
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateCourseStatus.fxml"));
                Parent root = loader.load();
                UpdateCourseStatusController controller = loader.getController();
                controller.setCourseAndTaxi(selectedCourse, associatedTaxi);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Mettre à jour le statut de la course");
                stage.setOnHidden(event -> chargerDonnees());
                stage.show();
            } catch (IOException e) {
                afficherAlerte("Erreur", "Impossible d'ouvrir la fenêtre de mise à jour du statut.", Alert.AlertType.ERROR);
                e.printStackTrace();
            } catch (Exception e) {
                afficherAlerte("Erreur", "Erreur : " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        } else {
            afficherAlerte("Sélection requise", "Veuillez sélectionner une course à mettre à jour.");
        }
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherAlerte(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void retourAccueil() {
        ((Stage) tableView.getScene().getWindow()).close();
    }
}
