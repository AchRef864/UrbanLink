package tn.esprit.jdbc.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.esprit.jdbc.entities.Taxi;
import javafx.scene.control.Alert;
import tn.esprit.jdbc.services.ServiceCourse;
import tn.esprit.jdbc.services.ServiceTaxi;

import java.io.IOException;
import java.sql.SQLException;

public class ListeTaxisController {

    @FXML private TableView<Taxi> tableView;
    @FXML private TableColumn<Taxi, Integer> colId;
    @FXML private TableColumn<Taxi, String> colImmatriculation;
    @FXML private TableColumn<Taxi, String> colMarque;
    @FXML private TableColumn<Taxi, String> colModele;
    @FXML private TableColumn<Taxi, Integer> colAnnee;
    @FXML private TableColumn<Taxi, Integer> colCapacite;
    @FXML private TableColumn<Taxi, String> colStatut;

    private final ServiceTaxi serviceTaxi = new ServiceTaxi();
    private final ServiceCourse serviceCourse = new ServiceCourse();


    @FXML
    public void initialize() {
        configurerColonnes();
        chargerDonnees();
    }

    private void configurerColonnes() {
        colImmatriculation.setCellValueFactory(new PropertyValueFactory<>("immatriculation"));
        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colModele.setCellValueFactory(new PropertyValueFactory<>("modele"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("anneeFabrication"));
        colCapacite.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }

    private void chargerDonnees() {
        try {
            tableView.setItems(FXCollections.observableArrayList(serviceTaxi.getAllTaxis()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void ModifierTaxi() {
        Taxi selectedTaxi = tableView.getSelectionModel().getSelectedItem();
        if (selectedTaxi != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierTaxi.fxml"));
                    Parent root = loader.load();

                    ModifierTaxiController controller = loader.getController();
                    controller.setTaxi(selectedTaxi);
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Modifier Taxi");

                    stage.setOnHidden(event -> {
                        Taxi taxiModifie = controller.getTaxiModifie();
                        chargerDonnees(); // Récupérer le taxi modifié
                        if (taxiModifie != null) {
                            int index = tableView.getItems().indexOf(selectedTaxi);
                            tableView.getItems().set(index, taxiModifie); // Mettre à jour uniquement ce taxi
                        }
                    });

                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        } else {
            afficherAlerte("Sélection requise", "Veuillez sélectionner un taxi à modifier.");
        }
    }
    @FXML
    public void supprimerTaxi() {
        Taxi selectedTaxi = tableView.getSelectionModel().getSelectedItem();
        if (selectedTaxi != null) {
            // Vérifie si l'utilisateur est admin
                try {
                    serviceTaxi.delete(selectedTaxi.getIdTaxi());
                    chargerDonnees();
                } catch (Exception e) {
                    afficherAlerte("Erreur", "Erreur lors de la suppression du taxi : " + e.getMessage());
                }
        } else {
            afficherAlerte("Sélection requise", "Veuillez sélectionner un taxi à supprimer.");
        }
    }
    @FXML
    public void ajouterTaxi() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutTaxi.fxml"));
            Parent root = loader.load();

            AjoutTaxiController controller = loader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter Taxi");

            stage.setOnHidden(event -> {
                Taxi taxiAjoute = controller.getTaxiAjoute();
                if (taxiAjoute != null) {
                    tableView.getItems().add(taxiAjoute);
                }
            });

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void ajouterCourse() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutCourse.fxml"));
            Parent root = loader.load();

            AjoutCourseController controller = loader.getController();
            controller.setCoursesList(serviceCourse.showAll());
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une Course");

            stage.setOnHidden(event -> {
                chargerDonnees(); // Rafraîchir la liste après la fermeture
            });

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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