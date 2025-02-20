package tn.esprit.jdbc.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import tn.esprit.jdbc.entities.Trajet;
import tn.esprit.jdbc.services.TrajetService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TrajetListController {

    @FXML
    private TableView<Trajet> trajetTableView;

    @FXML
    private TableColumn<Trajet, Integer> idColumn;
    @FXML
    private TableColumn<Trajet, String> departureColumn;
    @FXML
    private TableColumn<Trajet, String> destinationColumn;
    @FXML
    private TableColumn<Trajet, Double> distanceColumn;
    @FXML
    private TableColumn<Trajet, String> durationColumn;
    @FXML
    private TableColumn<Trajet, String> departureTimeColumn;
    @FXML
    private TableColumn<Trajet, String> arrivalTimeColumn;
    @FXML
    private TableColumn<Trajet, Double> priceColumn;
    @FXML
    private TableColumn<Trajet, Void> actionColumn;

    private TrajetService trajetService;

    public TrajetListController() {
        trajetService = new TrajetService();
    }

    @FXML
    public void initialize() throws SQLException {
        // Set the cell value factories for the TableView columns
        departureColumn.setCellValueFactory(new PropertyValueFactory<>("departure"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
        distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        departureTimeColumn.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        arrivalTimeColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Load data from the database into the TableView
        loadTrajetData();

        actionColumn.setCellFactory(new Callback<TableColumn<Trajet, Void>, TableCell<Trajet, Void>>() {
            @Override
            public TableCell<Trajet, Void> call(TableColumn<Trajet, Void> param) {
                return new TableCell<Trajet, Void>() {
                    private final Button editButton = new Button("Editer");
                    private final Button deleteButton = new Button("Supprimer");

                    private final HBox hBox = new HBox(10, editButton, deleteButton);

                    {
                        editButton.setStyle(
                                "-fx-background-color: #4CAF50; " +    // Green background
                                        "-fx-text-fill: white; " +               // White text
                                        "-fx-font-size: 14px; " +                // Font size
                                        "-fx-padding: 10px 20px; " +             // Padding
                                        "-fx-border-radius: 5px; " +            // Rounded borders
                                        "-fx-background-radius: 5px;"            // Rounded background
                        );

                        deleteButton.setStyle(
                                "-fx-background-color: #f44336; " +    // Red background
                                        "-fx-text-fill: white; " +               // White text
                                        "-fx-font-size: 14px; " +                // Font size
                                        "-fx-padding: 10px 20px; " +             // Padding
                                        "-fx-border-radius: 5px; " +            // Rounded borders
                                        "-fx-background-radius: 5px;"            // Rounded background
                        );
                        // Handle edit button action
                        editButton.setOnAction(event -> {
                            Trajet trajet = getTableView().getItems().get(getIndex());
                            editTrajet(trajet);
                        });

                        // Handle delete button action
                        deleteButton.setOnAction(event -> {
                            Trajet trajet = getTableView().getItems().get(getIndex());
                            deleteTrajet(trajet);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(hBox);
                        }
                    }
                };
            }
        });
    }

    private void loadTrajetData() throws SQLException {
        List<Trajet> trajetList = trajetService.showAll();
        ObservableList<Trajet> observableTrajetList = FXCollections.observableArrayList(trajetList);
        trajetTableView.setItems(observableTrajetList);
    }

    private void editTrajet(Trajet trajet) {
        try {
            // Load the FXML file for the edit form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Editinterface.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Modifier le Trajet");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));

            // Get the controller for the edit window and pass the selected Trajet to it
            EditTrajetController editController = loader.getController();
            editController.setTrajet(trajet);

            // Show the edit window and wait for it to be closed before continuing
            stage.showAndWait();

            // Reload the data after the edit
            loadTrajetData();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteTrajet(Trajet trajet) {
        System.out.println("Suppression du trajet: " + trajet);
        try {
            trajetService.delete(trajet.getId());
            System.out.println("Trajet supprimé avec succès.");
            loadTrajetData(); // Reload data after deletion
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la suppression du trajet.");
        }
    }
}
