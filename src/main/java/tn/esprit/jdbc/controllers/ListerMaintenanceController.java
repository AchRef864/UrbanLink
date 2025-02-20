package tn.esprit.jdbc.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tn.esprit.jdbc.entities.Maintenance;
import tn.esprit.jdbc.services.maintenanceService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListerMaintenanceController {

    @FXML
    private TableView<Maintenance> maintenanceTable;

    @FXML
    private TableColumn<Maintenance, Integer> colVehicle;

    @FXML
    private TableColumn<Maintenance, String> colDescription;

    @FXML
    private TableColumn<Maintenance, String> colDate;

    @FXML
    private TableColumn<Maintenance, Double> colCost;

    @FXML
    private TableColumn<Maintenance, Void> colActions;

    @FXML
    private Button btnAdd;

    private final maintenanceService maintenanceService = new maintenanceService();

    @FXML
    public void initialize() {
        setupTable();
        loadMaintenanceData();

        // Add event for Add button
        btnAdd.setOnAction(event -> addMaintenance());
    }

    private void setupTable() {
        colVehicle.setCellValueFactory(cellData -> {
            int vehicleId = cellData.getValue().getVehicle() != null ? cellData.getValue().getVehicle().getVehicleId() : 0;
            return new javafx.beans.property.SimpleIntegerProperty(vehicleId).asObject();
        });

        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("maintenanceDate"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        // Add Edit & Delete buttons in Actions column
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button("Edit");
            private final Button btnDelete = new Button("Delete");
            private final HBox buttonBox = new HBox(10, btnEdit, btnDelete);

            {
                btnEdit.setOnAction(event -> {
                    Maintenance maintenance = getTableView().getItems().get(getIndex());
                    editMaintenance(maintenance);
                });

                btnDelete.setOnAction(event -> {
                    Maintenance maintenance = getTableView().getItems().get(getIndex());
                    deleteMaintenance(maintenance);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonBox);
                }
            }
        });
    }

    private void loadMaintenanceData() {
        try {
            List<Maintenance> maintenanceList = maintenanceService.showAll();
            ObservableList<Maintenance> observableList = FXCollections.observableArrayList(maintenanceList);
            maintenanceTable.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void editMaintenance(Maintenance maintenance) {
        try {
            System.out.println("Editing Maintenance ID: " + maintenance.getMaintenanceId());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/EditMaintenance.fxml"));
            Parent root = loader.load();

            // Pass maintenance data to the edit controller
            EditMaintenanceController controller = loader.getController();
            controller.setMaintenanceData(maintenance);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Maintenance");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteMaintenance(Maintenance maintenance) {
        try {
            System.out.println("Deleting Maintenance ID: " + maintenance.getMaintenanceId());
            maintenanceService.delete(maintenance.getMaintenanceId());
            loadMaintenanceData(); // Refresh table
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addMaintenance() {
        try {
            System.out.println("Adding new Maintenance...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterMaintenance.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Maintenance");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
