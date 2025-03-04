package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import tn.esprit.jdbc.entities.User;
import java.io.IOException;

import tn.esprit.jdbc.controllers.abonnement.ControllerReservationAjout;
import tn.esprit.jdbc.controllers.abonnement.ReservationController;
import tn.esprit.jdbc.controllers.abonnement.ReservationController;

import tn.esprit.jdbc.controllers.abonnement.ControllerAjoutAbonnement;





public class DashboardClientController {

    @FXML
    private StackPane contentArea;
    @FXML
    private Button btnHome, btnVehicle, btnMaintenance, btnInsertUser, btnEditUser, btnRateRide, btnLogout, btnListeTaxisEtCourses , btnAddReservation , btnAddAbonnement;

    @FXML
    private Button btnReviews, btnComplainings; // New buttons for Reviews & Complaints

    private User currentUser; // Store logged-in user information

    @FXML
    public void initialize() {
        btnHome.setOnAction(e -> loadPage("/Home.fxml", currentUser != null ? currentUser.getUserId() : -1));
        btnVehicle.setOnAction(e -> loadPage("/ListerVehicle.fxml", currentUser != null ? currentUser.getUserId() : -1));
        btnMaintenance.setOnAction(e -> loadPage("/ListerMaintenance.fxml", currentUser != null ? currentUser.getUserId() : -1));
        btnRateRide.setOnAction(e -> loadPage("/RatingForm.fxml")); // Load the rating form
        btnListeTaxisEtCourses.setOnAction(this::handleListeTaxisEtCourses);
        btnAddReservation.setOnAction(e -> loadPage("/ajoutReservation.fxml"));
         btnAddAbonnement.setOnAction(e -> loadPage("/ajoutAbonnement.fxml"));
        btnLogout.setOnAction(e -> logout());


        // New button actions
        btnReviews.setOnAction(e -> {
            if (currentUser != null) {
                loadPage("/UserAvisTable.fxml", currentUser.getUserId()); // Pass the user ID
            } else {
                showAlert("Error", "No user is logged in.");
            }
        });

        // Modified action for the Complainings button
        btnComplainings.setOnAction(this::btnComplainingsAction); // Handles button click
    }
    private void updateUIForRole() {
        if (currentUser != null) {
            if ("admin".equals(currentUser.getRole())) {
                // Admin-specific UI updates
                btnInsertUser.setVisible(true); // Show "Add User" button for admins
                btnEditUser.setVisible(true);   // Show "Edit User" button for admins
            } else if ("client".equals(currentUser.getRole())) {
                // Client-specific UI updates
                btnInsertUser.setVisible(false); // Hide "Add User" button for clients
                btnEditUser.setVisible(false);   // Hide "Edit User" button for clients
            }
        }
    }

    /**
     * Sets the logged-in user and updates the UI accordingly.
     */
    public void setUser(User user) {
        this.currentUser = user;
        updateUIForRole();
        System.out.println("User set in Dashboard: " + user.getUserId()); // Debug log
    }

    /**
     * Loads the specified FXML page into the content area.
     */
    private void loadPage(String fxml, int userId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent page = loader.load();

            // Pass the user ID to the controller
            if (fxml.equals("/UserAvisTable.fxml")) {
                UserAvisController userAvisController = loader.getController();
                userAvisController.setLoggedInUserId(userId); // Pass the logged-in user ID
            }

            contentArea.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load page: " + fxml);
        }
    }

    /**
     * Loads the specified FXML page into the content area.
     *
     * @param fxml The path to the FXML file.
     */
    private void loadPage(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent page = loader.load();

            // Pass the user ID to the RatingController if the loaded page is the rating form
            if (fxml.equals("/RatingForm.fxml")) {
                RatingController ratingController = loader.getController();
                ratingController.setUserId(currentUser.getUserId()); // Pass the user ID
            }

            // Pass the user ID to the ControllerReservationAjout if the loaded page is ajoutReservation.fxml
            if (fxml.equals("/ajoutReservation.fxml")) {
                ControllerReservationAjout controller = loader.getController();
                controller.setLoggedInUser(currentUser); // error here : setLoggedInUser method not found
            }

            if (fxml.equals("/ajoutAbonnement.fxml")) {
                ControllerAjoutAbonnement controller = loader.getController(); // Correct cast
                controller.setLoggedInUser(currentUser); // Pass the logged-in user
            }

            contentArea.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading page: " + fxml);
        }
    }
   /*
   private void loadPage(String fxml) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent page = loader.load();
        try {
            // Pass the user ID to the RatingController if the loaded page is the rating form
            if (fxml.equals("/RatingForm.fxml")) {
                RatingController ratingController = loader.getController();
                ratingController.setUserId(currentUser.getUserId()); // Pass the user ID
            }

            contentArea.getChildren().setAll(page);
        }try {
            // Pass the user ID to the ControllerReservationAjout if the loaded page is ajoutReservation.fxml
            if (fxml.equals("/ajoutReservation.fxml")) {
                ControllerReservationAjout controller = loader.getController();
                controller.setLoggedInUser(currentUser); // Pass the logged-in user
            }

            // Pass the user ID to the ReservationController if the loaded page is ajoutAbonnement.fxml
            if (fxml.equals("/ajoutAbonnement.fxml")) {
                ReservationController controller = loader.getController();
                controller.setLoggedInUser(currentUser); // Pass the logged-in user
            }

            contentArea.getChildren().setAll(page);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading page: " + fxml);
        }
    }
*/
    /**
     * Handles the "Liste Taxis et Courses" button click.
     */
    @FXML
    private void handleListeTaxisEtCourses(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeTaxisEtCourses.fxml"));
            Parent page = loader.load();

            // Pass the user ID to the ListeTaxisEtCoursesController
            ListeTaxisEtCoursesController controller = loader.getController();
            controller.setUserId(currentUser.getUserId()); // Pass the user ID

            // Set the loaded page into the content area
            contentArea.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load Liste Taxis et Courses page: " + e.getMessage());
        }
    }

    /**
     * Handles the "Complainings" button click and passes the user ID to the ReclamationController.
     */
    @FXML
    private void btnComplainingsAction(ActionEvent event) {
        try {
            // Load the Reclamation FXML page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation.fxml"));
            Parent page = loader.load();

            // Get the ReclamationController and set the user ID
            ReclamationController reclamationController = loader.getController();
            if (currentUser != null) {
                reclamationController.setUserId(currentUser.getUserId()); // Pass userId from currentUser
            } else {
                System.out.println("No user logged in.");
            }

            // Add the page to the content area
            contentArea.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Reclamation page.");
        }
    }

    /**
     * Logs out the user and redirects to the login page.
     */
    private void logout() {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setScene(new Scene(loginPage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays an alert with the specified title and message.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
