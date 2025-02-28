package tn.esprit.jdbc.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import tn.esprit.jdbc.entities.Vehicle;
import tn.esprit.jdbc.entities.VehicleType;
import tn.esprit.jdbc.services.vehicleService;
import java.sql.*;

public class AjouterVehicleController {
    @FXML
    private TextField modelTextField;
    @FXML
    private TextField licenseTextField;
    @FXML
    private Spinner<Integer> capacitySpinner;
    @FXML
    private MenuButton typeMenuButton;
    @FXML
    private MenuItem covoiturageItem;
    @FXML
    private MenuItem transportItem;

    private String selectedType = "Select one"; // Default value

    @FXML
    public void initialize() {
        // Set min=1, max=100, default=1
        capacitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));

        // Set event handlers for menu items and store selected value
        covoiturageItem.setOnAction(event -> {
            typeMenuButton.setText("Carpooling");
            selectedType = "Carpooling";
        });

        transportItem.setOnAction(event -> {
            typeMenuButton.setText("Public transport");
            selectedType = "Public transport";
        });
    }

    @FXML
    void ajouterVehicleAction(ActionEvent event) {
        // Retrieve values
        String model = modelTextField.getText().trim();
        String licensePlate = licenseTextField.getText().trim();
        int capacity = capacitySpinner.getValue();
        String type = selectedType; // Get the selected type

        // Show values in an alert (for debugging)
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Vehicle Information");
        alert.setHeaderText("Here is the retrieved information:");
        alert.setContentText("Model: " + model + "\nLicense Plate: " + licensePlate +
                "\nCapacity: " + capacity + "\nType: " + type);
        alert.showAndWait();
        saveVehicleToDatabase(model, licensePlate, capacity, selectedType);
    }

    private void saveVehicleToDatabase(String model, String licensePlate, int capacity, String type) {
        try {
            // Convert string type to enum
            VehicleType vehicleType;
            if (type.equalsIgnoreCase("Public transport")) {
                vehicleType = VehicleType.BUS;
            } else if (type.equalsIgnoreCase("Carpooling")) {
                vehicleType = VehicleType.COVOITURAGE;
            } else {
                throw new IllegalArgumentException("Invalid vehicle type: " + type);
            }

            Vehicle vehicle = new Vehicle(model, licensePlate, vehicleType, capacity);
            vehicleService service = new vehicleService(); // Create an instance
            int result = service.insert(vehicle); // Call insert properly

            if (result > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Vehicle added successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add vehicle!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
