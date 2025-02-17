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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.util.Callback;

import java.io.IOException;
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

    @FXML
    private TableColumn<Reponse, Void> editColumn;

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

        // Set custom cell factory for editColumn
        editColumn.setCellFactory(new Callback<TableColumn<Reponse, Void>, TableCell<Reponse, Void>>() {
            @Override
            public TableCell<Reponse, Void> call(final TableColumn<Reponse, Void> param) {
                final TableCell<Reponse, Void> cell = new TableCell<Reponse, Void>() {

                    private final Button btn = new Button("Edit");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Reponse reponse = getTableView().getItems().get(getIndex());
                            editReponse(reponse);
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
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/addReponse.fxml"));
            Parent parent = fxmlLoader.load();
            AddReponseController controller = fxmlLoader.getController();
            controller.setAvisId(avisId); // Pass the current Avis ID to AddReponseController
            controller.setReponseTableController(this); // Pass the current controller to AddReponseController
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add New Reponse");
            stage.setScene(new Scene(parent));
            stage.showAndWait();
            loadReponsesData(); // Refresh the table after adding a new Reponse
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void editReponse(Reponse reponse) {
        // Implement the logic to edit the selected Reponse
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/updateReponse.fxml"));
            Parent parent = fxmlLoader.load();
            UpdateReponseController controller = fxmlLoader.getController();
            controller.setReponse(reponse); // Pass the selected Reponse to the UpdateReponseController
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Reponse");
            stage.setScene(new Scene(parent));
            stage.showAndWait();
            loadReponsesData(); // Refresh the table after editing the Reponse
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}