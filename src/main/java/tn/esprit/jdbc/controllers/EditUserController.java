package tn.esprit.jdbc.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
<<<<<<< Updated upstream
import javafx.event.ActionEvent;
=======
>>>>>>> Stashed changes
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
<<<<<<< Updated upstream
import javafx.stage.Stage;
=======
>>>>>>> Stashed changes
import tn.esprit.jdbc.entities.User;
import tn.esprit.jdbc.services.UserService;

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
<<<<<<< Updated upstream


=======
>>>>>>> Stashed changes
}