package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import tn.esprit.jdbc.services.UserService;

import java.sql.SQLException;
import java.util.Random; // Import Random

public class ResetPasswordController {

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label suggestedPasswordLabel;

    private UserService userService = new UserService();
    private String suggestedPassword;
    private String email; // Add email field

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }

    @FXML
    public void handleSuggestPassword() {
        suggestedPassword = generateRandomPassword(10);
        newPasswordField.setText(suggestedPassword);
        confirmPasswordField.setText(suggestedPassword);
        suggestedPasswordLabel.setText("Suggested Password: " + suggestedPassword);
    }

    @FXML
    public void handleResetPassword() {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Password Mismatch", "Passwords do not match.");
            return;
        }

        try {
            // Update the password in the database
            userService.updatePassword(email, newPassword);
            showAlert("Success", "Your password has been reset.");
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while resetting the password: " + e.getMessage());
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}