package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tn.esprit.models.Taxi;
import java.time.format.DateTimeFormatter;

public class TaxiDetailsController {

    // Références FXML
    @FXML private Label lblId;
    @FXML private Label lblImmatriculation;
    @FXML private Label lblMarque;
    @FXML private Label lblModele;
    @FXML private Label lblAnnee;
    @FXML private Label lblCapacite;
    @FXML private Label lblZone;
    @FXML private Label lblStatut;
    @FXML private Label lblLicence;
    @FXML private Label lblDateLicence;
    @FXML private Label lblTarif;

    private Taxi taxi;
    private static boolean isAdmin = false; // Stocke le statut admin

    public void setTaxi(Taxi taxi) {
        this.taxi = taxi;
        peuplerDetails();
    }
    public static void setIsAdminn(boolean adminStatus) {
        isAdmin = adminStatus;
    }

    private void peuplerDetails() {
        if (!isAdmin) {
            lblId.setVisible(false); // Masquer le label si l'utilisateur n'est pas admin
        } else {
            lblId.setText("ID: " + taxi.getIdTaxi()); // Afficher l'ID si l'utilisateur est admin
        }
        lblImmatriculation.setText("Immatriculation: " + taxi.getImmatriculation());
        lblMarque.setText("Marque: " + taxi.getMarque());
        lblModele.setText("Modèle: " + taxi.getModele());
        lblAnnee.setText("Année: " + taxi.getAnneeFabrication());
        lblCapacite.setText("Capacité: " + taxi.getCapacite());
        lblZone.setText("Zone: " + taxi.getZoneDesserte());
        lblStatut.setText("Statut: " + taxi.getStatut());
        lblLicence.setText("Licence: " + taxi.getLicenceNumero());

        // Formatage de la date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        lblDateLicence.setText("Date Licence: " +
                taxi.getLicenceDateObtention().format(formatter));

        lblTarif.setText("Tarif de base: " + String.format("%.2f TND", taxi.getTarifBase()));
    }

    @FXML
    private void fermerDetails() {
        ((Stage) lblId.getScene().getWindow()).close();
    }
}