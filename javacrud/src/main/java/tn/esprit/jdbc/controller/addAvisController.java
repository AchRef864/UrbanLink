package tn.esprit.jdbc.controller;

import tn.esprit.jdbc.entities.Avis;
import tn.esprit.jdbc.entities.User;
import tn.esprit.jdbc.services.AvisService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import tn.esprit.jdbc.services.UserService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Arrays;
import java.util.List;

public class AddAvisController {

    @FXML
    private TextField commentaireTextField;

    @FXML
    private ComboBox<Integer> noteComboBox;

    @FXML
    private ComboBox<Integer> userIdComboBox;

    @FXML
    private Button addReviewButton;

    private AvisTableController avisTableController;

    public void setAvisTableController(AvisTableController avisTableController) {
        this.avisTableController = avisTableController;
    }

    @FXML
    public void initialize() {
        System.out.println("initialize called");

        // Populate noteComboBox with values 1 to 5
        noteComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));

        // Fetch user IDs from the database dynamically
        UserService userService = new UserService();
        List<Integer> userIds = new ArrayList<>();
        try {
            for (User user : userService.showAll()) {
                userIds.add(user.getUserId()); // Extracting only the user ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Populate userIdComboBox with the retrieved user IDs
        userIdComboBox.setItems(FXCollections.observableArrayList(userIds));

        // Add input validation for commentaireTextField
        commentaireTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("[a-zA-Z0-9\\s]*")) {
                    commentaireTextField.setText(newValue.replaceAll("[^a-zA-Z0-9\\s]", ""));
                }
                validateInputs();
            }
        });

        // Add listeners to validate inputs
        noteComboBox.valueProperty().addListener((obs, oldVal, newVal) -> validateInputs());
        userIdComboBox.valueProperty().addListener((obs, oldVal, newVal) -> validateInputs());

        // Initially disable the addReviewButton
        addReviewButton.setDisable(true);

        System.out.println("addReviewButton initialized: " + (addReviewButton != null));
    }

    private void validateInputs() {
        String commentaire = commentaireTextField.getText();
        Integer note = noteComboBox.getValue();
        Integer userId = userIdComboBox.getValue();

        // Enable the button only if all fields are filled out and commentaire has at least 2 characters
        addReviewButton.setDisable(commentaire.length() < 2 || note == null || userId == null);
    }

    @FXML
    void ajouteAvisAction(ActionEvent event) {
        System.out.println("ajouteAvisAction called");

        String commentaire = commentaireTextField.getText();
        Integer note = noteComboBox.getValue();
        Integer userId = userIdComboBox.getValue();

        System.out.println("Commentaire: " + commentaire);
        System.out.println("Note: " + note);
        System.out.println("User ID: " + userId);

        Avis avis = new Avis(note, commentaire, new Date(), userId);

        AvisService avisService = new AvisService();
        try {
            avisService.insert(avis);
            showInfoAlert("Information", "Review added successfully");

            // Reload the table data
            avisTableController.loadAvisData();
        } catch (SQLException e) {
            showErrorAlert("Error adding review", e.getMessage());
        }
    }

    private void showErrorAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfoAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}