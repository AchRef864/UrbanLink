package tn.esprit.jdbc.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.esprit.jdbc.entities.Taxi;
import tn.esprit.jdbc.services.ServiceCourse;
import tn.esprit.jdbc.services.ServiceTaxi;

import java.io.IOException;
import java.util.List;

public class ListeTaxisController {

    @FXML private TableView<Taxi> tableView;
    @FXML private TableColumn<Taxi, Integer> colId;
    @FXML private TableColumn<Taxi, String> colImmatriculation;
    @FXML private TableColumn<Taxi, String> colMarque;
    @FXML private TableColumn<Taxi, String> colModele;
    @FXML private TableColumn<Taxi, Integer> colAnnee;
    @FXML private TableColumn<Taxi, Integer> colCapacite;
    @FXML private TableColumn<Taxi, String> colStatut;
    // Champ de recherche (à déclarer dans votre FXML)
    @FXML private TextField txtRecherche;

    private final ServiceTaxi serviceTaxi = new ServiceTaxi();
    private final ServiceCourse serviceCourse = new ServiceCourse();

    // Liste complète des taxis chargés
    private ObservableList<Taxi> taxiList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurerColonnes();
        chargerDonnees();
        tableView.getColumns().remove(colId); // Supprimer la colonne ID de l'affichage
        txtRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            rechercherTaxi(newValue);
        });
    }

    private void configurerColonnes() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idTaxi"));
        colImmatriculation.setCellValueFactory(new PropertyValueFactory<>("immatriculation"));
        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colModele.setCellValueFactory(new PropertyValueFactory<>("modele"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("anneeFabrication"));
        colCapacite.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }

    private void chargerDonnees() {
        try {
            List<Taxi> taxis = serviceTaxi.getAllTaxis();
            taxiList = FXCollections.observableArrayList(taxis);
            tableView.setItems(taxiList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode de recherche qui filtre la liste des taxis selon le texte saisi.
     *
     * @param query Le texte de recherche.
     */
    private void rechercherTaxi(String query) {
        if (query == null || query.isEmpty()) {
            tableView.setItems(taxiList);
        } else {
            String lowerQuery = query.toLowerCase();
            ObservableList<Taxi> filteredList = FXCollections.observableArrayList();

            for (Taxi taxi : taxiList) {
                // Vérifier chaque champ pour voir s'il correspond à la recherche
                if (taxi.getImmatriculation().toLowerCase().contains(lowerQuery)
                        || taxi.getMarque().toLowerCase().contains(lowerQuery)
                        || taxi.getModele().toLowerCase().contains(lowerQuery)
                        || String.valueOf(taxi.getAnneeFabrication()).contains(lowerQuery) // Vérifier l'année
                        || String.valueOf(taxi.getCapacite()).contains(lowerQuery) // Vérifier la capacité
                        || taxi.getStatut().toLowerCase().contains(lowerQuery)) { // Vérifier le statut
                    filteredList.add(taxi);
                }
            }
            tableView.setItems(filteredList);
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
                    chargerDonnees(); // Recharger la liste après modification
                    if (taxiModifie != null) {
                        int index = tableView.getItems().indexOf(selectedTaxi);
                        tableView.getItems().set(index, taxiModifie);
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
                    taxiList.add(taxiAjoute);
                    tableView.setItems(taxiList);
                }
            });

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
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
