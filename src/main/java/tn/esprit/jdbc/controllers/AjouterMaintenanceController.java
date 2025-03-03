package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import tn.esprit.jdbc.entities.Maintenance;
import tn.esprit.jdbc.entities.Vehicle;
import tn.esprit.jdbc.services.maintenanceService;
import tn.esprit.jdbc.services.vehicleService;

import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class AjouterMaintenanceController {

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

    public AjouterMaintenanceController() {
        this.maintenanceService = new maintenanceService();
        this.vehicleService = new vehicleService();
    }

    @FXML
    public void initialize() {
        loadVehicles();
        setupComboBoxDisplay();
    }

    /**
     * Loads vehicles from the database into the ComboBox.
     */
    private void loadVehicles() {
        try {
                List<Vehicle> vehicles = vehicleService.showAll();
            vehicleComboBox.getItems().addAll(vehicles);
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Erreur de chargement des véhicules: " + e.getMessage());
        }
    }

    /**
     * Sets up the ComboBox to display meaningful vehicle information.
     */
    private void setupComboBoxDisplay() {
        vehicleComboBox.setConverter(new StringConverter<Vehicle>() {
            @Override
            public String toString(Vehicle vehicle) {
                return (vehicle != null) ? vehicle.getModel() + " - " + vehicle.getLicensePlate() : "";
            }

            @Override
            public Vehicle fromString(String string) {
                return null; // Not used
            }
        });

        vehicleComboBox.setCellFactory(lv -> new ListCell<Vehicle>() {
            @Override
            protected void updateItem(Vehicle vehicle, boolean empty) {
                super.updateItem(vehicle, empty);
                setText((empty || vehicle == null) ? null : vehicle.getModel() + " - " + vehicle.getLicensePlate());
            }
        });
    }

    @FXML
    private void handleAddMaintenance() {
        try {
            Vehicle selectedVehicle = vehicleComboBox.getSelectionModel().getSelectedItem();
            String description = descriptionField.getText();
            LocalDate selectedDate = datePicker.getValue();
            double cost;

            // Validate input fields
            if (selectedVehicle == null || description.isEmpty() || selectedDate == null) {
                showAlert(AlertType.ERROR, "Veuillez remplir tous les champs correctement.");
                return;
            }

            try {
                cost = Double.parseDouble(costField.getText());
                if (cost <= 0) {
                    showAlert(AlertType.ERROR, "Le coût doit être un nombre positif.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Veuillez entrer un coût valide.");
                return;
            }

            // Convert LocalDate to SQL Date
            Date maintenanceDate = Date.valueOf(selectedDate);

            // Create Maintenance object and insert it into the database
            Maintenance newMaintenance = new Maintenance(selectedVehicle, description, maintenanceDate, cost);
            maintenanceService.insert(newMaintenance);

            showAlert(AlertType.INFORMATION, "Maintenance ajoutée avec succès!");

            // Clear input fields after successful addition
            clearFields();

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Erreur lors de l'ajout: " + e.getMessage());
        }
    }

    /**
     * Clears the form fields after successful addition.
     */
    private void clearFields() {
        descriptionField.clear();
        datePicker.setValue(null);
        costField.clear();
        vehicleComboBox.getSelectionModel().clearSelection();
    }

    /**
     * Displays an alert dialog.
     */
    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.show();
    }
}
