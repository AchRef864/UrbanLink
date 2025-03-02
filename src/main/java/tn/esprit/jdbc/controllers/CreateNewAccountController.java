package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tn.esprit.jdbc.entities.User;
import tn.esprit.jdbc.services.UserService;

<<<<<<< Updated upstream
=======
import java.io.IOException;
>>>>>>> Stashed changes
import java.sql.SQLException;
import java.util.Random;

public class CreateNewAccountController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
<<<<<<< Updated upstream
    private PasswordField passwordTextField;
=======
    private TextField phoneTextField;

    @FXML
    private PasswordField passwordField;
>>>>>>> Stashed changes

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
<<<<<<< Updated upstream
        String password = passwordTextField.getText();
        String confirmPassword = confirmPasswordTextField.getText();
=======
        String phone = phoneTextField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
>>>>>>> Stashed changes

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
<<<<<<< Updated upstream
            newUser.setPassword(password);
            newUser.setAdmin(0);
=======
            newUser.setPhone(phone);
            newUser.setPassword(password);
            newUser.setRole("client");
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
    // Input validation methods
=======
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

>>>>>>> Stashed changes
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }

<<<<<<< Updated upstream
=======
    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{1,7}");
    }

>>>>>>> Stashed changes
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