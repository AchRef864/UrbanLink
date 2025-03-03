package tn.esprit.jdbc.controllers;

import tn.esprit.jdbc.entities.Reponse;
import tn.esprit.jdbc.services.ReponseService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.sql.SQLException;

public class UpdateReponseController {

    @FXML
    private TextField commentaireTextField;

    private ReponseService reponseService = new ReponseService();
    private Reponse reponse;

    @FXML
    public void initialize() {
        // Add input validation for commentaireTextField
        commentaireTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("[a-zA-Z0-9\\s]*")) {
                    commentaireTextField.setText(newValue.replaceAll("[^a-zA-Z0-9\\s]", ""));
                }
            }
        });
    }

    public void setReponse(Reponse reponse) {
        this.reponse = reponse;
        commentaireTextField.setText(reponse.getCommentaire());
    }

    @FXML
    private void updateReponseAction(ActionEvent event) {
        String commentaire = commentaireTextField.getText();

        if (commentaire.length() < 2) {
            showErrorAlert("Validation Error", "Commentaire must be at least 2 characters long");
            return;
        }

        reponse.setCommentaire(commentaire);

        try {
            reponseService.update(reponse);
            showInfoAlert("Update Reponse", "Reponse updated successfully!");
        } catch (SQLException e) {
            showErrorAlert("Error updating response", e.getMessage());
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