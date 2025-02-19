package tn.esprit.jdbc.controller;

import tn.esprit.jdbc.entities.Reponse;
import tn.esprit.jdbc.services.ReponseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

public class ReponseTableController {


    private int avisId;

    public void setAvisId(int avisId) {
        this.avisId = avisId;
    }

    @FXML
    private TableView<Reponse> reponseTableView;

    @FXML
    private TableColumn<Reponse, Integer> reponseIdColumn;

    @FXML
    private TableColumn<Reponse, String> commentaireColumn;

    @FXML
    private TableColumn<Reponse, java.util.Date> dateReponseColumn;

    @FXML
    private TableColumn<Reponse, Integer> avisIdColumn;

    @FXML
    private TableColumn<Reponse, Integer> userIdColumn;

    private ReponseService reponseService = new ReponseService();



    @FXML
    public void initialize() {
        reponseIdColumn.setCellValueFactory(new PropertyValueFactory<>("reponse_id"));
        commentaireColumn.setCellValueFactory(new PropertyValueFactory<>("commentaire"));
        dateReponseColumn.setCellValueFactory(new PropertyValueFactory<>("date_reponse"));
        avisIdColumn.setCellValueFactory(new PropertyValueFactory<>("avis_id"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("user_id"));
    }

    public void loadReponsesData() {
        try {
            List<Reponse> reponseList = reponseService.getReponsesByAvisId(avisId);
            ObservableList<Reponse> reponseObservableList = FXCollections.observableArrayList(reponseList);
            reponseTableView.setItems(reponseObservableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}