package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.esprit.models.Course;
import javafx.scene.control.Alert;
import tn.esprit.services.ServiceCourse;

import java.io.IOException;
import java.time.LocalDateTime;

public class ListeCoursesController {

    @FXML private TableView<Course> tableView;
    @FXML private TableColumn<Course, Integer> colId;
    @FXML private TableColumn<Course, Integer> colUserId;
    @FXML private TableColumn<Course, Integer> colTaxiId;
    @FXML private TableColumn<Course, LocalDateTime> colDateCourse;
    @FXML private TableColumn<Course, String> colVilleDepart;
    @FXML private TableColumn<Course, String> colVilleArrivee;
    @FXML private TableColumn<Course, Double> colDistance;
    @FXML private TableColumn<Course, Double> colMontant;
    @FXML private TableColumn<Course, String> colStatut;

    private final ServiceCourse serviceCourse = new ServiceCourse();
    private static boolean isAdmin = false; // Stocke le statut admin
    private static boolean isUser = false; // Stocke le statut user

    public static void setIsAdmin(boolean adminStatus) {
        isAdmin = adminStatus;
    }
    public static void setIsUser(boolean userStatus) {
        isUser = userStatus;
    }

    @FXML
    public void initialize() {
        if(isAdmin){
        configurerColonnes();
        chargerDonnees();}
        else if (isUser){
            configurerColonness();
            chargerDonneess();
        }
        else{
            afficherAlerte("Accès refusé", "Vous n'avez pas les droits pour accéder à cette page.");
        }

    }

    private void configurerColonnes() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_course"));
        colUserId.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        colTaxiId.setCellValueFactory(new PropertyValueFactory<>("id_taxi"));
        colDateCourse.setCellValueFactory(new PropertyValueFactory<>("date_course"));
        colVilleDepart.setCellValueFactory(new PropertyValueFactory<>("ville_depart"));
        colVilleArrivee.setCellValueFactory(new PropertyValueFactory<>("ville_arrivee"));
        colDistance.setCellValueFactory(new PropertyValueFactory<>("distance_km"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }
    private void configurerColonness() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_course"));
        colUserId.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        colTaxiId.setCellValueFactory(new PropertyValueFactory<>("id_taxi"));
        colDateCourse.setCellValueFactory(new PropertyValueFactory<>("date_course"));
        colVilleDepart.setCellValueFactory(new PropertyValueFactory<>("ville_depart"));
        colVilleArrivee.setCellValueFactory(new PropertyValueFactory<>("ville_arrivee"));
        colDistance.setCellValueFactory(new PropertyValueFactory<>("distance_km"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }

    private void chargerDonnees() {
        try {
            tableView.setItems(FXCollections.observableArrayList(serviceCourse.afficher()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void chargerDonneess() {
        try {
            tableView.setItems(FXCollections.observableArrayList(serviceCourse.afficherByUser( LoginController.userId)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void ajouterCourse() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutCourse.fxml"));
            Parent root = loader.load();

            AjoutCourseController controller = loader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une Course");

            stage.setOnHidden(event -> {
                chargerDonnees(); // Rafraîchir la liste après la fermeture
            });

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void modifierCourse() {
        Course selectedCourse = tableView.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            if (isAdmin) {
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
                            tableView.getItems().set(index, courseModifiee); // Mettre à jour uniquement cette course
                        }
                    });

                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                afficherAlerte("Accès refusé", "Seuls les administrateurs peuvent modifier une course.");
            }
        } else {
            afficherAlerte("Sélection requise", "Veuillez sélectionner une course à modifier.");
        }
    }

    @FXML
    private void supprimerCourse() {
        Course selectedCourse = tableView.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            if (isAdmin) { // Vérifie si l'utilisateur est admin
                try {
                    serviceCourse.supprimer(selectedCourse.getId_course());
                    chargerDonnees();
                } catch (Exception e) {
                    afficherAlerte("Erreur", "Erreur lors de la suppression de la course : " + e.getMessage());
                }
            } else {
                afficherAlerte("Accès refusé", "Seuls les administrateurs peuvent supprimer une course.");
            }
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

    @FXML
    private void retourAccueil() {
        ((Stage) tableView.getScene().getWindow()).close();
    }
}