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
import javafx.scene.Scene;
import javafx.scene.Parent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.stream.Collectors;

public class UserAvisController {
    @FXML
    private TableView<Avis> tableAvis;
    @FXML
    private TableColumn<Avis, Integer> colNote;
    @FXML
    private TableColumn<Avis, String> colCommentaire;
    @FXML
    private TableColumn<Avis, Date> colDate;
    @FXML
    private TableColumn<Avis, Void> colEdit;
    @FXML
    private TableColumn<Avis, Void> colDelete;
    @FXML
    private TableColumn<Avis, Void> colViewResponses;
    @FXML
    private TextField txtNote;
    @FXML
    private TextField txtCommentaire;
    @FXML
    private TextField txtUserId;
    @FXML
    private TextField searchTextField;

    private final AvisService avisService = new AvisService();
    private final ObservableList<Avis> avisList = FXCollections.observableArrayList();
    private int loggedInUserId; // To store the logged-in user's ID

    // Setter for loggedInUserId
    public void setLoggedInUserId(int loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
    }

    @FXML
    public void initialize() {
        colNote.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNote()).asObject());
        colCommentaire.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCommentaire()));
        colDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate_avis()));

        // Add Edit, Delete, and View Responses buttons to the table
        addEditButton();
        addDeleteButton();
        addViewResponsesButton();

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

    @FXML
    private void searchAvis() {
        String searchText = searchTextField.getText().toLowerCase();
        ObservableList<Avis> filteredList = avisList.stream()
                .filter(avis -> avis.getCommentaire().toLowerCase().contains(searchText))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        tableAvis.setItems(filteredList);
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

    private void addViewResponsesButton() {
        colViewResponses.setCellFactory(param -> new TableCell<>() {
            private final Button viewResponsesButton = new Button("View Responses");

            {
                viewResponsesButton.setOnAction(event -> {
                    Avis avis = getTableView().getItems().get(getIndex());
                    viewResponses(avis);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(viewResponsesButton);
                }
            }
        });
    }

    private void viewResponses(Avis avis) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserReponseTable.fxml"));
            Parent root = loader.load();

            UserReponseTableController userReponseTableController = loader.getController();
            userReponseTableController.setAvisId(avis.getAvis_id());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("View Responses");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editAvis(Avis avis) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateAvis.fxml"));
            Parent root = loader.load();

            UpdateAvisController updateAvisController = loader.getController();
            updateAvisController.setAvis(avis);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Review");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load updateAvis.fxml: " + e.getMessage());
        }
    }

    private void deleteAvis(Avis avis) {
        try {
            avisService.delete(avis);
            loadAvis();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addAvis() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addAvis.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Adding Page");
            stage.show();

            int note = Integer.parseInt(txtNote.getText());
            String commentaire = txtCommentaire.getText();
            int userId = Integer.parseInt(txtUserId.getText());
            Avis avis = new Avis(note, commentaire, new Date(), userId);
            avisService.insert(avis);

            loadAvis();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load addAvis.fxml: " + e.getMessage());
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
}