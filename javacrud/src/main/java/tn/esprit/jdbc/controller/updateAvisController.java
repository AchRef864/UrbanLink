package tn.esprit.jdbc.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import tn.esprit.jdbc.entities.Avis;
import tn.esprit.jdbc.services.AvisService;

import java.sql.SQLException;

public class UpdateAvisController {

    @FXML
    private TextField commentaireTextField;

    @FXML
    private ComboBox<Integer> noteComboBox;

    @FXML
    private ComboBox<Integer> userIdComboBox;

    private Avis avis; // The review being edited
    private final AvisService avisService = new AvisService();

    @FXML
    public void initialize() {
        // Populate the noteComboBox with values from 1 to 10
        ObservableList<Integer> noteValues = FXCollections.observableArrayList();
        for (int i = 1; i <= 10; i++) {
            noteValues.add(i);
        }
        noteComboBox.setItems(noteValues);

        // Populate the userIdComboBox with user IDs (you can fetch this from a service)
        ObservableList<Integer> userIdValues = FXCollections.observableArrayList();
        userIdValues.addAll(1, 2, 3, 4, 5); // Example user IDs
        userIdComboBox.setItems(userIdValues);
    }

    // Set the review to be edited
    public void setAvis(Avis avis) {
        this.avis = avis;
        populateFields(); // Populate the fields with the review's data
    }

    // Populate the fields with the review's data
    private void populateFields() {
        if (avis != null) {
            commentaireTextField.setText(avis.getCommentaire());
            noteComboBox.setValue(avis.getNote());
            userIdComboBox.setValue(avis.getUser_id());
        }
    }

    // Handle the update button action
    @FXML
    private void updateAvisAction() {
        try {
            // Update the review's data
            avis.setCommentaire(commentaireTextField.getText());
            avis.setNote(noteComboBox.getValue());
            avis.setUser_id(userIdComboBox.getValue());

            // Save the updated review
            avisService.update(avis);

            // Close the updateAvis page
            closeWindow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Close the updateAvis page
    private void closeWindow() {
        commentaireTextField.getScene().getWindow().hide();
    }
}