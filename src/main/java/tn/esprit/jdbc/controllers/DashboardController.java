package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.stage.Stage;
<<<<<<< Updated upstream

=======
import tn.esprit.jdbc.entities.User;
>>>>>>> Stashed changes

public class DashboardController {

    @FXML
    private void handleChat() {
        loadPage("/Chat.fxml");
    }

    @FXML
    private StackPane contentArea;

    @FXML
<<<<<<< Updated upstream
    private Button btnHome, btnVehicle, btnMaintenance, btnReclamation, btnInsertUser, btnEditUser, btnLogout;
=======
<<<<<<< HEAD
    private Button btnHome, btnVehicle, btnMaintenance, btnInsertUser, btnEditUser, btnLogout, btnViewRatings, btnOpenTaxiListPage, btnOpenCourseListPage, btnChat;

    @FXML
    private Button btnReviews, btnComplainings; // New buttons added
=======
    private Button btnHome, btnVehicle, btnMaintenance, btnInsertUser, btnEditUser, btnLogout, btnViewRatings;
>>>>>>> e244cabab4672bf5258bf2c96b9c84d2fec77a85
>>>>>>> Stashed changes

    @FXML
    public void initialize() {
        // Set actions for buttons
<<<<<<< HEAD
        btnHome.setOnAction(e -> loadPage("/Home.fxml"));
        btnVehicle.setOnAction(e -> loadPage("/ListerVehicle.fxml"));
        btnMaintenance.setOnAction(e -> loadPage("/ListerMaintenance.fxml"));
<<<<<<< Updated upstream
        btnReclamation.setOnAction(e -> loadPage("/AdminReclamationResponse.fxml"));
=======
>>>>>>> Stashed changes
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
=======
        btnHome.setOnAction(e -> loadPage("/Home.fxml")); // Load Home page
        btnVehicle.setOnAction(e -> loadPage("/ListerVehicle.fxml")); // Load Vehicle page
        btnMaintenance.setOnAction(e -> loadPage("/ListerMaintenance.fxml")); // Load Maintenance page
        btnInsertUser.setOnAction(e -> loadPage("/AjouterUser.fxml")); // Load Insert User page
        btnEditUser.setOnAction(e -> loadPage("/EditUser.fxml")); // Load Edit User page
        btnViewRatings.setOnAction(e -> loadPage("/ViewRatings.fxml")); // Load View Ratings page
        btnLogout.setOnAction(e -> logout()); // Logout
>>>>>>> e244cabab4672bf5258bf2c96b9c84d2fec77a85
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
<<<<<<< Updated upstream
=======

    public void setUser(User user) {
        // Use the user data as needed
    }
>>>>>>> Stashed changes

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
