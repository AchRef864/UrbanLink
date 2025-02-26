package tn.esprit.jdbc.controller;

import tn.esprit.jdbc.entities.Avis;
import tn.esprit.jdbc.services.AvisService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.util.Callback;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import tn.esprit.jdbc.services.ReponseService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javafx.stage.FileChooser;
import java.io.File;

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
    private TableColumn<Avis, Date> dateAvisColumn;

    @FXML
    private TableColumn<Avis, Integer> userIdColumn;

    @FXML
    private TableColumn<Avis, String> userEmailColumn;

    @FXML
    private TableColumn<Avis, Void> editColumn;

    @FXML
    private TableColumn<Avis, Void> deleteColumn;

    @FXML
    private TableColumn<Avis, Void> viewReponsesColumn;

    @FXML
    private Button exportPdfButton;

    private AvisService avisService = new AvisService();
    private ReponseService reponseService = new ReponseService();

    @FXML
    public void initialize() {
        avisIdColumn.setCellValueFactory(new PropertyValueFactory<>("avis_id"));
        commentaireColumn.setCellValueFactory(new PropertyValueFactory<>("commentaire"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
        dateAvisColumn.setCellValueFactory(new PropertyValueFactory<>("date_avis"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("user_id"));

        // Set custom cell factory for editColumn
        editColumn.setCellFactory(new Callback<TableColumn<Avis, Void>, TableCell<Avis, Void>>() {
            @Override
            public TableCell<Avis, Void> call(final TableColumn<Avis, Void> param) {
                final TableCell<Avis, Void> cell = new TableCell<Avis, Void>() {

                    private final Button btn = new Button("Edit");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Avis avis = getTableView().getItems().get(getIndex());
                            editAvis(avis);
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

        // Set custom cell factory for deleteColumn
        deleteColumn.setCellFactory(new Callback<TableColumn<Avis, Void>, TableCell<Avis, Void>>() {
            @Override
            public TableCell<Avis, Void> call(final TableColumn<Avis, Void> param) {
                final TableCell<Avis, Void> cell = new TableCell<Avis, Void>() {

                    private final Button btn = new Button("Delete");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Avis avis = getTableView().getItems().get(getIndex());
                            deleteAvis(avis);
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

        // Set custom cell factory for viewReponsesColumn
        viewReponsesColumn.setCellFactory(new Callback<TableColumn<Avis, Void>, TableCell<Avis, Void>>() {
            @Override
            public TableCell<Avis, Void> call(final TableColumn<Avis, Void> param) {
                final TableCell<Avis, Void> cell = new TableCell<Avis, Void>() {

                    private final Button btn = new Button("View Reponses");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Avis avis = getTableView().getItems().get(getIndex());
                            viewReponses(avis);
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

        loadAvisData();
    }

    public void loadAvisData() {
        try {
            List<Avis> avisList = avisService.showAll();
            ObservableList<Avis> avisObservableList = FXCollections.observableArrayList(avisList);
            avisTableView.setItems(avisObservableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void createAvisAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/addAvis.fxml"));
            Parent parent = fxmlLoader.load();
            AddAvisController controller = fxmlLoader.getController();
            controller.setAvisTableController(this); // Pass the current controller to AddAvisController
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add New Avis");
            stage.setScene(new Scene(parent));
            stage.showAndWait();
            loadAvisData(); // Refresh the table after adding a new Avis
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editAvis(Avis avis) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/updateAvis.fxml"));
            Parent parent = fxmlLoader.load();
            UpdateAvisController controller = fxmlLoader.getController();
            controller.setAvis(avis); // Pass the selected Avis to the update controller
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Avis");
            stage.setScene(new Scene(parent));
            stage.showAndWait();
            loadAvisData(); // Refresh the table after editing the Avis
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteAvis(Avis avis) {
        try {
            avisService.delete(avis);
            loadAvisData();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete Avis");
            alert.setHeaderText(null);
            alert.setContentText("Deleted review");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void viewReponses(Avis avis) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/reponseTable.fxml"));
            Parent parent = fxmlLoader.load();
            ReponseTableController controller = fxmlLoader.getController();
            controller.setAvisId(avis.getAvis_id()); // Pass the selected Avis ID to the ReponseTableController
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("View Reponses");
            stage.setScene(new Scene(parent));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void exportToPdf(ActionEvent event) {
        Document document = new Document();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(exportPdfButton.getScene().getWindow());

        if (file != null) {
            try {
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
                Paragraph title = new Paragraph("Avis Table", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                document.add(new Paragraph("\n"));

                PdfPTable table = new PdfPTable(4);
                table.setWidthPercentage(100);

                addTableHeader(table);
                addRows(table);

                document.add(table);

                showInfoAlert("PDF Generated", "The PDF file has been successfully created: " + file.getAbsolutePath());

            } catch (Exception e) {
                showErrorAlert("PDF Generation Error", "An error occurred while generating the PDF.");
                e.printStackTrace();
            } finally {
                document.close();
            }
        }
    }

    private void addTableHeader(PdfPTable table) {
        table.addCell("Avis ID");
        table.addCell("Commentaire");
        table.addCell("Note");
        table.addCell("Date Avis");
    }

    private void addRows(PdfPTable table) {
        for (Avis avis : avisTableView.getItems()) {
            table.addCell(String.valueOf(avis.getAvis_id()));
            table.addCell(avis.getCommentaire());
            table.addCell(String.valueOf(avis.getNote()));
            table.addCell(avis.getDate_avis().toString());
        }
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}