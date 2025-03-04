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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import tn.esprit.jdbc.entities.User;
import tn.esprit.jdbc.entities.reservation;
import tn.esprit.jdbc.services.reservationservice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

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
    @FXML
    private ComboBox<String> statutFilterCombo;
    @FXML
    private TextField userIdFilterField;

    private final reservationservice reservationService;
    private User loggedInUser;
    private List<reservation> allReservations; // Cache des réservations complètes

    public ReservationListController() {
        reservationService = new reservationservice();
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    @FXML
    public void initialize() {
        try {
            System.out.println("Chargement des réservations...");

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

            colAction.setCellFactory(new Callback<TableColumn<reservation, Void>, TableCell<reservation, Void>>() {
                @Override
                public TableCell<reservation, Void> call(TableColumn<reservation, Void> param) {
                    return new TableCell<reservation, Void>() {
                        private final Button editButton = new Button("Editer");
                        private final Button deleteButton = new Button("Supprimer");
                        private final HBox hBox = new HBox(10, editButton, deleteButton);

                        {
                            editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px;");
                            deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px;");

                            editButton.setOnAction(event -> modifierReservation(getTableView().getItems().get(getIndex())));
                            deleteButton.setOnAction(event -> supprimerReservation(getTableView().getItems().get(getIndex())));
                        }

                        @Override
                        protected void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            setGraphic(empty ? null : hBox);
                        }
                    };
                }
            });

            // Initialiser le filtre de statut
            statutFilterCombo.getItems().addAll("Tous les statuts", "en attente", "Annulée", "Confirmer");
            statutFilterCombo.setValue("Tous les statuts");

            loadReservations();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des réservations : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadReservations() throws SQLException {
        allReservations = reservationService.showAll();
        tableReservations.setItems(FXCollections.observableArrayList(allReservations));
    }

    @FXML
    private void filterReservations() {
        String statutFilter = statutFilterCombo.getValue();
        String userIdFilter = userIdFilterField.getText().trim();

        List<reservation> filteredList = allReservations;

        if (!"Tous les statuts".equals(statutFilter)) {
            filteredList = filteredList.stream()
                    .filter(r -> r.getstatut().equals(statutFilter))
                    .collect(Collectors.toList());
        }

        if (!userIdFilter.isEmpty()) {
            try {
                int userId = Integer.parseInt(userIdFilter);
                filteredList = filteredList.stream()
                        .filter(r -> r.getuserId() == userId)
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                showAlert("Erreur", "L'ID utilisateur doit être un nombre valide.");
                return;
            }
        }

        tableReservations.setItems(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    private void refreshReservations() {
        try {
            loadReservations();
            statutFilterCombo.setValue("Tous les statuts");
            userIdFilterField.clear();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du rafraîchissement des réservations : " + e.getMessage());
        }
    }

    private void modifierReservation(reservation reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierReservation.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            ReservationController controller = loader.getController();
            controller.setReservation(reservation);
            stage.setTitle("Modifier Réservation");
            stage.setOnCloseRequest(event -> {
                try {
                    loadReservations();
                } catch (SQLException e) {
                    showAlert("Erreur", "Erreur lors du rafraîchissement après modification.");
                }
            });
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
        int id = selectedReservation.getId();
        if (id <= 0) {
            showAlert("Erreur", "ID de réservation invalide.");
            return;
        }
        try {
            int result = reservationService.delete(id);
            if (result > 0) {
                showAlert("Succès", "Réservation supprimée avec succès !");
                loadReservations();
            } else {
                showAlert("Erreur", "Aucune réservation trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la suppression de la réservation : " + e.getMessage());
        }
    }

    @FXML
    private void handleAddReservation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutReservation.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Ajouter une Réservation");
            stage.initModality(Modality.APPLICATION_MODAL);

            ControllerReservationAjout controller = loader.getController();
            controller.setLoggedInUser(loggedInUser);

            stage.setOnCloseRequest(event -> {
                try {
                    loadReservations();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture de la fenêtre d'ajout : " + e.getMessage());
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