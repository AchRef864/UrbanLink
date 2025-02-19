package tn.esprit.jdbc.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.jdbc.entities.User;
import tn.esprit.jdbc.services.UserService;
import javafx.scene.layout.AnchorPane;
import  tn.esprit.jdbc.controller.UserAvisController;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordTextField;

    private final UserService userService = new UserService();

    @FXML
    public void handleLoginButton() {
        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        // Input validation
        if (!isValidEmail(email)) {
            showAlert("Invalid Email", "Email must contain '@'.");
            return;
        }

        if (!isValidPassword(password)) {
            showAlert("Invalid Password", "Password must be at least 8 characters long.");
            return;
        }

        try {
            // Authenticate the user
            User user = userService.authenticate(email, password);

            if (user != null) {
                FXMLLoader loader;
                Parent root;
                Stage stage = (Stage) emailTextField.getScene().getWindow();

                if (user.getAdmin() == 1) {
                    // Load Admin Page
                    loader = new FXMLLoader(getClass().getResource("/HelloAdmin.fxml"));
                    root = loader.load();
                } else {
                    // Load Client Page
                    loader = new FXMLLoader(getClass().getResource("/ClientPage.fxml"));
                    root = loader.load();

                    // Pass the user ID to the client page controller
                    ClientPageController clientPageController = loader.getController();
                    clientPageController.setUserId(user.getUserId());

                    // Load the UserAvis view and pass its controller to ClientPageController
                    FXMLLoader userAvisLoader = new FXMLLoader(getClass().getResource("/UserAvisTable.fxml"));
                    AnchorPane userAvisView = userAvisLoader.load();
                    UserAvisController userAvisController = userAvisLoader.getController();
                    clientPageController.setUserAvisController(userAvisController);

                    // Add the UserAvis view to the container
                    clientPageController.getUserAvisContainer().getChildren().add(userAvisView);
                }

                // Set the new scene and show the stage
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                showAlert("Login Failed", "Invalid email or password.");
            }
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while accessing the database: " + e.getMessage());
        } catch (IOException e) {
            showAlert("Application Error", "An error occurred while loading the page: " + e.getMessage());
        }
    }
    // Input validation methods
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }

    // Helper method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}