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
    private TextField avisIdTextField;

    @FXML
    private TextField commentaireTextField;

    @FXML
    private TextField noteTextField;

    @FXML
    private TextField userIdTextField;

    private Avis avis;
    private AvisService avisService = new AvisService();

    public void setAvis(Avis avis) {
        this.avis = avis;
        noteTextField.setText(String.valueOf(avis.getNote()));
        commentaireTextField.setText(avis.getCommentaire());
        // Assuming dateAvisField is a TextField for date
        // dateAvisField.setText(avis.getDate_avis().toString());
        userIdTextField.setText(String.valueOf(avis.getUser_id()));
    }

    @FXML
    void loadAvisAction(ActionEvent event) {
        int avisId = Integer.parseInt(avisIdTextField.getText());
        try {
            Avis avis = avisService.findById(avisId);
            if (avis != null) {
                setAvis(avis);
            } else {
                showErrorAlert("Avis not found", "No review found with the given ID.");
            }
        } catch (SQLException e) {
            showErrorAlert("Error loading review", e.getMessage());
        }
    }

    @FXML
    void updateAvisAction(ActionEvent event) {
        int avisId = Integer.parseInt(avisIdTextField.getText());
        String commentaire = commentaireTextField.getText();
        int note = Integer.parseInt(noteTextField.getText());
        int userId = Integer.parseInt(userIdTextField.getText());

        Avis avis = new Avis(avisId, note, commentaire, new java.util.Date(), userId);

        try {
            avisService.update(avis);
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