package tn.esprit.jdbc.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloAdminController {

    @FXML
    private void OpenInsertPageAction(ActionEvent event) {
        try {
            // Load the Insert page
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterUser.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void OpenEditPageAction(ActionEvent event) {
        try {
            // Load the Edit page
            Parent root = FXMLLoader.load(getClass().getResource("/EditUser.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void OpenReclamationResponsePageAction(ActionEvent event) {
        try {
            // Load the Reclamation Response page
            Parent root = FXMLLoader.load(getClass().getResource("/AdminReclamationResponse.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}