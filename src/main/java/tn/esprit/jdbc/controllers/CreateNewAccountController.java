package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tn.esprit.jdbc.entities.User;
import tn.esprit.jdbc.services.UserService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

public class CreateNewAccountController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button showHidePasswordButton;

    @FXML
    private Tooltip passwordTooltip;

    @FXML
    private Label suggestedPasswordLabel;

    private boolean isPasswordVisible = false;
    private String suggestedPassword;

    private UserService userService = new UserService();

    @FXML
    public void handleCreateAccountButton() {
        String name = nameTextField.getText();
        String email = emailTextField.getText();
        String phone = phoneTextField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

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
            newUser.setPhone(phone);
            newUser.setPassword(password);
            newUser.setRole("client");

            // Add the new user to the database
            userService.addUser(newUser);

            showAlert("Account Created", "Your account has been created successfully.");

            // Redirect to the login page
            redirectToLoginPage();
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while creating the account: " + e.getMessage());
        }
    }

    @FXML
    public void handlePasswordFieldClick() {
        suggestedPassword = generateRandomPassword(10);
        passwordField.setText(suggestedPassword);
        confirmPasswordField.setText(suggestedPassword);
        suggestedPasswordLabel.setText(suggestedPassword);
    }

    @FXML
    public void handleShowHidePassword() {
        if (isPasswordVisible) {
            // Hide password (show as dots)
            passwordField.setVisible(true);
            confirmPasswordField.setVisible(true);
            showHidePasswordButton.setText("Show");
            isPasswordVisible = false;
        } else {
            // Show password (plain text)
            passwordField.setVisible(false);
            confirmPasswordField.setVisible(false);
            showHidePasswordButton.setText("Hide");
            isPasswordVisible = true;
        }
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }

    private void redirectToLoginPage() {
        try {
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
            Parent loginPage = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = (Stage) emailTextField.getScene().getWindow();
            stage.setScene(new Scene(loginPage));
        } catch (IOException e) {
            showAlert("Error", "An error occurred while loading the login page: " + e.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }

    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{1,7}");
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