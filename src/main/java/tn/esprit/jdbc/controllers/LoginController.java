package tn.esprit.jdbc.controllers;

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
import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordTextField;

    private UserService userService = new UserService();

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

                // Check the user's role
                if ("admin".equals(user.getRole())) {
                    // Load Admin Dashboard
                    loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
                    root = loader.load();

                    // Pass data to the DashboardController
                    DashboardController dashboardController = loader.getController();
                    dashboardController.setUser(user); // Pass the user to the dashboard
                } else {
                    // Load Client Dashboard
                    loader = new FXMLLoader(getClass().getResource("/DashboardClient.fxml"));
                    root = loader.load();

                    // Pass data to the DashboardClientController
                    DashboardClientController dashboardClientController = loader.getController();
                    dashboardClientController.setUser(user); // Pass the user to the client dashboard
                }

                // Set new scene
                stage.setScene(new Scene(root));
            } else {
                showAlert("Login Failed", "Invalid email or password.");
            }
        } catch (SQLException | IOException e) {
            showAlert("Error", "An error occurred during login: " + e.getMessage());
        }
    }

    @FXML
    public void handleCreateAccountLink() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CreateNewAccount.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) emailTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Error", "An error occurred while loading the create account page: " + e.getMessage());
        }
    }

    // Input validation methods
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 7;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}