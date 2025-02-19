package tn.esprit.jdbc.controller;

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
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ReponseTableController {

    private int avisId;

    public void setAvisId(int avisId) {
        this.avisId = avisId;
        loadReponsesData(); // Load responses after setting the avisId
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

    @FXML
    private TableColumn<Reponse, Void> editColumn;

    @FXML
    private TableColumn<Reponse, Void> deleteColumn;

    private ReponseService reponseService = new ReponseService();

    @FXML
    public void initialize() {
        reponseIdColumn.setCellValueFactory(new PropertyValueFactory<>("reponse_id"));
        commentaireColumn.setCellValueFactory(new PropertyValueFactory<>("commentaire"));
        dateReponseColumn.setCellValueFactory(new PropertyValueFactory<>("date_reponse"));
        avisIdColumn.setCellValueFactory(new PropertyValueFactory<>("avis_id"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("user_id"));

        setCellFactory(editColumn, "Edit", reponse -> {
            editReponse(reponse);
            return null;
        });

        setCellFactory(deleteColumn, "Delete", reponse -> {
            deleteReponse(reponse);
            return null;
        });
    }

    private void setCellFactory(TableColumn<Reponse, Void> column, String buttonText, Callback<Reponse, Void> action) {
        column.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button(buttonText);

            {
                btn.setOnAction(event -> action.call(getTableView().getItems().get(getIndex())));
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
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

    private void editReponse(Reponse reponse) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/updateReponse.fxml"));
            Parent parent = fxmlLoader.load();
            UpdateReponseController controller = fxmlLoader.getController();
            controller.setReponse(reponse); // Pass the reponse to the UpdateReponseController
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Response");
            stage.setScene(new Scene(parent));
            stage.showAndWait();
            loadReponsesData(); // Refresh the table after editing the response
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteReponse(Reponse reponse) {
        try {
            reponseService.delete(reponse);
            loadReponsesData();
            showAlert(Alert.AlertType.INFORMATION, "Delete Response", "Deleted Response ID: " + reponse.getReponse_id());
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