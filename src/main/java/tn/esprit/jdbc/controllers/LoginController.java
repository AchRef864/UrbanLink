package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import tn.esprit.jdbc.entities.User;
import tn.esprit.jdbc.services.UserService;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private ImageView bearImageView;

    @FXML
    private WebView recaptchaWebView; // WebView for reCAPTCHA

    private UserService userService = new UserService();

    // Replace with your reCAPTCHA site key and secret key
    private final String RECAPTCHA_SITE_KEY = "6LfItOkqAAAAAJhIWsK-09iOBK0vrsJCdO5RbcC2";
    private final String RECAPTCHA_SECRET_KEY = "6LfItOkqAAAAAEIoYcFSJs6L5IP81qcLoi_ORN0y";

    @FXML
    public void initialize() {
        // Load reCAPTCHA widget
        loadRecaptcha();
    }

    /**
     * Loads the reCAPTCHA widget into the WebView.
     */
    private void loadRecaptcha() {
        String recaptchaHtml = "<html>" +
                "<head>" +
                "<script src='https://www.google.com/recaptcha/api.js'></script>" +
                "</head>" +
                "<body>" +
                "<div class='g-recaptcha' data-sitekey='" + RECAPTCHA_SITE_KEY + "'></div>" +
                "</body>" +
                "</html>";
        recaptchaWebView.getEngine().loadContent(recaptchaHtml);
    }

    /**
     * Handles the login button click event.
     */
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

        // Verify reCAPTCHA
        String recaptchaResponse = recaptchaWebView.getEngine().executeScript("grecaptcha.getResponse()").toString();
        if (recaptchaResponse.isEmpty()) {
            showAlert("reCAPTCHA Error", "Please complete the reCAPTCHA.");
            return;
        }

        if (!verifyRecaptcha(recaptchaResponse)) {
            showAlert("reCAPTCHA Error", "reCAPTCHA verification failed.");
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

    /**
     * Handles the "Create Account" link click event.
     */
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

    /**
     * Verifies the reCAPTCHA response with Google's API.
     */
    private boolean verifyRecaptcha(String recaptchaResponse) {
        try {
            String url = "https://www.google.com/recaptcha/api/siteverify";
            String params = "secret=" + URLEncoder.encode(RECAPTCHA_SECRET_KEY, StandardCharsets.UTF_8) +
                    "&response=" + URLEncoder.encode(recaptchaResponse, StandardCharsets.UTF_8);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(params))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body().contains("\"success\": true");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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

    @FXML
    public void handleForgotPasswordLink() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ForgotPassword.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) emailTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Error", "An error occurred while loading the forgot password page: " + e.getMessage());
        }
    }
}