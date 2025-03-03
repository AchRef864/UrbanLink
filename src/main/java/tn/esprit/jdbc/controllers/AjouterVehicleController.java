package tn.esprit.jdbc.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private Label licenseHintLabel; // Label for showing format hints
    @FXML
    private Spinner<Integer> capacitySpinner;
    @FXML
    private MenuButton typeMenuButton;

    @FXML
    private MenuItem busItem;
    @FXML
    private MenuItem carItem;
    @FXML
    private MenuItem truckItem;
    @FXML
    private MenuItem motorcycleItem;

    private String selectedType = "Select one"; // Default value
    private String licenseRegex = ""; // Stores the current regex for validation

    @FXML
    public void initialize() {
        // Set min=1, max=100, default=1
        capacitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));

        // Set event handlers for menu items and store selected value
        busItem.setOnAction(event -> setVehicleType("Bus", "\\d{2}-\\d{3}-\\d{2}", "Format: 12-345-67"));
        carItem.setOnAction(event -> setVehicleType("Car", "\\d{3}-\\d{4}", "Format: 123-4567"));
        truckItem.setOnAction(event -> setVehicleType("Truck", "\\d{4}-[A-Z]{2}", "Format: 1234-AB"));
        motorcycleItem.setOnAction(event -> setVehicleType("Motorcycle", "[A-Z]{2}-\\d{3}-[A-Z]", "Format: AB-123-C"));
    }

    private void setVehicleType(String type, String regex, String formatHint) {
        typeMenuButton.setText(type);
        selectedType = type;
        licenseRegex = regex;
        licenseTextField.setVisible(true);
        licenseHintLabel.setText(formatHint);
        licenseHintLabel.setVisible(true);
    }

    @FXML
    void ajouterVehicleAction(ActionEvent event) {
        // Retrieve values
        String model = modelTextField.getText().trim();
        String licensePlate = licenseTextField.getText().trim();
        int capacity = capacitySpinner.getValue();
        String type = selectedType; // Get the selected type

        // Validate license plate format
        if (!licensePlate.matches(licenseRegex)) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "License plate format must be: " + licenseHintLabel.getText());
            return;
        }

        // Convert string type to enum and validate capacity
        VehicleType vehicleType;
        int minCapacity, maxCapacity;

        switch (type) {
            case "Bus":
                vehicleType = VehicleType.Bus;
                minCapacity = 10;
                maxCapacity = 50;
                break;
            case "Car":
                vehicleType = VehicleType.Car;
                minCapacity = 1;
                maxCapacity = 7;
                break;
            case "Truck":
                vehicleType = VehicleType.Truck;
                minCapacity = 2;
                maxCapacity = 10;
                break;
            case "Motorcycle":
                vehicleType = VehicleType.Motorcycle;
                minCapacity = 1;
                maxCapacity = 2;
                break;
            default:
                showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid vehicle type selected!");
                return;
        }

        // Validate capacity
        if (capacity < minCapacity || capacity > maxCapacity) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Capacity must be between " + minCapacity + " and " + maxCapacity + " for " + type);
            return;
        }

        // Save to database if all validations pass
        saveVehicleToDatabase(model, licensePlate, capacity, type);
    }

    private void saveVehicleToDatabase(String model, String licensePlate, int capacity, String type) {
        try {
            // Convert string type to enum
            VehicleType vehicleType;
            switch (type) {
                case "Bus":
                    vehicleType = VehicleType.Bus;
                    break;
                case "Car":
                    vehicleType = VehicleType.Car;
                    break;
                case "Truck":
                    vehicleType = VehicleType.Truck;
                    break;
                case "Motorcycle":
                    vehicleType = VehicleType.Motorcycle;
                    break;
                default:
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