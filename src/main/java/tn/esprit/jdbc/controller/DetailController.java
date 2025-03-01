package tn.esprit.jdbc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;


public class DetailController {

    @FXML
    private TextField nomTextField;

    public void setNomTextField(String nom) {
        this.nomTextField.setText(nom);
    }


    @FXML
    private TextField phoneTextField;

    public void setEmailTextField(String email){
        this.emailTextField.setText(email);
    }

    @FXML
    private TextField emailTextField;

    public void setPhoneTextField(String phone ){
        this.phoneTextField.setText(phone);
    }


    @FXML
    private TextField passwordTextField;
   private void setPasswordTextField(String password)
    {
        this.passwordTextField.setText(password) ;
    }


}


