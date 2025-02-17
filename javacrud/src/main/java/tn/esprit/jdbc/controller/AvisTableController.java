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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.TableCell;

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
    private TableColumn<Avis, Void> editColumn;

    @FXML
    private TableColumn<Avis, Void> deleteColumn;

    private AvisService avisService = new AvisService();

    @FXML
    public void initialize() {
        avisIdColumn.setCellValueFactory(new PropertyValueFactory<>("avis_id"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
        commentaireColumn.setCellValueFactory(new PropertyValueFactory<>("commentaire"));
        dateAvisColumn.setCellValueFactory(new PropertyValueFactory<>("date_avis"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("user_id"));

        addEditButtonToTable();
        addDeleteButtonToTable();

        loadAvisData();
    }

    public void loadAvisData() {
        try {
            List<Avis> avisList = avisService.showAll();
            ObservableList<Avis> avisObservableList = FXCollections.observableArrayList(avisList);
            avisTableView.setItems(avisObservableList);
        } catch (SQLException e) {
            showErrorAlert("Error loading data", e.getMessage());
        }
    }

    private void addEditButtonToTable() {
        Callback<TableColumn<Avis, Void>, TableCell<Avis, Void>> cellFactory = new Callback<TableColumn<Avis, Void>, TableCell<Avis, Void>>() {
            @Override
            public TableCell<Avis, Void> call(final TableColumn<Avis, Void> param) {
                final TableCell<Avis, Void> cell = new TableCell<Avis, Void>() {

                    private final Button editButton = new Button("Edit");

                    {
                        editButton.setStyle("-fx-background-color: yellow; -fx-text-fill: black;");
                        editButton.setOnAction((ActionEvent event) -> {
                            Avis data = getTableView().getItems().get(getIndex());
                            updateAvis(data);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(editButton);
                        }
                    }
                };
                return cell;
            }
        };

        editColumn.setCellFactory(cellFactory);
    }

    private void addDeleteButtonToTable() {
        Callback<TableColumn<Avis, Void>, TableCell<Avis, Void>> cellFactory = new Callback<TableColumn<Avis, Void>, TableCell<Avis, Void>>() {
            @Override
            public TableCell<Avis, Void> call(final TableColumn<Avis, Void> param) {
                final TableCell<Avis, Void> cell = new TableCell<Avis, Void>() {

                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                        deleteButton.setOnAction((ActionEvent event) -> {
                            Avis data = getTableView().getItems().get(getIndex());
                            deleteAvis(data);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }
                };
                return cell;
            }
        };

        deleteColumn.setCellFactory(cellFactory);
    }

    private void updateAvis(Avis avis) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateAvis.fxml"));
            Parent root = loader.load();

            // Pass the selected Avis to the update controller
            UpdateAvisController controller = loader.getController();
            controller.setAvis(avis);

            Stage stage = new Stage();
            stage.setTitle("Update Avis");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Error", "Unable to load updateAvis.fxml");
        }
    }

    public void deleteAvis(Avis avis) {
        try {
            avisService.delete(avis);
            loadAvisData();
            showInfoAlert("Avis deleted successfully", null);
        } catch (SQLException e) {
            showErrorAlert("Error deleting Avis", e.getMessage());
        }
    }

    @FXML
    private void createAvisAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addAvis.fxml"));
            Parent root = loader.load();

            // Pass the current controller to the AddAvisController
            AddAvisController addAvisController = loader.getController();
            addAvisController.setAvisTableController(this);

            Stage stage = new Stage();
            stage.setTitle("Add Avis");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Error", "Unable to load addAvis.fxml");
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