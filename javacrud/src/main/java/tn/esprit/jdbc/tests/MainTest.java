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
            System.out.println("Donner votre avis :");
            String commentaire = sc.nextLine();
            System.out.println("Donner la nouvelle note de l'avis.");
            int note = sc.nextInt();
            sc.nextLine();
            System.out.println("Donner l'ID de l'utilisateur.");
            int userId = sc.nextInt();
            sc.nextLine();

            Avis newAvis = new Avis(note, commentaire, new Date(), userId);
            avisService.insert(newAvis);

            System.out.println("Donner l'ID de l'avis a mettre a jour.");
            int avisIdToUpdate = sc.nextInt();
            sc.nextLine();
            System.out.println("Donner le nouveau commentaire.");
            String newCommentaire = sc.nextLine();
            System.out.println("Donner la nouvelle note.");
            int newNote = sc.nextInt();
            sc.nextLine();

            Avis avisToUpdate = new Avis(newNote, newCommentaire, new Date(), userId);
            avisToUpdate.setAvisId(avisIdToUpdate);
            avisService.update(avisToUpdate);

            System.out.println("All avis after update:");
            displayAvisList(avisService.showAll());

            // Delete an avis
            System.out.println("Donner l'ID de l'avis a supprimer.");
            int avisIdToDelete = sc.nextInt();
            sc.nextLine();

            Avis avisToDelete = new Avis();
            avisToDelete.setAvisId(avisIdToDelete);
            avisService.delete(avisToDelete);

            System.out.println("tous les avis apres suppresion:");
            displayAvisList(avisService.showAll());

            // Insert initial reponse
            System.out.println("Donner votre reponse.");
            String reponseCommentaire = sc.nextLine();
            System.out.println("Donner l'ID de l'avis.");
            int avisId = sc.nextInt();
            sc.nextLine();
            System.out.println("Donner l'ID de l'utilisateur.");
            int reponseUserId = sc.nextInt();
            sc.nextLine();

            Reponse newReponse = new Reponse(reponseCommentaire, new Date(), avisId, reponseUserId);
            reponseService.insert(newReponse);

            // Update a reponse
            System.out.println("Donner l'ID de la reponse a mettre a jour :");
            int reponseIdToUpdate = sc.nextInt();
            sc.nextLine();
            System.out.println("Donner le nouveau commentaire :");
            String newReponseCommentaire = sc.nextLine();
            System.out.println("Donner le nouvel ID de l'avis.");
            int newAvisId = sc.nextInt();
            sc.nextLine();

            // Retrieve the existing reponse to get the current userId
            Reponse existingReponse = reponseService.showAll().stream()
                    .filter(r -> r.getReponseId() == reponseIdToUpdate)
                    .findFirst()
                    .orElseThrow(() -> new SQLException("Reponse not found"));

            Reponse reponseToUpdate = new Reponse(newReponseCommentaire, new Date(), newAvisId, existingReponse.getUserId());
            reponseToUpdate.setReponseId(reponseIdToUpdate);
            reponseService.update(reponseToUpdate);

            // Delete a reponse
            System.out.println("Donner l'ID de la reponse a supprimer.");
            int reponseIdToDelete = sc.nextInt();
            sc.nextLine();

            Reponse reponseToDelete = new Reponse();
            reponseToDelete.setReponseId(reponseIdToDelete);
            reponseService.delete(reponseToDelete);

            System.out.println("tous les reponses apres la suppresion:");
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