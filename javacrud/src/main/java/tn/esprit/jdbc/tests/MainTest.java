package tn.esprit.jdbc.tests;

import tn.esprit.jdbc.entities.Avis;
import tn.esprit.jdbc.entities.Reponse;
import tn.esprit.jdbc.services.AvisService;
import tn.esprit.jdbc.services.ReponseService;
import tn.esprit.jdbc.utils.MyDatabase;
import java.util.Scanner;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class MainTest {

    public static void main(String[] args) {
        MyDatabase m1 = MyDatabase.getInstance();
        Scanner sc = new Scanner(System.in);
        AvisService avisService = new AvisService();
        ReponseService reponseService = new ReponseService();

        try {
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

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void displayAvisList(List<Avis> avisList) {
        for (Avis avis : avisList) {
            System.out.println(avis);
            System.out.println();
        }
    }

    private static void displayReponseList(List<Reponse> reponseList) {
        for (Reponse reponse : reponseList) {
            System.out.println(reponse);
            System.out.println();
        }
    }
}