package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

public class HomeController {

    @FXML
    private DatePicker datePicker; // This must match the fx:id in the FXML file

    @FXML
    public void initialize() {
        System.out.println("Initializing HomeController...");
        if (datePicker == null) {
            System.err.println("DatePicker is null. Check fx:id in FXML.");
        } else {
            System.out.println("DatePicker initialized successfully.");
            datePicker.setValue(java.time.LocalDate.now());
        }
    }
}