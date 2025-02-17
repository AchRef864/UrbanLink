package tn.esprit.jdbc.controller;

import tn.esprit.jdbc.entities.Avis;
import tn.esprit.jdbc.services.AvisService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.sql.SQLException;
import java.util.Arrays;

public class UpdateAvisController {

    @FXML
    private ComboBox<Integer> noteComboBox;

    @FXML
    private TextField commentaireTextField;

    @FXML
    private ComboBox<Integer> userIdComboBox;

    @FXML
    private Button updateButton;

    private AvisService avisService = new AvisService();
    private Avis avis;

    public void setAvis(Avis avis) {
        this.avis = avis;
        noteComboBox.setValue(avis.getNote());
        commentaireTextField.setText(avis.getCommentaire());
        userIdComboBox.setValue(avis.getUser_id());
    }

    @FXML
    public void initialize() {
        // Populate noteComboBox with values 1 to 5
        noteComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));

        // Populate userIdComboBox with sample user IDs
        userIdComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(1, 2, 3, 4, 5)));

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
    }

    private void validateInputs() {
        String commentaire = commentaireTextField.getText();
        Integer note = noteComboBox.getValue();
        Integer userId = userIdComboBox.getValue();

        // Enable the button only if all fields are filled out and commentaire has at least 2 characters
        updateButton.setDisable(commentaire.length() < 2 || note == null || userId == null);
    }

    @FXML
    void updateAvisAction(ActionEvent event) {
        String commentaire = commentaireTextField.getText();
        int note = noteComboBox.getValue();
        int userId = userIdComboBox.getValue();

        Avis updatedAvis = new Avis(avis.getAvis_id(), note, commentaire, new java.util.Date(), userId);

        try {
            avisService.update(updatedAvis);
            showInfoAlert("Review updated successfully", null);
        } catch (SQLException e) {
            showErrorAlert("Error updating review", e.getMessage());
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