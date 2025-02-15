package org.example.urbanlink.test;

import org.example.urbanlink.entities.Maintenance;
import org.example.urbanlink.entities.Vehicle;
import org.example.urbanlink.entities.VehicleType;
import org.example.urbanlink.services.maintenanceService;
import org.example.urbanlink.services.vehicleService;
import org.example.urbanlink.utils.MyDatabase;

import java.sql.SQLException;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        System.out.println("Testing database connection...");

        // Initialize database connection
        MyDatabase dbInstance = MyDatabase.getInstance();

        if (dbInstance.getConnection() != null) {
            System.out.println("Database connection successful!");
        } else {
            System.err.println("Failed to connect to the database.");
            return; // Stop execution if no database connection
        }

        // Create VehicleService instance
       /* vehicleService vehicleService = new vehicleService();
        Vehicle vehicle = new Vehicle("Toyota", "ABC-333", VehicleType.COVOITURAGE, 4);

        try {
            vehicle = vehicleService.getByLicensePlate("ABC-333");
            System.out.println(vehicle.getVehicleId());
            vehicle.setModel("Ford");
            vehicleService.update(vehicle);
            System.out.println("Vehicle inserted successfully" );
        } catch (SQLException e) {
            System.err.println("Error inserting vehicle: " + e.getMessage());
        }

        // Insert Maintenance Record
        maintenanceService maintenanceService = new maintenanceService();
        Maintenance maintenance = new Maintenance(vehicle, "Change tire", new Date(), 75.50);

        try {
            System.out.println(maintenanceService.getMaintenanceByVehicleId(vehicle.getVehicleId()).getFirst().getDescription());
            System.out.println("Maintenance record inserted successfully!");
        } catch (SQLException e) {
            System.err.println("Error inserting maintenance record: " + e.getMessage());
        }*/
    }
}
