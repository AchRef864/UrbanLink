package tn.esprit.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class Home extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Chargez le fichier FXML et définissez le contrôleur
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();

            // Vous pouvez ici récupérer le contrôleur et configurer des actions si nécessaire
            Home controller = loader.getController();

            // Par exemple, vous pouvez définir des actions supplémentaires sur le contrôleur ici.

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Taxi Management - Home");
            stage.show();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    private Button ajouterTaxiBtn;

    @FXML
    void handleAjouterTaxi(ActionEvent event) {
        try {
            // Chargez le fichier FXML et définissez le contrôleur
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutTaxi.fxml"));
            Parent root = loader.load();

            // Vous pouvez ici récupérer le contrôleur et configurer des actions si nécessaire
            AjoutTaxiController controller = loader.getController();

            // Par exemple, vous pouvez définir des actions supplémentaires sur le contrôleur ici.

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Ajout Taxi");
            stage.show();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}
