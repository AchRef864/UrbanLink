package tn.esprit.jdbc.controllers;

import tn.esprit.jdbc.entities.Reponse;
import tn.esprit.jdbc.services.ReponseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserReponseTableController {

    private int avisId;

    public void setAvisId(int avisId) {
        this.avisId = avisId;
        loadReponsesData(); // Load responses after setting the avisId
    }

    @FXML
    private TableView<Reponse> reponseTableView;

    @FXML
    private TableColumn<Reponse, String> commentaireColumn;

    @FXML
    private TableColumn<Reponse, java.util.Date> dateReponseColumn;

    private ReponseService reponseService = new ReponseService();

    @FXML
    public void initialize() {
        commentaireColumn.setCellValueFactory(new PropertyValueFactory<>("commentaire"));
        dateReponseColumn.setCellValueFactory(new PropertyValueFactory<>("date_reponse"));
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

    @FXML
    private void createReponseAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/addReponse.fxml"));
            Parent parent = fxmlLoader.load();
            AddReponseController controller = fxmlLoader.getController();
            controller.setAvisId(avisId); // Pass the avisId to the AddReponseController
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add New Response");
            stage.setScene(new Scene(parent));
            stage.showAndWait();
            loadReponsesData(); // Refresh the table after adding a new response
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}