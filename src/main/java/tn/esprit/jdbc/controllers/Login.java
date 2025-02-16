package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class Login {
    @FXML
    private TextField nomTextField;

    @FXML
    private TextField prenomTextField;

    public void setNomTextField(String nom) {
        nomTextField.setText(nom);
    }

    public void setPrenomTextField(String prenom) {
        prenomTextField.setText(prenom);
    }
}