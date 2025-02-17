package tn.esprit.jdbc.controller;

import tn.esprit.jdbc.entities.Reponse;
import tn.esprit.jdbc.services.ReponseService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.sql.SQLException;

public class UpdateReponseController {

    @FXML
    private TextField commentaireTextField;

    @FXML
    private ComboBox<Integer> avisIdComboBox;

    @FXML
    private ComboBox<Integer> userIdComboBox;

    private ReponseService reponseService = new ReponseService();
    private Reponse reponse;

    public void setReponse(Reponse reponse) {
        this.reponse = reponse;
        commentaireTextField.setText(reponse.getCommentaire());
        avisIdComboBox.setValue(reponse.getAvis_id());
        userIdComboBox.setValue(reponse.getUser_id());
    }

    @FXML
    private void updateReponseAction(ActionEvent event) {
        reponse.setCommentaire(commentaireTextField.getText());
        reponse.setAvis_id(avisIdComboBox.getValue());
        reponse.setUser_id(userIdComboBox.getValue());

        try {
            reponseService.update(reponse);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update Reponse");
            alert.setHeaderText(null);
            alert.setContentText("Reponse updated successfully!");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}