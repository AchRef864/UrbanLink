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
    private TextField txtAdminId; // Champ pour saisir l'ID utilisateur

    // Instanciation du service pour vérifier l'attribut admin
    private final ServiceUser userService = new ServiceUser();

    @FXML
    void handleAjouterTaxi(ActionEvent event) {
        // Récupération de l'ID saisi dans le TextField
        String userIdString = txtAdminId.getText().trim();

        // Vérification basique : s'assurer que c'est un nombre
        int userId;
        try {
            userId = Integer.parseInt(userIdString);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez saisir un ID valide (numérique) !");
            alert.showAndWait();
            return;
        }

        // Vérifier si l'utilisateur est admin
        if (userService.isAdmin(userId)) {
            try {
                // Charger la page d'ajout de taxi
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutTaxi.fxml"));
                Parent root = loader.load();

                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Ajout Taxi");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // L'utilisateur n'est pas admin => on affiche une alerte
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Accès refusé");
            alert.setHeaderText(null);
            alert.setContentText("Vous n'êtes pas administrateur !");
            alert.showAndWait();
        }
    }

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