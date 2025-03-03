package tn.esprit.jdbc.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.jdbc.entities.Taxi;
import tn.esprit.jdbc.services.ServiceTaxi;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class ModifierTaxiController {

    @FXML private ComboBox<String> comboStatut, comboMarque, comboModele, comboZone;
    @FXML private DatePicker datePickerLicence;
    @FXML private TextField txtImmatriculation, txtAnnee, txtCapacite, txtLicenceNumero, txtTarifBase;
    @FXML private Button btnModifier;

    private final ServiceTaxi serviceTaxi = new ServiceTaxi();
    private Taxi taxiActuel;

    private final List<String> marques = Arrays.asList("Toyota", "BMW", "Peugeot", "Renault", "Ford", "Mercedes", "Audi", "Volkswagen", "Honda", "Nissan");
    private final List<String> zones = Arrays.asList("Tunis", "Sousse", "Sfax", "Nabeul", "Bizerte", "Gabès", "Ariana", "Ben Arous", "Manouba", "Mahdia");

    @FXML
    void initialize() {
        initialiserComposants();
        configurerDatePicker();
        configurerEcouteurs();
    }
    private Taxi taxiModifie;

    public Taxi getTaxiModifie() {
        return taxiModifie;
    }
    private void configurerEcouteurs() {
        List<TextField> champs = Arrays.asList(txtImmatriculation, txtAnnee, txtCapacite, txtLicenceNumero, txtTarifBase);
        champs.forEach(champ -> champ.textProperty().addListener((observable, oldVal, newVal) -> mettreAJourBouton()));
        comboMarque.valueProperty().addListener((obs, oldVal, newVal) -> mettreAJourBouton());
        comboModele.valueProperty().addListener((obs, oldVal, newVal) -> mettreAJourBouton());
        comboZone.valueProperty().addListener((obs, oldVal, newVal) -> mettreAJourBouton());
        comboStatut.valueProperty().addListener((obs, oldVal, newVal) -> mettreAJourBouton());
        datePickerLicence.valueProperty().addListener((obs, oldVal, newVal) -> mettreAJourBouton());
    }

    private void mettreAJourBouton() {
        boolean champsValides = !txtImmatriculation.getText().isEmpty()
                && !txtAnnee.getText().isEmpty()
                && !txtCapacite.getText().isEmpty()
                && !txtLicenceNumero.getText().isEmpty()
                && !txtTarifBase.getText().isEmpty()
                && comboMarque.getValue() != null
                && comboModele.getValue() != null
                && comboZone.getValue() != null
                && comboStatut.getValue() != null
                && datePickerLicence.getValue() != null;

        btnModifier.setDisable(!champsValides);
    }

    private void initialiserComposants() {
        comboStatut.getItems().addAll("Disponible", "En service", "En réservation");
        comboMarque.getItems().addAll(marques);
        comboZone.getItems().addAll(zones);
        comboMarque.setOnAction(e -> mettreAJourModeles());
    }

    private void mettreAJourModeles() {
        String marqueSelectionnee = comboMarque.getValue();
        if (marqueSelectionnee == null) return;
        switch (marqueSelectionnee) {
            case "Toyota" -> comboModele.getItems().setAll("Corolla", "Yaris", "Land Cruiser", "Camry", "Hilux");
            case "BMW" -> comboModele.getItems().setAll("Series 3", "Series 5", "X5", "X3", "Z4");
            case "Peugeot" -> comboModele.getItems().setAll("208", "308", "508", "2008", "3008");
            case "Renault" -> comboModele.getItems().setAll("Clio", "Megane", "Kadjar", "Captur", "Talisman");
            case "Ford" -> comboModele.getItems().setAll("Fiesta", "Focus", "Mondeo", "Kuga", "Ranger");
            case "Mercedes" -> comboModele.getItems().setAll("Class A", "Class C", "Class E", "Class S", "GLC");
            case "Audi" -> comboModele.getItems().setAll("A3", "A4", "A6", "Q3", "Q5");
            case "Volkswagen" -> comboModele.getItems().setAll("Golf", "Polo", "Passat", "Tiguan", "T-Roc");
            case "Honda" -> comboModele.getItems().setAll("Civic", "HR-V", "CR-V", "Jazz", "Accord");
            case "Nissan" -> comboModele.getItems().setAll("Micra", "Qashqai", "Juke", "X-Trail", "Leaf");
            default -> comboModele.getItems().clear();
        }
        comboModele.getSelectionModel().selectFirst();
    }

    private void configurerDatePicker() {
        datePickerLicence.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isAfter(LocalDate.now()));
            }
        });
    }

    public void setTaxi(Taxi taxi) {
        this.taxiActuel = taxi;
        if (taxi != null) {
            txtImmatriculation.setText(taxi.getImmatriculation());
            comboMarque.setValue(taxi.getMarque());
            mettreAJourModeles();
            comboModele.setValue(taxi.getModele());
            txtAnnee.setText(String.valueOf(taxi.getAnneeFabrication()));
            txtCapacite.setText(String.valueOf(taxi.getCapacite()));
            comboZone.setValue(taxi.getZoneDesserte());
            comboStatut.setValue(taxi.getStatut());
            txtLicenceNumero.setText(taxi.getLicenceNumero());
            datePickerLicence.setValue(taxi.getLicenceDateObtention());
            txtTarifBase.setText(String.valueOf(taxi.getTarifBase()));
        }
    }

    @FXML
    void modifierTaxi(ActionEvent event) {
        if (!validerFormulaire()) return;

        try {
            Taxi modifie = new Taxi(
                    taxiActuel.getIdTaxi(),
                    txtImmatriculation.getText().trim(),
                    comboMarque.getValue(),
                    comboModele.getValue(),
                    Integer.parseInt(txtAnnee.getText()),
                    Integer.parseInt(txtCapacite.getText()),
                    comboZone.getValue(),
                    comboStatut.getValue(),
                    txtLicenceNumero.getText().trim(),
                    datePickerLicence.getValue(),
                    Double.parseDouble(txtTarifBase.getText())
            );

            serviceTaxi.update(modifie);
            afficherAlerte("Succès", "Taxi modifié avec succès !", Alert.AlertType.INFORMATION);

            fermerFenetre();
        } catch (SQLException e) {
            afficherAlerte("Erreur SQL", "Problème de modification : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validerFormulaire() {
        if (!txtImmatriculation.getText().matches("\\d{3}\\s*(تونس|TUN)\\s*\\d{3}")) {
            afficherAlerte("Format incorrect", "Format d'immatriculation invalide", Alert.AlertType.WARNING);
            return false;
        }
        if (!txtLicenceNumero.getText().matches("\\d{5}")) {
            afficherAlerte("Format incorrect", "Le numéro de licence doit contenir 5 chiffres", Alert.AlertType.WARNING);
            return false;
        }
        try {
            int annee = Integer.parseInt(txtAnnee.getText());
            int anneeActuelle = LocalDate.now().getYear();
            if (annee < 2014 || annee > anneeActuelle) {
                afficherAlerte("Année invalide", "L'année doit être entre 2014 et " + anneeActuelle, Alert.AlertType.WARNING);
                return false;
            }
            int capacite = Integer.parseInt(txtCapacite.getText());
            if (capacite < 1 || capacite > 9) {
                afficherAlerte("Capacité invalide", "La capacité doit être entre 1 et 9", Alert.AlertType.WARNING);
                return false;
            }
            double tarif = Double.parseDouble(txtTarifBase.getText());
            if (tarif < 0.5) {
                afficherAlerte("Tarif invalide", "Le tarif doit être ≥ 0.500 TND", Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            afficherAlerte("Erreur de format", "Valeur numérique invalide", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    // Après la modification, mettez à jour taxiModifie avec les nouvelles données
    private void afficherAlerte(String titre, String message, Alert.AlertType type) {
        Alert alerte = new Alert(type);
        alerte.setTitle(titre);
        alerte.setHeaderText(null);
        alerte.setContentText(message);
        alerte.showAndWait();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) btnModifier.getScene().getWindow();
        stage.close();
    }
}
