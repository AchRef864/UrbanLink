package tn.esprit.controllers;

import tn.esprit.models.Taxi;
import tn.esprit.services.ServiceTaxi;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.time.LocalDate;

public class AjoutTaxiController {

    @FXML
    private TextField txtImmatriculation;
    @FXML
    private TextField txtMarque;
    @FXML
    private TextField txtModele;
    @FXML
    private TextField txtAnnee;
    @FXML
    private TextField txtCapacite;
    @FXML
    private TextField txtZone;

    private final ServiceTaxi serviceTaxi = new ServiceTaxi();

    @FXML
    void ajouterTaxi(ActionEvent event) {
        try {
            String immatriculation = txtImmatriculation.getText();
            String marque = txtMarque.getText();
            String modele = txtModele.getText();
            int annee = Integer.parseInt(txtAnnee.getText());
            int capacite = Integer.parseInt(txtCapacite.getText());
            String zone = txtZone.getText();

            // Valeurs par défaut pour les autres champs
            String statut = "Disponible";  // Statut par défaut
            String licenceNumero = "Inconnu";  // Numéro de licence par défaut
            LocalDate licenceDateObtention = LocalDate.now();  // Date d'obtention de la licence par défaut
            Double tarifBase = 0.0;  // Tarif de base par défaut

            // Création de l'objet Taxi avec tous les paramètres requis
            Taxi taxi = new Taxi(0, immatriculation, marque, modele, annee, capacite, zone, statut, licenceNumero, licenceDateObtention, tarifBase);
            serviceTaxi.ajouter(taxi);

            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Taxi ajouté avec succès !");
            viderChamps();
        } catch (NumberFormatException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer des valeurs valides !");
        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout du taxi !");
        }
    }


    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void viderChamps() {
        txtImmatriculation.clear();
        txtMarque.clear();
        txtModele.clear();
        txtAnnee.clear();
        txtCapacite.clear();
        txtZone.clear();
    }
}
