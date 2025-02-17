package tn.esprit.jdbc.controller;

import tn.esprit.jdbc.entities.Reponse;
import tn.esprit.jdbc.services.ReponseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.List;

public class ReponseTableController {

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
    private TableColumn<Reponse, Void> deleteColumn;

    private ReponseService reponseService = new ReponseService();
    private int avisId;

    public void setAvisId(int avisId) {
        this.avisId = avisId;
        loadReponsesData();
    }

    @FXML
    public void initialize() {
        reponseIdColumn.setCellValueFactory(new PropertyValueFactory<>("reponse_id"));
        commentaireColumn.setCellValueFactory(new PropertyValueFactory<>("commentaire"));
        dateReponseColumn.setCellValueFactory(new PropertyValueFactory<>("date_reponse"));
        avisIdColumn.setCellValueFactory(new PropertyValueFactory<>("avis_id"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("user_id"));

        // Set custom cell factory for deleteColumn
        deleteColumn.setCellFactory(new Callback<TableColumn<Reponse, Void>, TableCell<Reponse, Void>>() {
            @Override
            public TableCell<Reponse, Void> call(final TableColumn<Reponse, Void> param) {
                final TableCell<Reponse, Void> cell = new TableCell<Reponse, Void>() {

                    private final Button btn = new Button("Delete");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Reponse reponse = getTableView().getItems().get(getIndex());
                            deleteReponse(reponse);
                        });
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
                };
                return cell;
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

    @FXML
    private void createReponseAction(ActionEvent event) {
        // Implement the logic for creating a new Reponse
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Create Reponse");
        alert.setHeaderText(null);
        alert.setContentText("Create Reponse action triggered!");
        alert.showAndWait();
    }

    private void deleteReponse(Reponse reponse) {
        // Implement the logic to delete the selected Reponse
        try {
            reponseService.delete(reponse);
            loadReponsesData();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete Reponse");
            alert.setHeaderText(null);
            alert.setContentText("Deleted Reponse ID: " + reponse.getReponse_id());
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}