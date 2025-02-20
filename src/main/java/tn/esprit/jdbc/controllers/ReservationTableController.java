package tn.esprit.jdbc.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import tn.esprit.jdbc.entities.Reservation;
import tn.esprit.jdbc.services.ReservationService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ReservationTableController {

    @FXML
    private TableView<Reservation> reservationTable;

    @FXML
    private TableColumn<Reservation, Integer> idReservationColumn;

    @FXML
    private TableColumn<Reservation, String> trajetColumn;

    @FXML
    private TableColumn<Reservation, String> userColumn;

    @FXML
    private TableColumn<Reservation, String> reservationDateColumn;

    @FXML
    private TableColumn<Reservation, Integer> numberOfSeatsColumn;

    @FXML
    private TableColumn<Reservation, Double> totalPriceColumn;

    @FXML
    private TableColumn<Reservation, Void> editColumn;

    @FXML
    private TableColumn<Reservation, Void> deleteColumn;

    private ReservationService reservationService = new ReservationService();

    @FXML
    public void initialize() {
        // Load data into the table
        loadReservations();

        // Add edit and delete buttons
        addButtonToTable();
    }

    private void loadReservations() {
        try {
            List<Reservation> reservations = reservationService.showAll(); // Assume this method exists in your service
            reservationTable.getItems().setAll(reservations);
        } catch (SQLException e) {
            showErrorAlert("Error", "Failed to load reservations: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddReservation(ActionEvent event) {
        try {
            // Open the Add Reservation form (FXML form for adding a reservation)
            Stage stage = new Stage();
            VBox root = (VBox) FXMLLoader.load(getClass().getResource("/ReservationAjout.fxml"));
            Scene scene = new Scene(root);
            stage.setTitle("Add Reservation");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Error", "Failed to open the reservation form: " + e.getMessage());
        }
    }

    private void addButtonToTable() {
        // Add Edit Button
        editColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Reservation, Void> call(final TableColumn<Reservation, Void> param) {
                return new TableCell<>() {
                    private final Button editButton = new Button("Edit");

                    {
                        editButton.setOnAction(event -> {
                            Reservation reservation = getTableView().getItems().get(getIndex());
                            // Handle edit logic here
                            handleEditReservation(reservation);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(editButton);
                        }
                    }
                };
            }
        });

        // Add Delete Button
        deleteColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Reservation, Void> call(final TableColumn<Reservation, Void> param) {
                return new TableCell<>() {
                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.setOnAction(event -> {
                            Reservation reservation = getTableView().getItems().get(getIndex());
                            handleDeleteReservation(reservation);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }
                };
            }
        });
    }

    private void handleEditReservation(Reservation reservation) {
        try {
            // Load the FXML for EditReservation form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditReservation.fxml"));
            AnchorPane root = loader.load();

            // Get the EditReservationController instance
            EditReservationController controller = loader.getController();

            // Pass the selected reservation to the controller using setReservation
            controller.setReservation(reservation);

            // Set up the stage and scene for the Edit Reservation window
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Edit Reservation");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Error", "Failed to open the reservation form: " + e.getMessage());
        }
    }

    private void handleDeleteReservation(Reservation reservation) {
        try {
            reservationService.delete(reservation.getIdReservation()); // Assume this method exists
            loadReservations(); // Refresh the table after deletion
        } catch (SQLException e) {
            showErrorAlert("Error", "Failed to delete reservation: " + e.getMessage());
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
