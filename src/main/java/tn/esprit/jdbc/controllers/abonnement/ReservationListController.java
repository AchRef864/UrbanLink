package tn.esprit.jdbc.controllers.abonnement;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import tn.esprit.jdbc.entities.reservation ;
import tn.esprit.jdbc.services.reservationservice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ReservationListController {

    @FXML
    private TableView<reservation> tableReservations;
    @FXML
    private TableColumn<reservation, Integer> colId;
    @FXML
    private TableColumn<reservation, Integer> colUserId;
    @FXML
    private TableColumn<reservation, String> colStatut;
    @FXML
    private TableColumn<reservation, String> colDateDebut;
    @FXML
    private TableColumn<reservation, String> colDateFin;
    @FXML
    private TableColumn<reservation, String> colAbonnement;
    @FXML
    private TableColumn<reservation, Void> colAction;

    private final reservationservice reservationService;

    public ReservationListController() {
        reservationService = new reservationservice();
    }

    @FXML
    public void initialize() {
        try {
            System.out.println("Chargement des réservations...");

            // Configuration des colonnes
            colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
            colUserId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getuserId()).asObject());
            colStatut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getstatut()));
            colDateDebut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getdateDebut().toString()));
            colDateFin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getdateFin().toString()));
            colAbonnement.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getabonnement() != null
                            ? cellData.getValue().getabonnement().gettype()
                            : "Aucun")
            );

            // Configuration de la colonne d'actions
            colAction.setCellFactory(new Callback<TableColumn<reservation, Void>, TableCell<reservation, Void>>() {
                @Override
                public TableCell<reservation, Void> call(TableColumn<reservation, Void> param) {
                    return new TableCell<reservation, Void>() {
                        private final Button editButton = new Button("Editer");
                        private final Button deleteButton = new Button("Supprimer");

                        private final HBox hBox = new HBox(10, editButton, deleteButton);

                        {
                            // Style pour le bouton "Editer"
                            editButton.setStyle(
                                    "-fx-background-color: #4CAF50; " +
                                            "-fx-text-fill: white; " +
                                            "-fx-font-size: 14px; " +
                                            "-fx-padding: 10px 20px; " +
                                            "-fx-border-radius: 5px; " +
                                            "-fx-background-radius: 5px;"
                            );

                            // Style pour le bouton "Supprimer"
                            deleteButton.setStyle(
                                    "-fx-background-color: #f44336; " +
                                            "-fx-text-fill: white; " +
                                            "-fx-font-size: 14px; " +
                                            "-fx-padding: 10px 20px; " +
                                            "-fx-border-radius: 5px; " +
                                            "-fx-background-radius: 5px;"
                            );

                            // Action pour le bouton "Editer"
                            editButton.setOnAction(event -> {
                                reservation selectedReservation = getTableView().getItems().get(getIndex());
                                modifierReservation(selectedReservation);
                            });

                            // Action pour le bouton "Supprimer"
                            deleteButton.setOnAction(event -> {
                                reservation selectedReservation = getTableView().getItems().get(getIndex());
                                supprimerReservation(selectedReservation);  // Passer l'objet reservation
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

            // Charger les réservations
            loadReservations();
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des réservations : " + e.getMessage());
        }
    }

    private void loadReservations() throws SQLException {
        List<reservation> reservations = reservationService.showAll();
        ObservableList<reservation> observableList = FXCollections.observableList(reservations);
        tableReservations.setItems(observableList);
    }

    private void modifierReservation(reservation reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierReservation.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            // Passer la réservation à la fenêtre de modification
            ReservationController controller = loader.getController();
            controller.setReservation(reservation);

            // Lorsque la fenêtre de modification est fermée (après modification), mettez à jour la TableView
            stage.setOnCloseRequest(event -> {
                try {
                    // Mettre à jour l'élément modifié dans la liste observable
                    ObservableList<reservation> items = tableReservations.getItems();
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).getId() == reservation.getId()) {
                            items.set(i, reservation); // Remplacer l'élément modifié
                            break;
                        }
                    }
                } catch (Exception e) {
                    showAlert("Erreur", "Erreur lors de la mise à jour de la réservation dans la vue : " + e.getMessage());
                }
            });

            stage.setTitle("Modifier Réservation");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture de la fenêtre de modification : " + e.getMessage());
        }
    }

    private void supprimerReservation(reservation selectedReservation) {
        if (selectedReservation == null) {
            showAlert("Erreur", "Aucune réservation sélectionnée.");
            return;
        }

        int id = selectedReservation.getId();  // Récupérer l'ID de la réservation

        if (id <= 0) {
            showAlert("Erreur", "ID de réservation invalide.");
            return;
        }

        try {
            int result = reservationService.delete(id);  // Appeler la méthode delete avec l'ID
            if (result > 0) {
                showAlert("Succès", "Réservation supprimée avec succès !");
                loadReservations(); // Recharger la TableView après suppression
            } else {
                showAlert("Erreur", "Aucune réservation trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la suppression de la réservation : " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
