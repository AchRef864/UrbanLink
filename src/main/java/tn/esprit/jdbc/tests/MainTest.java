package tn.esprit.jdbc.tests;
import tn.esprit.jdbc.entities.Reclamation;
import tn.esprit.jdbc.services.ReclamationService;
import tn.esprit.jdbc.services.UserService;
import tn.esprit.jdbc.entities.User;
import java.sql.SQLException;
import java.sql.Timestamp;


public class MainTest {
    public static void main(String[] args) {
        // Create service instances
        UserService userService = new UserService();
        ReclamationService reclamationService = new ReclamationService();

        // Create a Reclamation object
        Reclamation r = new Reclamation(10, "Issue", "Issue crashes  .");

        try {
            // Insert the reclamation
            int rowsAffected = reclamationService.insert(r);

            if (rowsAffected > 0) {
                System.out.println("Reclamation added successfully!");
            } else {
                System.out.println("Failed to add reclamation.");
            }

            // Display all users after insertion (optional)
            System.out.println("All users in the database:");
            System.out.println(userService.showAll());
        } catch (SQLException e) {
            System.out.println("Error inserting reclamation: " + e.getMessage());
        }
    }
}