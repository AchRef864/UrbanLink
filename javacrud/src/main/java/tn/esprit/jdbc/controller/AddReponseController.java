package tn.esprit.jdbc.controller;

import tn.esprit.jdbc.entities.Reponse;
import tn.esprit.jdbc.services.ReponseService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.event.ActionEvent;

import java.sql.SQLException;
import java.util.Date;

public class AddReponseController {

    @FXML
    private TextArea commentaireTextArea;

    private int avisId;
    private ReponseTableController reponseTableController;

    public void setAvisId(int avisId) {
        this.avisId = avisId;
    }

    public void setReponseTableController(ReponseTableController reponseTableController) {
        this.reponseTableController = reponseTableController;
    }

    @FXML
    private void addReponseAction(ActionEvent event) {
        String commentaire = commentaireTextArea.getText();

        if (commentaire.length() < 2) {
            showErrorAlert("Validation Error", "Commentaire must be at least 2 characters long");
            return;
        }

        Reponse reponse = new Reponse(commentaire, new Date(), avisId, 1); // Assuming user_id is 1 for now

        ReponseService reponseService = new ReponseService();
        try {
            reponseService.insert(reponse);
            showInfoAlert("Information", "Response added successfully");

            // Reload the table data
            reponseTableController.loadReponsesData();
        } catch (SQLException e) {
            showErrorAlert("Error adding response", e.getMessage());
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