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

public class CreateNewAccountController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField phoneTextField; // New field for phone number

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private PasswordField confirmPasswordTextField;

    private UserService userService = new UserService();

    @FXML
    public void handleCreateAccountButton() {
        String name = nameTextField.getText();
        String email = emailTextField.getText();
        String phone = phoneTextField.getText(); // Get phone number
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

        if (!isValidPhone(phone)) {
            showAlert("Invalid Phone", "Phone number must be less than 8 digits.");
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

            // Create a new user with role set to "client"
            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPhone(phone); // Set the phone number
            newUser.setPassword(password);
            newUser.setRole("client"); // Set the role to "client"

            // Add the new user to the database
            userService.addUser(newUser);

            showAlert("Account Created", "Your account has been created successfully.");

            // Redirect to the login page
            redirectToLoginPage();
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while creating the account: " + e.getMessage());
        }
    }

    /**
     * Redirects the user to the login page.
     */
    private void redirectToLoginPage() {
        try {
            // Load the login page
            Parent loginPage = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = (Stage) emailTextField.getScene().getWindow();
            stage.setScene(new Scene(loginPage));
        } catch (IOException e) {
            showAlert("Error", "An error occurred while loading the login page: " + e.getMessage());
        }
    }

    @FXML
    public void handleBackToLoginButton() {
        try {
            // Load the login page
            Parent loginPage = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = (Stage) emailTextField.getScene().getWindow();
            stage.setScene(new Scene(loginPage));
        } catch (IOException e) {
            showAlert("Error", "An error occurred while loading the login page: " + e.getMessage());
        }
    }

    // Input validation methods
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }

    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{1,7}"); // Phone number must be less than 8 digits
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