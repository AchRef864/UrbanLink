package tn.esprit.jdbc.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

<<<<<<< Updated upstream


=======
>>>>>>> Stashed changes
public class HelloAdminController {
    @FXML
    public void OpenInsertPageAction(ActionEvent event) {
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
    public void OpenEditPageAction(ActionEvent event) {
        try {
            // Load the Insert page
            Parent root = FXMLLoader.load(getClass().getResource("/EditUser.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

<<<<<<< Updated upstream



=======
>>>>>>> Stashed changes
}