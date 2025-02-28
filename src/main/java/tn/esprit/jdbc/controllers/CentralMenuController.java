package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CentralMenuController {

    @FXML
    private Text urbanLinkTitle; // Add this field for the UrbanLink title

    @FXML
    private Button taxiButton;

    @FXML
    private Button avisButton;

    @FXML
    private Button chatButton;

    @FXML
    private Button vehiculeButton;

    @FXML
    private Button reservationButton;

    @FXML
    private Button abonnementButton;

    @FXML
    public void initialize() {
        // Add an event handler to the UrbanLink title
        urbanLinkTitle.setOnMouseClicked(event -> openAboutUsPage());
    }

    @FXML
    public void handleTaxiButton() {
        System.out.println("Taxi Button Clicked");
        // Add logic to open the Taxi dashboard
    }

    @FXML
    public void handleAvisButton() {
        System.out.println("Avis Button Clicked");
        // Add logic to open the Avis dashboard
    }

    @FXML
    public void handleChatButton() {
        System.out.println("Chat Button Clicked");
        // Add logic to open the Chat dashboard
    }

    @FXML
    public void handleVehiculeButton() {
        System.out.println("Vehicule Button Clicked");
        // Add logic to open the Vehicule dashboard
    }

    @FXML
    public void handleReservationButton() {
        System.out.println("Reservation Button Clicked");
        // Add logic to open the Reservation dashboard
    }

    @FXML
    public void handleAbonnementButton() {
        System.out.println("Abonnement Button Clicked");
        // Add logic to open the Abonnement dashboard
    }

    // Method to open the About Us page
    public void openAboutUsPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/about_us.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) urbanLinkTitle.getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}