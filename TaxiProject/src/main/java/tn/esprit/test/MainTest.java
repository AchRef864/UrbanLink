package tn.esprit.test;

import tn.esprit.models.*;
import tn.esprit.services.*;
import tn.esprit.utils.MyDataBase;


import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MainTest {
    public static void main(String[] args) {
        MyDataBase db = MyDataBase.getInstance();
        ServiceTaxi serviceTaxi = new ServiceTaxi();
        ServiceUser serviceUser = new ServiceUser(); // À créer
        ServiceContrat serviceContrat = new ServiceContrat();
        ServiceCourse serviceCourse = new ServiceCourse();

        try {
            // ================== PHASE 1: CRÉATION DES DONNÉES DE BASE ==================
            System.out.println("=== CRÉATION TAXI ET USER ===");

            // 1. Création d'un taxi
            Taxi taxi = new Taxi(
                    "AB-987-CD",
                    "Renault",
                    "Clio",
                    2021,
                    4,
                    "Tunis",
                    "Disponible",
                    "LIC-123ABC",
                    LocalDate.of(2020, 5, 10),
                    12.5
            );
            serviceTaxi.ajouter(taxi);
            System.out.println("✅ Taxi créé - ID: " + taxi.getIdTaxi());

            // 2. Création d'un utilisateur
            User user = new User(
                    "Mohamed Ali",
                    "mohamed.ali@example.com",
                    "+216 12 345 678",
                    "securePass123"
            );
            serviceUser.ajouter(user); // Implémentez ServiceUser
            System.out.println("✅ User créé - ID: " + user.getuser_id());

            // ================== PHASE 2: CRÉATION CONTRAT ==================
            System.out.println("\n=== TEST CONTRAT ===");

            Contrat contrat = new Contrat(
                    taxi.getIdTaxi(), // ID du taxi créé
                    LocalDate.now(),
                    LocalDate.now().plusMonths(6),
                    1800.0,
                    "Active"
            );

            serviceContrat.ajouter(contrat);
            System.out.println("✅ Contrat créé - ID: " + contrat.getId_contrat());

            // ================== PHASE 3: CRÉATION COURSE ==================
            System.out.println("\n=== TEST COURSE ===");

            Course course = new Course(
                    user.getuser_id(), // ID du user créé
                    taxi.getIdTaxi(), // ID du taxi créé
                    LocalDateTime.now().plusHours(2),
                    "Tunis",
                    "Sousse",
                    140.5,
                    85.0,
                    "En attente"
            );

            serviceCourse.ajouter(course);
            System.out.println("✅ Course créée - ID: " + course.getId_course());

            // ================== AFFICHAGE ==================
            System.out.println("\n=== DONNÉES FINALES ===");
            System.out.println("Taxis: " + serviceTaxi.afficher().size());
            System.out.println("Contrats: " + serviceContrat.afficher().size());
            System.out.println("Courses: " + serviceCourse.afficher().size());

        } catch (SQLException e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.closeConnection();
            System.out.println("\n🔌 Connexion fermée");
        }
    }
}