package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.jdbc.entities.Vehicle;
import tn.esprit.jdbc.entities.VehicleType;
import tn.esprit.jdbc.services.vehicleService;

import java.sql.SQLException;

public class EditVehicleController {

    @FXML private TextField txtModel;
    @FXML private TextField txtLicensePlate;
    @FXML private TextField txtType;
    @FXML private TextField txtCapacity;
    @FXML private Button btnSave;

    private vehicleService vehicleService = new vehicleService();
    private Vehicle vehicle;

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        txtModel.setText(vehicle.getModel());
        txtLicensePlate.setText(vehicle.getLicensePlate());
        txtType.setText(vehicle.getType().toString());
        txtCapacity.setText(String.valueOf(vehicle.getCapacity()));
    }

    @FXML
    private void handleSave() {
        if (vehicle != null) {
            vehicle.setModel(txtModel.getText());
            vehicle.setLicensePlate(txtLicensePlate.getText());
            vehicle.setType(VehicleType.valueOf(txtType.getText()));
            vehicle.setCapacity(Integer.parseInt(txtCapacity.getText()));

            try {
                vehicleService.update(vehicle);
                showAlert("Success", "Vehicle updated successfully!");
                closeWindow();
            } catch (SQLException e) {
                showAlert("Error", "Failed to update vehicle: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
