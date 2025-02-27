package testes;

import entity.abonnement;
import services.abonnementservices;
import utiles.MyDataBase;
import java.sql.Date;
import java.sql.SQLException;

public class test {

    public static void main(String[] args) {
        // Établir la connexion à la base de données
        MyDataBase.getInstance();

        // Définition des dates
        Date date_debut = Date.valueOf("2024-05-19"); // Format YYYY-MM-DD
        Date date_fin = Date.valueOf("2025-05-20");   // Date de fin

        // Création de l'abonnement
        abonnement a1 = new abonnement("mensuel", 120, date_debut, date_fin, "Actif");

        abonnementservices abonnementService = new abonnementservices();

        try {
            // Insertion de l'abonnement
            int abonnementid = abonnementService.insert(a1); // Correction ici
            if (abonnementid != -1) {
                System.out.println("Abonnement ajouté avec succès. ID de l'abonnement : " + a1.getid());
            } else {
                System.out.println("Échec de l'ajout de l'abonnement.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}