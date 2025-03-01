package tn.esprit.jdbc.controller;

import tn.esprit.jdbc.entities.Reponse;
import tn.esprit.jdbc.services.ReponseService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Date;

public class AddReponseController {

    @FXML
    private TextField commentaireField;

    private int avisId;
    private ReponseService reponseService = new ReponseService();

    public void setAvisId(int avisId) {
        this.avisId = avisId;
    }

    @FXML
    private void saveReponse() {
        String commentaire = commentaireField.getText();
        Reponse reponse = new Reponse(commentaire, new Date(), avisId, 1); // Assuming user_id is 1 for now
        try {
            reponseService.insert(reponse);
            closeWindow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) commentaireField.getScene().getWindow();
        stage.close();
    }
}