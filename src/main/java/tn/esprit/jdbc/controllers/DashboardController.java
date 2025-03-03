package tn.esprit.jdbc.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import tn.esprit.jdbc.entities.User;

public class DashboardController {

    @FXML
    private StackPane contentArea;

    @FXML
    private Button btnHome, btnVehicle, btnMaintenance, btnInsertUser, btnEditUser, btnLogout, btnViewRatings, btnOpenTaxiListPage, btnOpenCourseListPage, btnChat;

    @FXML
    private Button btnReviews, btnComplainings; // New buttons added

    @FXML
    public void initialize() {
        // Set actions for buttons
        btnHome.setOnAction(e -> loadPage("/Home.fxml"));
        btnVehicle.setOnAction(e -> loadPage("/ListerVehicle.fxml"));
        btnMaintenance.setOnAction(e -> loadPage("/ListerMaintenance.fxml"));
        btnInsertUser.setOnAction(e -> loadPage("/AjouterUser.fxml"));
        btnEditUser.setOnAction(e -> loadPage("/EditUser.fxml"));
        btnViewRatings.setOnAction(e -> loadPage("/ViewRatings.fxml"));
        btnOpenTaxiListPage.setOnAction(e -> loadPage("/ListeTaxi.fxml"));
        btnOpenCourseListPage.setOnAction(e -> loadPage("/ListeCourses.fxml"));
        btnChat.setOnAction(e -> loadPage("/Chat.fxml"));
        btnLogout.setOnAction(e -> logout());

        // New buttons' actions
        btnReviews.setOnAction(e -> loadPage("/AvisTable.fxml"));
        btnComplainings.setOnAction(e -> loadPage("/AdminReclamationResponse.fxml"));
    }

    private void loadPage(String fxml) {
        try {
            System.out.println("Loading page: " + fxml);
            Parent page = FXMLLoader.load(getClass().getResource(fxml));
            contentArea.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading page: " + fxml);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load page: " + fxml);
            alert.showAndWait();
        }
    }

    public void setUser(User user) {
        // Use the user data as needed
    }

    private void logout() {
        System.out.println("User logged out.");
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setScene(new Scene(loginPage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
