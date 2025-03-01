package tn.esprit.jdbc.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import tn.esprit.jdbc.entities.Rating;
import tn.esprit.jdbc.services.RatingService;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ViewRatingsController implements Initializable {

    @FXML
    private TableView<Rating> ratingsTable;

    private final RatingService ratingService = new RatingService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            List<Rating> ratings = ratingService.getAllRatings();
            ratingsTable.getItems().addAll(ratings);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}