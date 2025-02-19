package tn.esprit.jdbc.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import tn.esprit.jdbc.entities.Avis;
import tn.esprit.jdbc.services.AvisService;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
// scene import
import javafx.scene.Scene;
import javafx.scene.Parent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;


public class UserAvisController {
    @FXML
    private TableView<Avis> tableAvis;
    @FXML
    private TableColumn<Avis, Integer> colId;
    @FXML
    private TableColumn<Avis, Integer> colNote;
    @FXML
    private TableColumn<Avis, String> colCommentaire;
    @FXML
    private TableColumn<Avis, Date> colDate;
    @FXML
    private TableColumn<Avis, Integer> colUserId;
    @FXML
    private TableColumn<Avis, Void> colEdit;
    @FXML
    private TableColumn<Avis, Void> colDelete;
    @FXML
    private TextField txtNote;
    @FXML
    private TextField txtCommentaire;
    @FXML
    private TextField txtUserId;

    private final AvisService avisService = new AvisService();
    private final ObservableList<Avis> avisList = FXCollections.observableArrayList();
    private int loggedInUserId; // To store the logged-in user's ID

    // Setter for loggedInUserId
    public void setLoggedInUserId(int loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAvis_id()).asObject());
        colNote.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNote()).asObject());
        colCommentaire.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCommentaire()));
        colDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate_avis()));
        colUserId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getUser_id()).asObject());

        // Add Edit and Delete buttons to the table
        addEditButton();
        addDeleteButton();

        loadAvis();
    }

    private void loadAvis() {
        avisList.clear();
        try {
            avisList.addAll(avisService.showAll());
            tableAvis.setItems(avisList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addEditButton() {
        colEdit.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");

            {
                editButton.setOnAction(event -> {
                    Avis avis = getTableView().getItems().get(getIndex());
                    if (avis.getUser_id() == loggedInUserId) { // Only allow editing if the review belongs to the logged-in user
                        editAvis(avis);
                    } else {
                        showAlert("Permission Denied", "You can only edit your own reviews.");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Avis avis = getTableView().getItems().get(getIndex());
                    if (avis.getUser_id() == loggedInUserId) { // Only show the button for the logged-in user's reviews
                        setGraphic(editButton);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
    }

    private void addDeleteButton() {
        colDelete.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Avis avis = getTableView().getItems().get(getIndex());
                    if (avis.getUser_id() == loggedInUserId) { // Only allow deletion if the review belongs to the logged-in user
                        deleteAvis(avis);
                    } else {
                        showAlert("Permission Denied", "You can only delete your own reviews.");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Avis avis = getTableView().getItems().get(getIndex());
                    if (avis.getUser_id() == loggedInUserId) { // Only show the button for the logged-in user's reviews
                        setGraphic(deleteButton);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
    }

    private void editAvis(Avis avis) {
        // Open a form to edit the review
        // You can implement this based on your requirements
        System.out.println("Editing review: " + avis.getAvis_id());
    }

    private void deleteAvis(Avis avis) {
        try {
            avisService.delete(avis);
            loadAvis(); // Refresh the table after deletion
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addAvis() {
        try {
            int note = Integer.parseInt(txtNote.getText());
            String commentaire = txtCommentaire.getText();
            int userId = Integer.parseInt(txtUserId.getText());
            Avis avis = new Avis(note, commentaire, new Date(), userId);
            avisService.insert(avis);
            loadAvis();
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openUpdateAvisPage(Avis avis) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/updateAvis.fxml"));
            Parent root = loader.load();

            // Pass the selected review to the UpdateAvisController
            UpdateAvisController updateAvisController = loader.getController();
            updateAvisController.setAvis(avis);

            // Show the updateAvis page
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Review");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}