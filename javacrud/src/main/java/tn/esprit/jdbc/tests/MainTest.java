package tn.esprit.jdbc.tests;

import tn.esprit.jdbc.entities.Avis;
import tn.esprit.jdbc.services.AvisService;
import tn.esprit.jdbc.utils.MyDatabase;
import java.util.Scanner;
import java.sql.SQLException;
import java.util.Date;

public class MainTest {

    public static void main(String[] args) {
        MyDatabase m1 = MyDatabase.getInstance();

        Avis a1 = new Avis(5, "Great service!", new Date(), 1);
        Avis a2 = new Avis(4, "Good experience", new Date(), 2);
        Scanner sc = new Scanner(System.in);
        AvisService avisService = new AvisService();

        try {
            //  avisService.insert(a1);
            //  avisService.insert(a2);
            //  avisService.delete(a1);
            System.out.println(avisService.showAll());
            System.out.println("Donner votre avis.");
            String commentaire = sc.nextLine();
            System.out.println("Donner la nouvelle note de l'avis.");
            int note = sc.nextInt();
            a1.setCommentaire(commentaire);
            a1.setNote(note);
            avisService.update(a1);

            System.out.println(avisService.showAll());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}