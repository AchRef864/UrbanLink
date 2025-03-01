package tn.esprit.jdbc.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import tn.esprit.jdbc.entities.User;
import tn.esprit.jdbc.services.UserService;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//io exeption
import java.io.IOException;


import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


import java.sql.SQLException;

public class EditUserController {

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, Integer> userIdColumn;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> phoneColumn;

    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private TextField userIdTextField;

    @FXML
    private VBox editFields;

    @FXML
    private TextField editNameTextField;

    @FXML
    private TextField editEmailTextField;

    @FXML
    private TextField editPhoneTextField;

    @FXML
    private TextField editPasswordTextField;

    private UserService userService = new UserService();





    @FXML
    public void initialize() {
        // Set up the columns in the TableView
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        // Load data into the TableView
        loadUserData();
    }

    private void loadUserData() {
        try {
            // Fetch all users from the database
            ObservableList<User> users = FXCollections.observableArrayList(userService.showAll());
            userTable.setItems(users);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDeleteButton() {
        try {
            // Get the user ID from the TextField
            int userId = Integer.parseInt(userIdTextField.getText());

            // Delete the user from the database
            userService.delete(userId);

            // Reload the data in the TableView
            loadUserData();

            // Clear the TextField
            userIdTextField.clear();

            System.out.println("User deleted successfully!");
        } catch (NumberFormatException e) {
            System.err.println("Invalid User ID. Please enter a valid number.");
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
    }

    @FXML
    public void handleEditButton() {
        // Get the selected user from the TableView
        User selectedUser = userTable.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            // Populate the editable fields with the selected user's data
            editNameTextField.setText(selectedUser.getName());
            editEmailTextField.setText(selectedUser.getEmail());
            editPhoneTextField.setText(selectedUser.getPhone());
            editPasswordTextField.setText(selectedUser.getPassword());

            // Show the editable fields
            editFields.setVisible(true);
        } else {
            System.err.println("No user selected.");
        }
    }

    @FXML
    public void handleUpdateButton() {
        try {
            // Get the selected user from the TableView
            User selectedUser = userTable.getSelectionModel().getSelectedItem();

            if (selectedUser != null) {
                // Validate input fields
                String email = editEmailTextField.getText();
                String phone = editPhoneTextField.getText();
                String password = editPasswordTextField.getText();

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

                // Update the user's data with the values from the text fields
                selectedUser.setName(editNameTextField.getText());
                selectedUser.setEmail(email);
                selectedUser.setPhone(phone);
                selectedUser.setPassword(password);

                // Update the user in the database using your dynamic update method
                userService.update(selectedUser);

                // Reload the data in the TableView
                loadUserData();

                // Hide the editable fields
                editFields.setVisible(false);

                System.out.println("User updated successfully!");
            } else {
                System.err.println("No user selected.");
            }
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
        }
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