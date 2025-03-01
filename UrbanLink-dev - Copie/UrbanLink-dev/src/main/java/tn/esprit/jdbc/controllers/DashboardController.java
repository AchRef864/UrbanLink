package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import tn.esprit.jdbc.entities.User;

public class DashboardController {

    @FXML
    private StackPane contentArea;

    // Ajout du bouton ModifyCourse dans la déclaration
    @FXML
    private Button btnHome, btnVehicle, btnMaintenance, btnInsertUser,
            btnEditUser, btnLogout, btnViewRatings;

    @FXML
    public void initialize() {
        // Configuration des actions pour TOUS les boutons
        btnHome.setOnAction(e -> loadPage("/Home.fxml"));
        btnVehicle.setOnAction(e -> loadPage("/ListerVehicle.fxml"));
        btnMaintenance.setOnAction(e -> loadPage("/ListerMaintenance.fxml"));
        btnInsertUser.setOnAction(e -> loadPage("/AjouterUser.fxml"));
        btnEditUser.setOnAction(e -> loadPage("/EditUser.fxml"));
        btnViewRatings.setOnAction(e -> loadPage("/ViewRatings.fxml"));
        btnLogout.setOnAction(e -> logout());
    }

    private void loadPage(String fxml) {
        try {
            System.out.println("Chargement de : " + fxml);
            Parent page = FXMLLoader.load(getClass().getResource(fxml));
            contentArea.getChildren().setAll(page);
        } catch (IOException e) {
            System.err.println("Échec du chargement de : " + fxml);
            e.printStackTrace();
            // Ajouter une alerte d'erreur si besoin
        }
    }

    public void setUser(User user) {
        // Logique utilisateur si nécessaire
    }
    @FXML
    private void OpenTaxiListPageAction(ActionEvent event) {
        try {
            // Load the Taxi List page
            Parent root = FXMLLoader.load(getClass().getResource("/ListeTaxi.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void OpenCourseListPageAction(ActionEvent event) {
        try {
            // Load the Course List page
            Parent root = FXMLLoader.load(getClass().getResource("/ListeCourses.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void logout() {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setScene(new Scene(loginPage));
        } catch (IOException e) {
            System.err.println("Échec de la déconnexion");
            e.printStackTrace();
        }
    }

    // Méthode handleChat() si vous en avez besoin
    @FXML
    private void handleChat() {
        loadPage("/Chat.fxml");
    }
}