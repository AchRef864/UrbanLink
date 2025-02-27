package tn.esprit.jdbc.tests;

import tn.esprit.jdbc.entities.Trajet;
import tn.esprit.jdbc.entities.TrajetType;
import tn.esprit.jdbc.services.TrajetService;
import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.Connection;
import java.sql.SQLException;

public class MainTest {
    public static void main(String[] args) {
        System.out.println("Testing database connection...");

        // Initialize database connection
        MyDatabase dbInstance = MyDatabase.getInstance();
        Connection connection = dbInstance.getCnx();

        // Check if the connection is open
        try {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Database connection successful!");
            } else {
                System.err.println("Failed to connect to the database or connection is closed.");
                return; // Stop execution if no database connection
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection status: " + e.getMessage());
            return;
        }

        // Create an instance of TrajetService
        TrajetService service = new TrajetService();

        // Define a test trajet for insertion
        Trajet testTrajet = new Trajet("ariana", "tunis", 15.0, "00:30", "12:30", "13:00", 12.00, TrajetType.PublicTransport);

        // Try inserting the test trajet
        try {
            // Ensure the connection is still open before inserting
            if (connection != null && !connection.isClosed()) {
                System.out.println("Connection is open. Proceeding with insert...");
            } else {
                System.err.println("Connection was closed before insert.");
                return;
            }

            // Try inserting the test trajet
            int result = service.insert(testTrajet); // Assuming you have an insert method in your TrajetService
            if (result > 0) {
                System.out.println("Trajet inserted successfully!");
            } else {
                System.err.println("Failed to insert trajet.");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error during insert: " + e.getMessage());
        } finally {
            try {
                // Close the connection explicitly when done
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    System.out.println("Connection closed.");
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
