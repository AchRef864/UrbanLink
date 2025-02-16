package tn.esprit.jdbc.controller;

import tn.esprit.jdbc.entities.Avis;
import tn.esprit.jdbc.services.AvisService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AvisTableController {

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

    @FXML
    private ComboBox<Avis> deleteAvisComboBox;

    private AvisService avisService = new AvisService();

    @FXML
    public void initialize() {
        avisIdColumn.setCellValueFactory(new PropertyValueFactory<>("avis_id"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
        commentaireColumn.setCellValueFactory(new PropertyValueFactory<>("commentaire"));
        dateAvisColumn.setCellValueFactory(new PropertyValueFactory<>("date_avis"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("user_id"));

        loadAvisData();
        loadAvisComboBox();
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

    private void loadAvisComboBox() {
        try {
            List<Avis> avisList = avisService.showAll();
            ObservableList<Avis> avisObservableList = FXCollections.observableArrayList(avisList);
            deleteAvisComboBox.setItems(avisObservableList);
        } catch (SQLException e) {
            showErrorAlert("Error loading data", e.getMessage());
        }
    }

    @FXML
    private void createAvisAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/addAvis.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add Avis");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Error", "Unable to load addAvis.fxml");
        }
    }

    @FXML
    private void addAvisAction(ActionEvent event) {
        // Implement the logic to add a new Avis
        showErrorAlert("Add Avis", "This feature is not yet implemented.");
    }

    @FXML
    private void updateAvisAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/updateAvis.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Update Avis");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Error", "Unable to load updateAvis.fxml");
        }
    }

    @FXML
    private void deleteAvisAction(ActionEvent event) {
        Avis selectedAvis = deleteAvisComboBox.getSelectionModel().getSelectedItem();
        if (selectedAvis != null) {
            try {
                avisService.delete(selectedAvis);
                loadAvisData();
                loadAvisComboBox();
                showInfoAlert("Avis deleted successfully", null);
            } catch (SQLException e) {
                showErrorAlert("Error deleting Avis", e.getMessage());
            }
        } else {
            showErrorAlert("No Avis selected", "Please select an Avis to delete.");
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