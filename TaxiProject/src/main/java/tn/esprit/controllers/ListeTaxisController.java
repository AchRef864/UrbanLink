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
import tn.esprit.models.Taxi;
import javafx.scene.control.Alert;
import tn.esprit.services.ServiceTaxi;

import java.io.IOException;

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
    private static boolean isAdmin = false; // Stocke le statut admin

    public static void setIsAdmin(boolean adminStatus) {
        isAdmin = adminStatus;
    }

    @FXML
    public void initialize() {
        configurerColonnes();
        chargerDonnees();
        ajouterGestionDoubleClic();
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

    private void ajouterGestionDoubleClic() {
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Vérifie si l'utilisateur double-clique
                Taxi selectedTaxi = tableView.getSelectionModel().getSelectedItem();
                if (selectedTaxi != null) {
                    ouvrirDetailsTaxi(selectedTaxi);
                }
            }
        });
    }

    private void ouvrirDetailsTaxi(Taxi taxi) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TaxiDetails.fxml"));
            Parent root = loader.load();

            TaxiDetailsController controller = loader.getController();
            controller.setTaxi(taxi);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails du Taxi");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void ModifierTaxi() {
        Taxi selectedTaxi = tableView.getSelectionModel().getSelectedItem();
        if (selectedTaxi != null) {
            if (isAdmin) {
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
                afficherAlerte("Accès refusé", "Seuls les administrateurs peuvent modifier un taxi.");
            }
        } else {
            afficherAlerte("Sélection requise", "Veuillez sélectionner un taxi à modifier.");
        }
    }
    @FXML
    public void supprimerTaxi() {
        Taxi selectedTaxi = tableView.getSelectionModel().getSelectedItem();
        if (selectedTaxi != null) {
            if (isAdmin) { // Vérifie si l'utilisateur est admin
                try {
                    serviceTaxi.supprimer(selectedTaxi.getIdTaxi());
                    chargerDonnees();
                } catch (Exception e) {
                    afficherAlerte("Erreur", "Erreur lors de la suppression du taxi : " + e.getMessage());
                }
            } else {
                afficherAlerte("Accès refusé", "Seuls les administrateurs peuvent supprimer un taxi.");
            }
        } else {
            afficherAlerte("Sélection requise", "Veuillez sélectionner un taxi à supprimer.");
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
