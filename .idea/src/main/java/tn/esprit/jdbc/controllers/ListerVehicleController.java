package tn.esprit.jdbc.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import tn.esprit.jdbc.entities.Vehicle;
import tn.esprit.jdbc.services.vehicleService;
import java.sql.SQLException;
import java.util.List;
import java.io.IOException;

public class ListerVehicleController {

    @FXML
    private TableView<Vehicle> vehicleTableView;
    @FXML
    private TableColumn<Vehicle, Integer> colId;
    @FXML
    private TableColumn<Vehicle, String> colModel;
    @FXML
    private TableColumn<Vehicle, String> colLicensePlate;
    @FXML
    private TableColumn<Vehicle, String> colType;
    @FXML
    private TableColumn<Vehicle, Integer> colCapacity;
    @FXML
    private TableColumn<Vehicle, Void> colEdit;
    @FXML
    private TableColumn<Vehicle, Void> colDelete;

    private final vehicleService vehicleService = new vehicleService();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadVehicles();
    }

    private void setupTableColumns() {
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colLicensePlate.setCellValueFactory(new PropertyValueFactory<>("licensePlate"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        // Add Edit Button to Each Row
        colEdit.setCellFactory(createButtonCellFactory("Edit"));

        // Add Delete Button to Each Row
        colDelete.setCellFactory(createButtonCellFactory("Delete"));
    }

    private void loadVehicles() {
        try {
            List<Vehicle> vehicles = vehicleService.showAll();
            ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList(vehicles);
            vehicleTableView.setItems(vehicleList);
        } catch (SQLException e) {
            System.err.println("Error loading vehicles: " + e.getMessage());
        }
    }

    private Callback<TableColumn<Vehicle, Void>, TableCell<Vehicle, Void>> createButtonCellFactory(String buttonType) {
        return new Callback<>() {
            @Override
            public TableCell<Vehicle, Void> call(final TableColumn<Vehicle, Void> param) {
                return new TableCell<>() {
                    private final Button button = new Button(buttonType);

                    {
                        button.setOnAction(event -> {
                            Vehicle vehicle = getTableView().getItems().get(getIndex());
                            if (buttonType.equals("Edit")) {
                                editVehicle(vehicle);
                            } else if (buttonType.equals("Delete")) {
                                deleteVehicle(vehicle);
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(button);
                        }
                    }
                };
            }
        };
    }

    private void openAddVehicleForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddVehicle.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Vehicle");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadVehicles(); // Refresh table after adding
        } catch (IOException e) {
            System.err.println("Error loading add form: " + e.getMessage());
        }
    }

    private void editVehicle(Vehicle vehicle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditVehicle.fxml"));
            Parent root = loader.load();

            EditVehicleController controller = loader.getController();
            controller.setVehicle(vehicle);

            Stage stage = new Stage();
            stage.setTitle("Edit Vehicle");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Refresh table after editing
            loadVehicles();
        } catch (IOException e) {
            System.err.println("Error loading edit form: " + e.getMessage());
        }
    }


    private void deleteVehicle(Vehicle vehicle) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Vehicle");
        alert.setHeaderText("Are you sure you want to delete this vehicle?");
        alert.setContentText("ID: " + vehicle.getVehicleId() + "\nModel: " + vehicle.getModel());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    vehicleService.delete(vehicle.getVehicleId());
                    vehicleTableView.getItems().remove(vehicle);
                    System.out.println("Deleted: " + vehicle);
                } catch (SQLException e) {
                    System.err.println("Error deleting vehicle: " + e.getMessage());
                }
            }
        });
    }
}
