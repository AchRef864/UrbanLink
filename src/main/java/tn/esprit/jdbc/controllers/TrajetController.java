package tn.esprit.jdbc.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.jdbc.entities.Trajet;
import tn.esprit.jdbc.entities.TrajetType;
import tn.esprit.jdbc.services.TrajetService;

public class TrajetController {

    // Champs de texte associés à la vue (définis dans le fichier FXML)
    @FXML
    private TextField departureField;
    @FXML
    private TextField destinationField;
    @FXML
    private TextField distanceField;
    @FXML
    private TextField durationHourField;
    @FXML
    private TextField durationMinuteField;
    @FXML
    private TextField departureHourField;
    @FXML
    private TextField departureMinuteField;
    @FXML
    private TextField arrivalHourField;
    @FXML
    private TextField arrivalMinuteField;
    @FXML
    private TextField priceField;

    // ComboBox for selecting TrajetType
    @FXML
    private ComboBox<TrajetType> trajetTypeComboBox;

    // Service pour gérer les trajets
    private TrajetService trajetService;

    public TrajetController() {
        // Initialisation du service de gestion des trajets
        trajetService = new TrajetService();
    }

    @FXML
    public void initialize() {
        // Initialize the ComboBox with values from the TrajetType enum
        trajetTypeComboBox.setItems(FXCollections.observableArrayList(TrajetType.values()));
    }

    // Méthode appelée lorsqu'on clique sur le bouton "Ajouter Trajet"
    @FXML
    public void handleAddTrajet() {
        try {
            // Vérifier que les champs ne sont pas vides
            if (departureField.getText().isEmpty() || destinationField.getText().isEmpty() ||
                    distanceField.getText().isEmpty() || durationHourField.getText().isEmpty() ||
                    durationMinuteField.getText().isEmpty() || departureHourField.getText().isEmpty() ||
                    departureMinuteField.getText().isEmpty() || arrivalHourField.getText().isEmpty() ||
                    arrivalMinuteField.getText().isEmpty() || priceField.getText().isEmpty() ||
                    trajetTypeComboBox.getValue() == null) { // Check if TrajetType is selected
                System.out.println("Tous les champs doivent être remplis.");
                return;  // Sortir de la méthode si un champ est vide
            }

            // Récupérer les valeurs des champs de texte
            String departure = departureField.getText();
            String destination = destinationField.getText();
            double distance = Double.parseDouble(distanceField.getText());

            // Convertir l'heure et la minute en durée en heures (double)
            String durationInHours = formatTime(durationHourField.getText(), durationMinuteField.getText());

            // Récupérer les heures et minutes et les formater pour les heures de départ et d'arrivée
            String departureTime = formatTime(departureHourField.getText(), departureMinuteField.getText());
            String arrivalTime = formatTime(arrivalHourField.getText(), arrivalMinuteField.getText());

            double price = Double.parseDouble(priceField.getText());

            // Récupérer le type de trajet sélectionné
            TrajetType trajetType = trajetTypeComboBox.getValue();

            // Créer un objet Trajet et l'ajouter à la base de données
            Trajet trajet = new Trajet(departure, destination, distance, durationInHours, departureTime, arrivalTime, price, trajetType);
            trajetService.insert(trajet);

            Stage stage = (Stage) departureField.getScene().getWindow();
            // Show success pop-up
            showConfirmationDialog("Succès", "Le trajet a été ajouté avec succès!",stage);

        } catch (NumberFormatException e) {
            System.out.println("Erreur de format numérique: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'ajout du trajet.");
            showErrorDialog("Erreur", "Une erreur s'est produite lors de l'ajout du trajet.");
        }
    }

    // Method to show confirmation dialog
    // Method to show a success confirmation dialog
    private void showConfirmationDialog(String title, String message, Stage stage) {
        // Create the alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Wait for the user to click OK
        alert.showAndWait().ifPresent(response -> {
            // After the user clicks OK, close the Add Trajet window (Stage)
            stage.close();  // Close the Add Trajet window
        });
    }

    // Method to show error dialog
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Wait for the user to click OK
        alert.showAndWait().ifPresent(response -> {
            // Continue execution after the dialog is closed
        });
    }

    // Méthode pour formater l'heure (combinaison des champs heure et minute)
    private String formatTime(String hour, String minute) {
        return String.format("%02d:%02d", Integer.parseInt(hour), Integer.parseInt(minute));
    }
}
