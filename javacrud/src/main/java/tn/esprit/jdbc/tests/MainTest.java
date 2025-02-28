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
<<<<<<< HEAD
            System.out.println("Enter your review:");
            String commentaire = sc.nextLine();
            System.out.println("Enter the new rating for the review:");
            int note = sc.nextInt();
            sc.nextLine();
            System.out.println("Enter the user ID:");
            int userId = sc.nextInt();
            sc.nextLine();

            Avis newAvis = new Avis(note, commentaire, new Date(), userId);
            avisService.insert(newAvis);

            System.out.println("Enter the ID of the review to update:");
            int avisIdToUpdate = sc.nextInt();
            sc.nextLine();
            System.out.println("Enter the new comment:");
            String newCommentaire = sc.nextLine();
            System.out.println("Enter the new rating:");
            int newNote = sc.nextInt();
            sc.nextLine();

            Avis avisToUpdate = new Avis(newNote, newCommentaire, new Date(), userId);
            avisToUpdate.setAvis_id(avisIdToUpdate);
            avisService.update(avisToUpdate);

            System.out.println("All reviews after update:");
            displayAvisList(avisService.showAll());

            // Delete a review
            System.out.println("Enter the ID of the review to delete:");
            int avisIdToDelete = sc.nextInt();
            sc.nextLine();

            Avis avisToDelete = new Avis();
            avisToDelete.setAvis_id(avisIdToDelete);
            avisService.delete(avisToDelete);

            System.out.println("All reviews after deletion:");
            displayAvisList(avisService.showAll());

            // Insert initial response
            System.out.println("Enter your response:");
            String reponseCommentaire = sc.nextLine();
            System.out.println("Enter the review ID:");
            int avisId = sc.nextInt();
            sc.nextLine();
            System.out.println("Enter the user ID:");
            int reponseUserId = sc.nextInt();
            sc.nextLine();

            Reponse newReponse = new Reponse(reponseCommentaire, new Date(), avisId, reponseUserId);
            reponseService.insert(newReponse);

            // Update a response
            System.out.println("Enter the ID of the response to update:");
            int reponseIdToUpdate = sc.nextInt();
            sc.nextLine();
            System.out.println("Enter the new comment:");
            String newReponseCommentaire = sc.nextLine();
// Update a response
            System.out.println("Enter the ID of the response to update:");

            sc.nextLine();
            System.out.println("Enter a new response:");

// Retrieve the existing response to get the current userId
            Reponse existingReponse = reponseService.showAll().stream()
                    .filter(r -> r.getReponse_id() == reponseIdToUpdate)
                    .findFirst()
                    .orElseThrow(() -> new SQLException("Response not found"));

// Create a new Reponse object with the updated comment and existing details
            Reponse reponseToUpdate = new Reponse();
            reponseToUpdate.setReponse_id(reponseIdToUpdate);
            reponseToUpdate.setCommentaire(newReponseCommentaire);
            reponseToUpdate.setDate_reponse(existingReponse.getDate_reponse());
            reponseToUpdate.setAvis_id(existingReponse.getAvis_id());
            reponseToUpdate.setUser_id(existingReponse.getUser_id());

            reponseService.update(reponseToUpdate);

            // Delete a response
            System.out.println("Enter the ID of the response to delete:");
            int reponseIdToDelete = sc.nextInt();
            sc.nextLine();

            Reponse reponseToDelete = new Reponse();
            reponseToDelete.setReponse_id(reponseIdToDelete);
            reponseService.delete(reponseToDelete);

            System.out.println("All responses after deletion:");
            displayReponseList(reponseService.showAll());

=======
            int result = service.insert(testVehicle);
            if (result > 0) {
                System.out.println("Vehicle inserted successfully!");
            } else {
                System.err.println("Failed to insert vehicle.");
            }
>>>>>>> 0c71dfb834f6add887b2b67a7725ae848c89067d
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        }
    }
}
