package tn.esprit.jdbc.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.jdbc.entities.Taxi;
import tn.esprit.jdbc.services.ServiceTaxi;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class AjoutTaxiController {

    // Composants FXML
    @FXML private ComboBox<String> comboStatut, comboMarque, comboModele, comboZone;
    @FXML private DatePicker datePickerLicence;
    @FXML private TextField txtImmatriculation, txtAnnee, txtCapacite, txtLicenceNumero, txtTarifBase;
    @FXML private Button btnAjouter;

    private final ServiceTaxi serviceTaxi = new ServiceTaxi();

    // Données prédéfinies
    private final List<String> marques = Arrays.asList(
            "Toyota", "BMW", "Peugeot", "Renault", "Ford",
            "Mercedes", "Audi", "Volkswagen", "Honda", "Nissan"
    );

    private final List<String> zones = Arrays.asList(
            "Tunis", "Sousse", "Sfax", "Nabeul", "Bizerte",
            "Gabès", "Ariana", "Ben Arous", "Manouba", "Mahdia"
    );

    // Modèles pour chaque marque
    private final List<String> modelesToyota = Arrays.asList("Corolla", "Yaris", "Land Cruiser", "Camry", "Hilux");
    private final List<String> modelesBMW = Arrays.asList("Series 3", "Series 5", "X5", "X3", "Z4");
    private final List<String> modelesPeugeot = Arrays.asList("208", "3008", "508", "Partner", "3008 GT");
    private final List<String> modelesRenault = Arrays.asList("Clio", "Megane", "Kadjar", "Twingo", "Captur");
    private final List<String> modelesFord = Arrays.asList("Focus", "Fiesta", "Mustang", "Kuga", "Explorer");
    private final List<String> modelesMercedes = Arrays.asList("C-Class", "E-Class", "S-Class", "GLA", "CLA");
    private final List<String> modelesAudi = Arrays.asList("A3", "A4", "Q5", "Q7", "A6");
    private final List<String> modelesVolkswagen = Arrays.asList("Golf", "Passat", "Tiguan", "Jetta", "Polo");
    private final List<String> modelesHonda = Arrays.asList("Civic", "Accord", "CR-V", "Jazz", "HR-V");
    private final List<String> modelesNissan = Arrays.asList("Micra", "Qashqai", "Juke", "X-Trail", "Leaf");

    // Taxi ajouté (pour être récupéré par le parent)
    private Taxi taxiAjouter;

    @FXML
    void initialize() {
        initialiserComposants();
        configurerDatePicker();
        configurerEcouteurs();
    }

    private void initialiserComposants() {
        // Initialisation des ComboBox
        comboStatut.getItems().addAll("Disponible", "En service", "En réservation");
        comboMarque.getItems().addAll(marques);
        comboZone.getItems().addAll(zones);

        // Sélections par défaut
        comboStatut.getSelectionModel().selectFirst();
        comboMarque.getSelectionModel().selectFirst();
        comboZone.getSelectionModel().selectFirst();

        // Mise à jour dynamique des modèles
        comboMarque.setOnAction(e -> mettreAJourModeles());
        mettreAJourModeles();
    }

    private void mettreAJourModeles() {
        String marqueSelectionnee = comboMarque.getValue();
        if (marqueSelectionnee == null) return;

        switch (marqueSelectionnee) {
            case "Toyota" -> comboModele.getItems().setAll(modelesToyota);
            case "BMW" -> comboModele.getItems().setAll(modelesBMW);
            case "Peugeot" -> comboModele.getItems().setAll(modelesPeugeot);
            case "Renault" -> comboModele.getItems().setAll(modelesRenault);
            case "Ford" -> comboModele.getItems().setAll(modelesFord);
            case "Mercedes" -> comboModele.getItems().setAll(modelesMercedes);
            case "Audi" -> comboModele.getItems().setAll(modelesAudi);
            case "Volkswagen" -> comboModele.getItems().setAll(modelesVolkswagen);
            case "Honda" -> comboModele.getItems().setAll(modelesHonda);
            case "Nissan" -> comboModele.getItems().setAll(modelesNissan);
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

    private void configurerEcouteurs() {
        // Écouteurs pour les champs texte
        List<TextField> champs = Arrays.asList(
                txtImmatriculation, txtAnnee, txtCapacite,
                txtLicenceNumero, txtTarifBase
        );
        champs.forEach(champ -> champ.textProperty().addListener(
                (observable, ancienneValeur, nouvelleValeur) -> mettreAJourBouton()
        ));

        // Écouteurs pour les ComboBox
        comboMarque.valueProperty().addListener((obs, oldVal, newVal) -> mettreAJourBouton());
        comboModele.valueProperty().addListener((obs, oldVal, newVal) -> mettreAJourBouton());
        comboZone.valueProperty().addListener((obs, oldVal, newVal) -> mettreAJourBouton());

        // Écouteur pour le DatePicker
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
                && datePickerLicence.getValue() != null;
        btnAjouter.setDisable(!champsValides);
    }

    @FXML
    void ajouterTaxi(ActionEvent event) {
        if (!validerFormulaire()) return;
        try {
            Taxi nouveauTaxi = creerTaxiDepuisFormulaire();
            taxiAjouter = nouveauTaxi;  // Affectation pour récupération ultérieure
            serviceTaxi.insert(nouveauTaxi);
            afficherAlerte("Succès", "Taxi ajouté avec succès !", Alert.AlertType.INFORMATION);
            reinitialiserFormulaire();
        } catch (SQLException e) {
            gererErreurSQL(e);
        } catch (Exception e) {
            afficherAlerte("Erreur", "Erreur inattendue : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private Taxi creerTaxiDepuisFormulaire() {
        return new Taxi(
                0,
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
    }

    private boolean validerFormulaire() {
        if (!txtImmatriculation.getText().matches("\\d{3}\\s*(تونس|TUN)\\s*\\d{3}")) {
            afficherAlerte("Format incorrect", "Format d'immatriculation invalide (ex: 123 تونس 456)", Alert.AlertType.WARNING);
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

    private void gererErreurSQL(SQLException e) {
        String message = "Erreur lors de l'ajout : ";
        if (e.getMessage().contains("licence")) {
            message += "Licence déjà existante !";
        } else if (e.getMessage().contains("immatriculation")) {
            message += "Immatriculation déjà existante !";
        } else {
            message += e.getMessage();
        }
        afficherAlerte("Erreur SQL", message, Alert.AlertType.ERROR);
    }

    private void afficherAlerte(String titre, String message, Alert.AlertType type) {
        Alert alerte = new Alert(type);
        alerte.setTitle(titre);
        alerte.setHeaderText(null);
        alerte.setContentText(message);
        alerte.showAndWait();
    }

    private void reinitialiserFormulaire() {
        txtImmatriculation.clear();
        txtAnnee.clear();
        txtCapacite.clear();
        txtLicenceNumero.clear();
        txtTarifBase.clear();
        comboMarque.getSelectionModel().selectFirst();
        comboModele.getSelectionModel().selectFirst();
        comboZone.getSelectionModel().selectFirst();
        comboStatut.getSelectionModel().selectFirst();
        datePickerLicence.setValue(null);
    }

    public Taxi getTaxiAjoute() {
        return taxiAjouter;
    }
}
