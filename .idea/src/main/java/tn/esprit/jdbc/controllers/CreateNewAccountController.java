package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.jdbc.entities.User;
import tn.esprit.jdbc.services.UserService;

import java.sql.SQLException;

public class CreateNewAccountController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private PasswordField confirmPasswordTextField;

    private UserService userService = new UserService();

    @FXML
    public void handleCreateAccountButton() {
        String name = nameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        String confirmPassword = confirmPasswordTextField.getText();

        // Input validation
        if (name == null || name.trim().isEmpty()) {
            showAlert("Invalid Name", "Name cannot be empty.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Invalid Email", "Email must contain '@'.");
            return;
        }

        if (!isValidPassword(password)) {
            showAlert("Invalid Password", "Password must be at least 8 characters long.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Password Mismatch", "Passwords do not match.");
            return;
        }

        try {
            // Check if email is already in use
            if (userService.isEmailInUse(email)) {
                showAlert("Email In Use", "This email is already registered.");
                return;
            }

            // Create a new user with admin set to 0 (client)
            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setAdmin(0);

            // Add the new user to the database
            userService.addUser(newUser);

            showAlert("Account Created", "Your account has been created successfully.");

            // Close the create account window
            Stage stage = (Stage) emailTextField.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while creating the account: " + e.getMessage());
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