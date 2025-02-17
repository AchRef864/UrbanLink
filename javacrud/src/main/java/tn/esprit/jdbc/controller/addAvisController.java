package tn.esprit.jdbc.controller;

import tn.esprit.jdbc.entities.Avis;
import tn.esprit.jdbc.services.AvisService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.Date;

public class AddAvisController {

    @FXML
    private TextField commentaireTextField;

    @FXML
    private TextField noteTextField;

    @FXML
    private TextField userIdTextField;

    private AvisTableController avisTableController;

    public void setAvisTableController(AvisTableController avisTableController) {
        this.avisTableController = avisTableController;
    }

    @FXML
    void ajouteAvisAction(ActionEvent event) {
        String commentaire = commentaireTextField.getText();
        int note = Integer.parseInt(noteTextField.getText());
        int userId = Integer.parseInt(userIdTextField.getText());

        Avis avis = new Avis(note, commentaire, new Date(), userId);

        AvisService avisService = new AvisService();
        try {
            avisService.insert(avis);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Review added successfully");
            alert.showAndWait();

            // Reload the table data
            avisTableController.loadAvisData();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error adding review");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            System.err.println(e.getMessage());
        }
    }
}