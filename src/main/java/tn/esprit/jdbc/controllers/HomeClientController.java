package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HomeClientController {

    @FXML
    private Label welcomeLabel; // Optional: Add this if you want to dynamically set the welcome message

    @FXML
    public void initialize() {
        // Set a welcome message (optional)
        welcomeLabel.setText("Hello, Client! Welcome to the Dashboard.");
    }
}