package tn.esprit.jdbc.tests;
import tn.esprit.jdbc.entities.Reclamation;
import tn.esprit.jdbc.services.ReclamationService;
import tn.esprit.jdbc.services.UserService;
import tn.esprit.jdbc.entities.User;
import java.sql.SQLException;
import java.sql.Timestamp;


public class MainTest {
    public static void main(String[] args) {

        UserService userService = new UserService();

        try {
            // Test inserting a new user
            User newUser = new User("John", "john.doe@example.com", "123456789", "password123");
            int rowsInserted = userService.insert(newUser);
            System.out.println(rowsInserted + " user inserted successfully!");

            // Retrieve the auto-generated user_id
            int userId = newUser.getUserId();
            System.out.println("New user ID: " + userId);

            // Test retrieving all users
            System.out.println("All users:");
            userService.showAll().forEach(System.out::println);

            // Test updating a user
            User userToUpdate = new User(userId, "John Updated", "john.updated@example.com", "987654321", "newpassword");
            int rowsUpdated = userService.update(userToUpdate);
            System.out.println(rowsUpdated + " user updated successfully!");

            // Test retrieving the updated user
            System.out.println("Updated user:");
            userService.showAll().forEach(System.out::println);

            // Test deleting a user
            int rowsDeleted = userService.delete(userId);
            System.out.println(rowsDeleted + " user deleted successfully!");

            // Test retrieving all users after deletion
            System.out.println("All users after deletion:");
            userService.showAll().forEach(System.out::println);

        } catch (SQLException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
        /* // Create service instances
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
        }*/
    }
}