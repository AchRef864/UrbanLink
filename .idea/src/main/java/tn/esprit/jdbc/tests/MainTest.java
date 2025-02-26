package tn.esprit.jdbc.tests;

import tn.esprit.jdbc.entities.Vehicle;
import tn.esprit.jdbc.entities.VehicleType;
import tn.esprit.jdbc.services.vehicleService;
import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.SQLException;

public class MainTest {
    public static void main(String[] args) {
        System.out.println("Testing database connection...");

        // Initialize database connection
        MyDatabase dbInstance = MyDatabase.getInstance();

        if (dbInstance.getCnx() != null) {
            System.out.println("Database connection successful!");
        } else {
            System.err.println("Failed to connect to the database.");
            return; // Stop execution if no database connection
        }

        // Create an instance of vehicleService
        vehicleService service = new vehicleService();

        // Define a test vehicle
        Vehicle testVehicle = new Vehicle("Toyota Corolla", "123-TUN-456", VehicleType.BUS, 50);

        // Insert the test vehicle
        try {
            int result = service.insert(testVehicle);
            if (result > 0) {
                System.out.println("Vehicle inserted successfully!");
            } else {
                System.err.println("Failed to insert vehicle.");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        }
    }
}
