package tn.esprit.jdbc.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.esprit.jdbc.entities.Taxi;
import tn.esprit.jdbc.entities.Course;
import tn.esprit.jdbc.services.ServiceTaxi;
import tn.esprit.jdbc.services.ServiceCourse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ListeTaxisEtCoursesController {

    @FXML private TableView<Taxi> tableTaxis;
    @FXML private TableColumn<Taxi, String> colTaxiMarque;
    @FXML private TableColumn<Taxi, String> colTaxiModele;
    @FXML private TableColumn<Taxi, Integer> colTaxiCapacite;
    @FXML private TableColumn<Taxi, String> colTaxiStatut;
    @FXML private Button btnReserverTaxi;

    @FXML private TableView<Course> tableCourses;
    @FXML private TableColumn<Course, String> colVilleDepart;
    @FXML private TableColumn<Course, String> colVilleArrivee;
    @FXML private TableColumn<Course, Double> colDistance;
    @FXML private TableColumn<Course, Double> colMontant;
    @FXML private Button btnModifierCourse;
    @FXML private Button btnSupprimerCourse;

    private final ServiceTaxi serviceTaxi = new ServiceTaxi();
    private final ServiceCourse serviceCourse = new ServiceCourse();
    private int userId; // Stocke l'ID de l'utilisateur connecté


    public void setUserId(int userId) {
        this.userId = userId;
        chargerTaxisDisponibles();
        chargerCoursesUtilisateur();
    }


    @FXML
    public void initialize() {
        configurerColonnes();
    }

    private void configurerColonnes() {
        // Configuration des colonnes pour les taxis
        colTaxiMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colTaxiModele.setCellValueFactory(new PropertyValueFactory<>("modele"));
        colTaxiCapacite.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        colTaxiStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Configuration des colonnes pour les courses
        colVilleDepart.setCellValueFactory(new PropertyValueFactory<>("ville_depart"));
        colVilleArrivee.setCellValueFactory(new PropertyValueFactory<>("ville_arrivee"));
        colDistance.setCellValueFactory(new PropertyValueFactory<>("distance_km"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
    }

    private void chargerTaxisDisponibles() {
        try {
            List<Taxi> taxisDisponibles = serviceTaxi.showAll().stream()
                    .filter(taxi -> taxi.getStatut().equalsIgnoreCase("Disponible"))
                    .collect(Collectors.toList());

            tableTaxis.setItems(FXCollections.observableArrayList(taxisDisponibles));
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Problème lors du chargement des taxis disponibles.");
        }
    }

    private void chargerCoursesUtilisateur() {
        try {
            List<Course> coursesUser = serviceCourse.showAll().stream()
                    .filter(course -> course.getUser_id() == userId)
                    .collect(Collectors.toList());

            tableCourses.setItems(FXCollections.observableArrayList(coursesUser));
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Problème lors du chargement des courses de l'utilisateur.");
        }
    }

    @FXML
    private void handleAjouterCourse() {
        Taxi selectedTaxi = tableTaxis.getSelectionModel().getSelectedItem();
        if (selectedTaxi != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutCourse.fxml"));
                Parent root = loader.load();

                AjoutCourseController controller = loader.getController();
                controller.setTaxiEtUtilisateur(selectedTaxi, userId);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Ajouter une Course");

                stage.setOnHidden(event -> {
                    selectedTaxi.setStatut("En réservation");
                    try {
                        serviceTaxi.update(selectedTaxi);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    chargerTaxisDisponibles();
                    chargerCoursesUtilisateur();
                });

                stage.show();
            } catch (IOException e) {
                afficherAlerte("Erreur", "Problème lors du chargement de la fenêtre d'ajout de course.");
            }
        } else {
            afficherAlerte("Sélection requise", "Veuillez sélectionner un taxi disponible.");
        }
    }

    @FXML
    private void handleModifierCourse() {
        Course selectedCourse = tableCourses.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierCourse.fxml"));
                Parent root = loader.load();

                ModifierCourseController controller = loader.getController();
                controller.setCourse(selectedCourse);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier la Course");

                stage.setOnHidden(event -> chargerCoursesUtilisateur());

                stage.show();
            } catch (IOException e) {
                afficherAlerte("Erreur", "Problème lors du chargement de la modification.");
            }
        } else {
            afficherAlerte("Sélection requise", "Veuillez sélectionner une course à modifier.");
        }
    }

    @FXML
    private void handleSupprimerCourse() throws SQLException {
        Course selectedCourse = tableCourses.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            serviceCourse.delete(selectedCourse.getId_course());
            chargerCoursesUtilisateur();
        } else {
            afficherAlerte("Sélection requise", "Veuillez sélectionner une course à supprimer.");
        }
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
