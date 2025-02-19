package tn.esprit.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import tn.esprit.services.ServiceUser;

public class Home extends Application {

    @FXML
    private Button ajouterTaxiBtn;

    @FXML
    private Button afficherListeTaxisBtn; // Bouton pour afficher la liste des taxis

    @FXML
    private TextField txtAdminId; // Champ pour saisir l'ID utilisateur

    // Instanciation du service pour vérifier si l'utilisateur est admin
    private final ServiceUser userService = new ServiceUser();

    private static boolean isAdmin = false;
    private static boolean isUser = false;

    @FXML
    void handleAjouterTaxi(ActionEvent event) {
        verifierAdminEtAction(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutTaxi.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Ajout Taxi");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    @FXML
    void handleAfficherListeCourses() {
        verifierAdminoruserEtAction(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeCourses.fxml"));
                Parent root = loader.load();
                // Passer le statut admin au contrôleur de la liste
                ListeCoursesController.setIsAdmin(isAdmin);
                ListeCoursesController.setIsUser(isUser);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Liste des Courses");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void handleAfficherListeTaxi() {
        verifierAcceEtAction(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeTaxis.fxml"));
                Parent root = loader.load();
                // Passer le statut admin au contrôleur de la liste
                ListeTaxisController.setIsAdmin(isAdmin);
                TaxiDetailsController.setIsAdminn(isAdmin);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Liste des Taxis");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

        /**
         * Vérifie si l'utilisateur est admin avant d'exécuter l'action
         */
    private void verifierAcceEtAction(Runnable action) {
        String userIdString = txtAdminId.getText().trim();

        try {
            if (userIdString.isEmpty()) {
                // Si l'ID est vide, utiliser la méthode isAdminWithoutId() pour vérifier l'admin
                afficherAlerte("Erreur", "utilisateur n'existe pas !");
            } else {
                // Si un ID est fourni, vérifier l'admin par ID
                int userId = Integer.parseInt(userIdString);
                isAdmin = userService.isAdmin(userId);
                isUser = userService.isUsersengeneral(userId);
            }

            if (isAdmin || isUser) {
                // Si l'utilisateur est admin, exécuter l'action
                action.run();
            } else {
                afficherAlerte("Accès refusé", "Vous devez être un administrateur ou utilisateur pour accéder à cette page.");
            }

        } catch (NumberFormatException e) {
            afficherAlerte("Erreur", "Veuillez saisir un ID valide (numérique) !");
        }
    }
    public void verifierAdminEtAction(Runnable action) {
        String userIdString = txtAdminId.getText().trim();

        try {
            if (userIdString.isEmpty()) {
                // Si l'ID est vide, utiliser la méthode isAdminWithoutId() pour vérifier l'admin
                afficherAlerte("Erreur", "Admin n'existe pas !");
            } else {
                // Si un ID est fourni, vérifier l'admin par ID
                int userId = Integer.parseInt(userIdString);
                isAdmin = userService.isAdmin(userId);
            }

            if (isAdmin) {
                // Si l'utilisateur est admin, exécuter l'action
                action.run();
            } else {
                afficherAlerte("Accès refusé", "Vous devez être un administrateur pour accéder à cette page.");
            }

        } catch (NumberFormatException e) {
            afficherAlerte("Erreur", "Veuillez saisir un ID valide (numérique) !");
        }
    }
    public void verifierAdminoruserEtAction(Runnable action) {
        String userIdString = txtAdminId.getText().trim();

        try {
            if (userIdString.isEmpty()) {
                // Si l'ID est vide, utiliser la méthode isAdminWithoutId() pour vérifier l'admin
                afficherAlerte("Erreur", "admin or user n'existe pas !");
            } else {
                // Si un ID est fourni, vérifier l'admin par ID
                int userId = Integer.parseInt(userIdString);
                isAdmin = userService.isAdmin(userId);
                isUser = userService.isUsersengeneral(userId);
            }

            if (isAdmin || isUser) {
                // Si l'utilisateur est admin, exécuter l'action
                action.run();
            } else {
                afficherAlerte("Accès refusé", "Vous devez être un administrateur ou utilisateur pour accéder à cette page.");
            }

        } catch (NumberFormatException e) {
            afficherAlerte("Erreur", "Veuillez saisir un ID valide (numérique) !");
        }
    }

    /**
     * Affiche une alerte
     */
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    //verifier si c'est un utilisateur qui existe dans la base de données:
    //si oui il peut accéder à la page ListeTaxis sinon il ne peut pas accéder à cette page


    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Taxi Management - Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
