package tn.esprit.jdbc.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

import javafx.util.Callback;
import tn.esprit.jdbc.entities.Trajet;
import tn.esprit.jdbc.services.TrajetService;

public class TrajetListController {

    @FXML
    private TableView<Trajet> trajetTableView;

    @FXML
    private TableColumn<Trajet, String> departureColumn;

    @FXML
    private TableColumn<Trajet, String> destinationColumn;

    @FXML
    private TableColumn<Trajet, Double> distanceColumn;

    @FXML
    private TableColumn<Trajet, Double> durationColumn;

    @FXML
    private TableColumn<Trajet, String> departureTimeColumn;

    @FXML
    private TableColumn<Trajet, String> arrivalTimeColumn;

    @FXML
    private TableColumn<Trajet, Double> priceColumn;

    @FXML
    private TableColumn<Trajet, String> actionColumn;

    @FXML
    private Button addButton;

    // Reference to the service class
    private TrajetService trajetService = new TrajetService();

    @FXML
    public void initialize() {
        // Initialize the columns
        departureColumn.setCellValueFactory(new PropertyValueFactory<>("departure"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
        distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        departureTimeColumn.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        arrivalTimeColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Fetch all trajets and populate the table
        loadTrajets();

        addEditButtonToTable();

        // Add event listeners to the buttons
        addButton.setOnMouseClicked(event -> handleAddTrajet());
    }

    private void loadTrajets() {
        // Fetch all trajets from the service
        List<Trajet> trajets = trajetService.getAllTrajets();

        // Convert duration to Double (if duration is a TIME field)
        for (Trajet trajet : trajets) {
            // Assuming `duration` is a Time object or String (e.g., HH:mm:ss), convert it to seconds
            if (trajet.getDuration() != null) {
                // If duration is stored as Time or String (e.g., "HH:mm:ss"), convert it to Double (seconds)
                trajet.setDuration(trajet.getDuration()); // Update the trajet with the calculated duration
            }
        }

        // Create an observable list from the fetched trajets
        ObservableList<Trajet> observableTrajets = FXCollections.observableArrayList(trajets);

        // Set the items for the TableView
        trajetTableView.setItems(observableTrajets);
    }

    private void addEditButtonToTable() {
        Callback<TableColumn<Trajet, String>, TableCell<Trajet, String>> cellFactory = (param) -> {
            final TableCell<Trajet, String> cell = new TableCell<>() {
                final Button editButton = new Button("Edit");
                final Button deleteButton = new Button("Delete");

                // Create HBox to hold both buttons
                final HBox hBox = new HBox(10, editButton, deleteButton);

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        // Edit button action
                        editButton.setOnAction(event -> {
                            Trajet trajet = getTableView().getItems().get(getIndex());
                            handleEditTrajet(trajet);
                        });

                        // Delete button action
                        deleteButton.setOnAction(event -> {
                            Trajet trajet = getTableView().getItems().get(getIndex());
                            handleDeleteTrajet(trajet);
                        });

                        setGraphic(hBox);  // Set the HBox containing both buttons
                        setText(null);
                    }
                }
            };

            return cell;
        };

        actionColumn.setCellFactory(cellFactory);
    }

    @FXML
    private void handleAddTrajet() {
        try {
            // Load the FXML file for adding a trajet
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaceAjoutTrajet.fxml"));
            Parent root = loader.load();

            // Create a new stage and display the loaded FXML file
            Stage stage = new Stage();
            stage.setTitle("Ajouter Trajet");
            stage.setScene(new Scene(root));
            stage.show();

            // Add an event handler to reload the table when the window is closed
            stage.setOnHidden(event -> loadTrajets());  // Reload the table when the window is closed

            System.out.println("Adding trajet for Covoiturage");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load interfaceAjoutTrajet.fxml for covoiturage.");
        }
    }

    private void handleEditTrajet(Trajet trajet) {
        try {
            // Load the FXML file for editing a trajet
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Editinterface.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the selected trajet to it
            EditTrajetController editTrajetController = loader.getController();
            editTrajetController.setTrajet(trajet); // Pass the selected trajet to the controller

            // Create a new stage for the edit window
            Stage stage = new Stage();
            stage.setTitle("Modifier Trajet");
            stage.setScene(new Scene(root));
            stage.show();

            // Reload the table when the edit window is closed
            stage.setOnHidden(event -> loadTrajets());  // Reload the table after editing

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement de l'interface d'édition.");
        }
    }

    // Handle the delete operation
    private void handleDeleteTrajet(Trajet trajet) {
        Alert confirmDeleteAlert = new Alert(AlertType.CONFIRMATION);
        confirmDeleteAlert.setTitle("Confirmer la suppression");
        confirmDeleteAlert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce trajet ?");
        confirmDeleteAlert.setContentText("Cette action est irréversible.");

        confirmDeleteAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    trajetService.delete(trajetService.getTrajetId(trajet.getDeparture(), trajet.getDestination(), trajet.getDistance(),
                            trajet.getDuration(), trajet.getDepartureTime(), trajet.getArrivalTime(), trajet.getPrice(),
                            trajet.getTrajetType())); // Call the delete service method
                    showAlert(AlertType.INFORMATION, "Supprimé", "Le trajet a été supprimé avec succès.");
                    loadTrajets(); // Reload the trajets after deletion

                } catch (Exception e) {
                    showAlert(AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la suppression du trajet.");
                }
            }
        });
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}