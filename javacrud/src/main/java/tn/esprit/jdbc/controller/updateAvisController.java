package tn.esprit.jdbc.controller;

import tn.esprit.jdbc.entities.Avis;
import tn.esprit.jdbc.services.AvisService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.sql.SQLException;

public class UpdateAvisController {

    @FXML
    private TextField noteTextField;

    @FXML
    private TextField commentaireTextField;

    @FXML
    private TextField userIdTextField;

    private AvisService avisService = new AvisService();
    private Avis avis;

    public void setAvis(Avis avis) {
        this.avis = avis;
        noteTextField.setText(String.valueOf(avis.getNote()));
        commentaireTextField.setText(avis.getCommentaire());
        userIdTextField.setText(String.valueOf(avis.getUser_id()));
    }

    @FXML
    void updateAvisAction(ActionEvent event) {
        String commentaire = commentaireTextField.getText();
        int note = Integer.parseInt(noteTextField.getText());
        int userId = Integer.parseInt(userIdTextField.getText());

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