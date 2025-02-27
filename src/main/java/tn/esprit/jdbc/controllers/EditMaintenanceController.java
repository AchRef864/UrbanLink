package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.jdbc.entities.Maintenance;
import tn.esprit.jdbc.entities.Vehicle;
import tn.esprit.jdbc.services.maintenanceService;
import tn.esprit.jdbc.services.vehicleService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.SQLException;
import java.util.List;
import java.util.Date;

public class EditMaintenanceController {

    @FXML
    private ComboBox<Vehicle> vehicleComboBox;

    @FXML
    private TextField descriptionField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField costField;

    private maintenanceService maintenanceService;
    private vehicleService vehicleService;

    private Maintenance selectedMaintenance;

    public EditMaintenanceController() {
        this.maintenanceService = new maintenanceService();
        this.vehicleService = new vehicleService();
    }

    @FXML
    public void initialize() {
        loadVehicles();
    }

    private void loadVehicles() {
        try {
            List<Vehicle> vehicles = vehicleService.showAll();
            vehicleComboBox.getItems().addAll(vehicles);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setMaintenanceData(Maintenance maintenance) {
        this.selectedMaintenance = maintenance;

        vehicleComboBox.getSelectionModel().select(maintenance.getVehicle());
        descriptionField.setText(maintenance.getDescription());

        // Convert and display date properly
        if (maintenance.getMaintenanceDate() != null) {
            Date utilDate = new Date(maintenance.getMaintenanceDate().getTime()); // Convert java.sql.Date to java.util.Date
            datePicker.setValue(utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            datePicker.setDisable(false);   // Enable editing
        }

        costField.setText(String.valueOf(maintenance.getCost()));
    }

    @FXML
    private void handleUpdateMaintenance() {
        try {
            if (selectedMaintenance == null) {
                showAlert(Alert.AlertType.ERROR, "Aucune maintenance sélectionnée!");
                return;
            }

            Vehicle selectedVehicle = vehicleComboBox.getSelectionModel().getSelectedItem();
            String description = descriptionField.getText();
            LocalDate selectedDate = datePicker.getValue();
            double cost = Double.parseDouble(costField.getText());

            if (selectedVehicle == null || description.isEmpty() || selectedDate == null || cost <= 0) {
                showAlert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs correctement.");
                return;
            }

            // Convert LocalDate to java.sql.Date for database storage
            Date maintenanceDate = java.sql.Date.valueOf(selectedDate);

            selectedMaintenance.setVehicle(selectedVehicle);
            selectedMaintenance.setDescription(description);
            selectedMaintenance.setMaintenanceDate(maintenanceDate);
            selectedMaintenance.setCost(cost);

            maintenanceService.update(selectedMaintenance);
            showAlert(Alert.AlertType.INFORMATION, "Maintenance mise à jour avec succès!");
        } catch (SQLException | NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.show();
    }
}
