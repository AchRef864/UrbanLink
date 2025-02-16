package tn.esprit.jdbc.controllers;

import javafx.stage.Stage;
import tn.esprit.jdbc.entities.User;
import tn.esprit.jdbc.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterUserController {

    @FXML
    private TextField nomTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField phoneTextField;



    @FXML
    void ajouteUserAction(ActionEvent event) {
        String nom = nomTextField.getText();
        String email = emailTextField.getText();
        String phone = phoneTextField.getText();

        // Use the correct constructor
        User user = new User(nom, email, phone, null);

        UserService serviceUser = new UserService();
        try {
            serviceUser.insert(user);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("User ajouté avec succès");
            alert.showAndWait();

            // Load the Detail.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Detail.fxml"));
            Parent root = loader.load();

            DetailController detailController = loader.getController();
            // from the Detail controller
            detailController.setNomTextField(nom);
            detailController.setEmailTextField(email);
            detailController.setPhoneTextField(phone) ;
            //Test field --+
            nomTextField.getScene().setRoot(root);

        } catch (SQLException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de l'ajout de l'utilisateur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            System.err.println(e.getMessage());
        }
    }
}