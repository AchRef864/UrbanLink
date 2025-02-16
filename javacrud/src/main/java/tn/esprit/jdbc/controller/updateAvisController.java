package tn.esprit.jdbc.controller;

import tn.esprit.jdbc.entities.Avis;
import tn.esprit.jdbc.services.AvisService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class updateAvisController {

    @FXML
    private TextField avisIdTextField;

    @FXML
    private TextField commentaireTextField;

    @FXML
    private TextField noteTextField;

    @FXML
    private TextField userIdTextField;

    @FXML
    void loadAvisAction(ActionEvent event) {
        int avisId = Integer.parseInt(avisIdTextField.getText());
        AvisService avisService = new AvisService();
        try {
            Avis avis = avisService.findById(avisId);
            if (avis != null) {
                commentaireTextField.setText(avis.getCommentaire());
                noteTextField.setText(String.valueOf(avis.getNote()));
                userIdTextField.setText(String.valueOf(avis.getUser_id()));
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Avis not found");
                alert.setContentText("No review found with the given ID.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error loading review");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            System.err.println(e.getMessage());
        }
    }

    @FXML
    void updateAvisAction(ActionEvent event) {
        int avisId = Integer.parseInt(avisIdTextField.getText());
        String commentaire = commentaireTextField.getText();
        int note = Integer.parseInt(noteTextField.getText());
        int userId = Integer.parseInt(userIdTextField.getText());

        Avis avis = new Avis(avisId, note, commentaire, new java.util.Date(), userId);

        AvisService avisService = new AvisService();
        try {
            avisService.update(avis);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Review updated successfully");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error updating review");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            System.err.println(e.getMessage());
        }
    }
}