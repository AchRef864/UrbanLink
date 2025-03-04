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
import tn.esprit.jdbc.controllers.abonnement.ControllerReservationAjout;
import tn.esprit.jdbc.controllers.abonnement.ControllerAjoutAbonnement;
import java.io.IOException;

public class DashboardClientController {

    @FXML
    private StackPane contentArea;
    @FXML
    private Button btnHome, btnVehicle, btnMaintenance, btnInsertUser, btnEditUser, btnRateRide, btnLogout, btnListeTaxisEtCourses;
    @FXML
    private Button btnReviews, btnComplainings, btnAddReservation, btnAddAbonnement;

    private User currentUser;

    @FXML
    public void initialize() {
        btnHome.setOnAction(e -> loadPage("/Home.fxml", currentUser != null ? currentUser.getUserId() : -1));
        btnVehicle.setOnAction(e -> loadPage("/ListerVehicle.fxml", currentUser != null ? currentUser.getUserId() : -1));
        btnMaintenance.setOnAction(e -> loadPage("/ListerMaintenance.fxml", currentUser != null ? currentUser.getUserId() : -1));
        btnRateRide.setOnAction(e -> loadPage("/RatingForm.fxml", currentUser != null ? currentUser.getUserId() : -1));
        btnListeTaxisEtCourses.setOnAction(this::handleListeTaxisEtCourses);
        btnAddReservation.setOnAction(this::handleAddReservation);
        btnAddAbonnement.setOnAction(this::handleAddAbonnement);
        btnLogout.setOnAction(e -> logout());

        btnReviews.setOnAction(e -> {
            if (currentUser != null) {
                loadPage("/UserAvisTable.fxml", currentUser.getUserId());
            } else {
                showAlert("Error", "No user is logged in.");
            }
        });

        btnComplainings.setOnAction(this::btnComplainingsAction);
    }

    private void updateUIForRole() {
        if (currentUser != null) {
            if ("admin".equals(currentUser.getRole())) {
                btnInsertUser.setVisible(true);
                btnEditUser.setVisible(true);
            } else if ("client".equals(currentUser.getRole())) {
                btnInsertUser.setVisible(false);
                btnEditUser.setVisible(false);
            }
        }
        // Always make btnAddAbonnement visible, regardless of role
        btnAddAbonnement.setVisible(true);
    }

    public void setUser(User user) {
        this.currentUser = user;
        updateUIForRole();
        System.out.println("User set in DashboardClient: " + user.getUserId());
    }

    private void loadPage(String fxml, int userId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent page = loader.load();

            if (fxml.equals("/UserAvisTable.fxml")) {
                UserAvisController userAvisController = loader.getController();
                userAvisController.setLoggedInUserId(userId);
            }

            contentArea.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load page: " + fxml);
        }
    }

    @FXML
    private void handleListeTaxisEtCourses(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeTaxisEtCourses.fxml"));
            Parent page = loader.load();
            ListeTaxisEtCoursesController controller = loader.getController();
            controller.setUserId(currentUser.getUserId());
            contentArea.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load Liste Taxis et Courses page: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddReservation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutReservation.fxml"));
            Parent page = loader.load();
            ControllerReservationAjout controller = loader.getController();
            controller.setLoggedInUser(currentUser);
            contentArea.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Créer Réservation page: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddAbonnement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutAbonnement.fxml"));
            Parent page = loader.load();
            ControllerAjoutAbonnement controller = loader.getController();
            controller.setLoggedInUser(currentUser);
            contentArea.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Ajouter Abonnement page: " + e.getMessage());
        }
    }

    @FXML
    private void btnComplainingsAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation.fxml"));
            Parent page = loader.load();
            ReclamationController reclamationController = loader.getController();
            if (currentUser != null) {
                reclamationController.setUserId(currentUser.getUserId());
            } else {
                System.out.println("No user logged in.");
            }
            contentArea.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Reclamation page.");
        }
    }

    private void logout() {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setScene(new Scene(loginPage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}