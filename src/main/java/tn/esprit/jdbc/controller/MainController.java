package tn.esprit.jdbc.controller;

import tn.esprit.jdbc.entities.Avis;
import tn.esprit.jdbc.services.AvisService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MainController {

    @FXML
    private TableView<Avis> avisTableView;

    @FXML
    private TableColumn<Avis, Integer> avisIdColumn;

    @FXML
    private TableColumn<Avis, Integer> noteColumn;

    @FXML
    private TableColumn<Avis, String> commentaireColumn;

    @FXML
    private TableColumn<Avis, java.util.Date> dateAvisColumn;

    @FXML
    private TableColumn<Avis, Integer> userIdColumn;

    private AvisService avisService = new AvisService();

    @FXML
    public void initialize() {
        avisIdColumn.setCellValueFactory(new PropertyValueFactory<>("avis_id"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
        commentaireColumn.setCellValueFactory(new PropertyValueFactory<>("commentaire"));
        dateAvisColumn.setCellValueFactory(new PropertyValueFactory<>("date_avis"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("user_id"));

        loadAvisData();
    }

    private void loadAvisData() {
        try {
            List<Avis> avisList = avisService.showAll();
            ObservableList<Avis> avisObservableList = FXCollections.observableArrayList(avisList);
            avisTableView.setItems(avisObservableList);
        } catch (SQLException e) {
            showErrorAlert("Error loading data", e.getMessage());
        }
    }

    @FXML
    void createAvisAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addAvis.fxml"));
            Parent root = loader.load();
            avisTableView.getScene().setRoot(root);
        } catch (IOException e) {
            showErrorAlert("Error loading create interface", e.getMessage());
        }
    }

    @FXML
    void updateAvisAction(ActionEvent event) {
        Avis selectedAvis = avisTableView.getSelectionModel().getSelectedItem();
        if (selectedAvis != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateAvis.fxml"));
                Parent root = loader.load();
                UpdateAvisController controller = loader.getController();
                controller.setAvis(selectedAvis);
                avisTableView.getScene().setRoot(root);
            } catch (IOException e) {
                showErrorAlert("Error loading update interface", e.getMessage());
            }
        } else {
            showErrorAlert("No selection", "Please select a review to update.");
        }
    }

    @FXML
    void deleteAvisAction(ActionEvent event) {
        Avis selectedAvis = avisTableView.getSelectionModel().getSelectedItem();
        if (selectedAvis != null) {
            try {
                avisService.delete(selectedAvis);
                loadAvisData();
                showInfoAlert("Success", "Review deleted successfully.");
            } catch (SQLException e) {
                showErrorAlert("Error deleting review", e.getMessage());
            }
        } else {
            showErrorAlert("No selection", "Please select " +
                    "" +
                    "+a review to delete.");
        }
    }

    private void showErrorAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfoAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}