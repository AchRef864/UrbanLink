package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.jdbc.services.UserService;

import java.io.IOException;
import java.sql.SQLException;

public class ForgotPasswordController {

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField verificationCodeTextField;

    private UserService userService = new UserService();

    @FXML
    public void handleSendCodeButton() {
        String email = emailTextField.getText();

        if (!isValidEmail(email)) {
            showAlert("Invalid Email", "Please enter a valid email address.");
            return;
        }

        try {
            // Check if the email exists in the database
            if (userService.doesEmailExist(email)) {
                // Generate a random verification code
                String code = userService.generateVerificationCode();

                // Save the code to the database
                userService.saveVerificationCode(email, code);

                // Send the code to the user's email
                EmailUtil.sendVerificationCode(email, code);

                showAlert("Code Sent", "A verification code has been sent to your email.");
            } else {
                showAlert("Error", "This email is not registered.");
            }
        } catch (SQLException e) {
            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }

    @FXML
    public void handleVerifyCodeButton() {
        String email = emailTextField.getText();
        String enteredCode = verificationCodeTextField.getText();

        try {
            // Verify the code
            if (userService.verifyCode(email, enteredCode)) {
                // Redirect to the password reset page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ResetPassword.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) emailTextField.getScene().getWindow();
                stage.setScene(new Scene(root));
            } else {
                showAlert("Invalid Code", "The verification code is incorrect.");
            }
        } catch (SQLException | IOException e) {
            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }

    @FXML
    public void handleComeBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/jdbc/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) emailTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Error", "An error occurred while loading the login page: " + e.getMessage());
        }
    }

    // Input validation method
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}