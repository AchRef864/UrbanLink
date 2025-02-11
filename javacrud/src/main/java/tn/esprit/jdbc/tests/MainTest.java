package tn.esprit.jdbc.tests;

import tn.esprit.jdbc.entities.Avis;
import tn.esprit.jdbc.services.AvisService;
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

        try {
            // Insert initial avis
            // avisService.insert(a1);
            // avisService.insert(a2);

            System.out.println("Donner votre avis.");
            String commentaire = sc.nextLine();
            System.out.println("Donner la nouvelle note de l'avis.");
            int note = sc.nextInt();
            sc.nextLine();
            System.out.println("Donner l'ID de l'utilisateur.");
            int userId = sc.nextInt();
            sc.nextLine();

            Avis newAvis = new Avis(note, commentaire, new Date(), userId);
            avisService.insert(newAvis);

            System.out.println("All avis after adding new one:");
            displayAvisList(avisService.showAll());


            System.out.println("Donner l'ID de l'avis à mettre à jour.");
            int avisIdToUpdate = sc.nextInt();
            sc.nextLine();
            System.out.println("Donner le nouveau commentaire.");
            String newCommentaire = sc.nextLine();
            System.out.println("Donner la nouvelle note.");
            int newNote = sc.nextInt();
            sc.nextLine();
            System.out.println("Donner le nouvel ID de l'utilisateur.");
            int newUserId = sc.nextInt();
            sc.nextLine();

            Avis avisToUpdate = new Avis(newNote, newCommentaire, new Date(), newUserId);
            avisToUpdate.setAvisId(avisIdToUpdate);
            avisService.update(avisToUpdate);

            System.out.println("All avis after update:");
            displayAvisList(avisService.showAll());

            // Delete an avis
            System.out.println("Donner l'ID de l'avis à supprimer.");
            int avisIdToDelete = sc.nextInt();
            sc.nextLine();

            Avis avisToDelete = new Avis();
            avisToDelete.setAvisId(avisIdToDelete);
            avisService.delete(avisToDelete);

            System.out.println("All avis after deletion:");
            displayAvisList(avisService.showAll());

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
}