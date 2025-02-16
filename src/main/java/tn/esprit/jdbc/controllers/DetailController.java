package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class DetailController {

    @FXML
    private TextField nomTextField;

    @FXML
    private TextField prenomTextField;

    public void setNomTextField(String nom) {
        this.nomTextField.setText(nom);
    }

    public void setPrenomTextField(String prenom) {
        this.prenomTextField.setText(prenom);
    }
}