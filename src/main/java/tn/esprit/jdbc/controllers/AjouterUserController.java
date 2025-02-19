package tn.esprit.jdbc.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.jdbc.entities.User;
import tn.esprit.jdbc.services.UserService;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterUserController {

    @FXML
    private TextField nomTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private ComboBox<String> adminComboBox; // ComboBox for selecting Admin/Client

    @FXML
    public void initialize() {
        // Add options to the ComboBox
        adminComboBox.getItems().addAll("Admin", "Client");
        adminComboBox.setValue("Client"); // Set default value
    }

    @FXML
    void ajouteUserAction(ActionEvent event) {
        String nom = nomTextField.getText();
        String email = emailTextField.getText();
        String phone = phoneTextField.getText();
        String password = passwordTextField.getText();
        int admin = adminComboBox.getValue().equals("Admin") ? 1 : 0; // Convert selection to int

        // Input validation
        if (!isValidEmail(email)) {
            showAlert("Invalid Email", "Email must contain '@'.");
            return;
        }

        if (!isValidPhone(phone)) {
            showAlert("Invalid Phone", "Phone must be a number of 8 digits.");
            return;
        }

        if (!isValidPassword(password)) {
            showAlert("Invalid Password", "Password must be at least 8 characters long.");
            return;
        }

        // Use the correct constructor
        User user = new User(nom, email, phone, password, admin);

        UserService serviceUser = new UserService();
        try {
            serviceUser.insert(user);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("User ajouté avec succès");
            alert.showAndWait();

            // Load the Detail.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Detail.fxml"));
            Parent root = loader.load();

            DetailController detailController = loader.getController();
            // Pass data to the Detail controller
            detailController.setNomTextField(nom);
            detailController.setEmailTextField(email);
            detailController.setPhoneTextField(phone);

            // Change the scene
            nomTextField.getScene().setRoot(root);

        } catch (SQLException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de l'ajout de l'utilisateur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            System.err.println(e.getMessage());
        }
    }

    @FXML
    void ExitButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    // Input validation methods
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }

    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{8}");
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}