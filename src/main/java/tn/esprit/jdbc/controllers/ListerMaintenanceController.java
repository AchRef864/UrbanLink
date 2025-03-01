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
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

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

    @FXML
    private Button btnSearch;

    @FXML
    private TextField searchField;

    private final maintenanceService maintenanceService = new maintenanceService();
    private ObservableList<Maintenance> allData;

    @FXML
    public void initialize() {
        setupTable();
        loadMaintenanceData();

        // Event for Add button
        btnAdd.setOnAction(event -> addMaintenance());

        // Event for Search button
        btnSearch.setOnAction(event -> searchMaintenance());
    }

    private void setupTable() {
        colVehicle.setCellValueFactory(cellData -> {
            int vehicleId = cellData.getValue().getVehicle() != null ? cellData.getValue().getVehicle().getVehicleId() : 0;
            return new javafx.beans.property.SimpleIntegerProperty(vehicleId).asObject();
        });

        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Format Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        colDate.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                dateFormat.format(cellData.getValue().getMaintenanceDate())));

        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        // Actions Column (Edit & Delete)
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
                setGraphic(empty ? null : buttonBox);
            }
        });
    }

    private void loadMaintenanceData() {
        try {
            List<Maintenance> maintenanceList = maintenanceService.showAll();
            allData = FXCollections.observableArrayList(maintenanceList);
            maintenanceTable.setItems(allData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchMaintenance() {
        String keyword = searchField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            maintenanceTable.setItems(allData); // Restore full list
        } else {
            List<Maintenance> filteredList = allData.stream()
                    .filter(m -> m.getDescription().toLowerCase().contains(keyword) ||
                            String.valueOf(m.getMaintenanceDate()).contains(keyword) ||
                            String.valueOf(m.getCost()).contains(keyword))
                    .collect(Collectors.toList());

            maintenanceTable.setItems(FXCollections.observableArrayList(filteredList));
        }

        // Reapply cell factory to ensure buttons are shown
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
                setGraphic(empty ? null : buttonBox);
            }
        });
    }

    private void editMaintenance(Maintenance maintenance) {
        try {
            System.out.println("Editing Maintenance ID: " + maintenance.getMaintenanceId());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditMaintenance.fxml"));
            Parent root = loader.load();

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
