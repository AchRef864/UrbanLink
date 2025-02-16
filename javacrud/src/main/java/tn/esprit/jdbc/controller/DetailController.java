package tn.esprit.jdbc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class DetailController {

    @FXML
    private TextField commentaireTextField;

    @FXML
    private TextField noteTextField;

    public void setCommentaireTextField(String commentaire) {
        this.commentaireTextField.setText(commentaire);
    }

    public void setNoteTextField(String note) {
        this.noteTextField.setText(note);
    }
}