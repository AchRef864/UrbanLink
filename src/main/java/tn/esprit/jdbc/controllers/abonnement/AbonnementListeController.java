package tn.esprit.jdbc.controllers.abonnement;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import tn.esprit.jdbc.entities.abonnement;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import tn.esprit.jdbc.services.abonnementservices;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class AbonnementListeController {

    @FXML
    private TableView<abonnement> tableAbonnements;
    @FXML
    private TableColumn<abonnement, Integer> colId;
    @FXML
    private TableColumn<abonnement, String> colType;
    @FXML
    private TableColumn<abonnement, Double> colPrix;
    @FXML
    private TableColumn<abonnement, String> colDateDebut;
    @FXML
    private TableColumn<abonnement, String> colDateFin;
    @FXML
    private TableColumn<abonnement, String> colEtat;
    @FXML
    private TableColumn<abonnement, Void> colAction;
    @FXML
    private TextField tfSearch;
    @FXML
    private ComboBox<String> cbTri;
    @FXML
    private Button btnGeneratePdf;

    private abonnementservices abonnementService;
    private List<abonnement> allAbonnements = new ArrayList<>();

    public AbonnementListeController() {
        abonnementService = new abonnementservices();
    }

    @FXML
    public void initialize() throws SQLException {
        // Initialisation des colonnes pour le tableau
        colId.setCellValueFactory(cellData -> new javafx.beans.binding.ObjectBinding<Integer>() {
            @Override
            protected Integer computeValue() {
                return cellData.getValue().getid();
            }
        });
        colType.setCellValueFactory(cellData -> new javafx.beans.binding.ObjectBinding<String>() {
            @Override
            protected String computeValue() {
                return cellData.getValue().gettype();
            }
        });
        colPrix.setCellValueFactory(cellData -> new javafx.beans.binding.ObjectBinding<Double>() {
            @Override
            protected Double computeValue() {
                return cellData.getValue().getprix();
            }
        });
        colDateDebut.setCellValueFactory(cellData -> new javafx.beans.binding.ObjectBinding<String>() {
            @Override
            protected String computeValue() {
                return cellData.getValue().getdate_debut().toString();
            }
        });
        colDateFin.setCellValueFactory(cellData -> new javafx.beans.binding.ObjectBinding<String>() {
            @Override
            protected String computeValue() {
                return cellData.getValue().getdate_fin().toString();
            }
        });
        colEtat.setCellValueFactory(cellData -> new javafx.beans.binding.ObjectBinding<String>() {
            @Override
            protected String computeValue() {
                return cellData.getValue().getetat();
            }
        });

        // Charger les abonnements depuis la base de données
        loadAbonnements();

        // Initialisation des ComboBox
        cbTri.setItems(FXCollections.observableArrayList("Prix croissant", "Date de fin", "Date de début", "Type", "Etat"));
        cbTri.getSelectionModel().selectFirst();

        // Event listeners
        cbTri.setOnAction(event -> trierAbonnements());
        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> filterAbonnements(newValue));

        // Configuration de la colonne d'actions
        colAction.setCellFactory(new Callback<TableColumn<abonnement, Void>, TableCell<abonnement, Void>>() {
            @Override
            public TableCell<abonnement, Void> call(TableColumn<abonnement, Void> param) {
                return new TableCell<abonnement, Void>() {
                    private final Button editButton = new Button("Editer");
                    private final Button deleteButton = new Button("Supprimer");
                    private final HBox hBox = new HBox(10, editButton, deleteButton);

                    {
                        editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
                        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");

                        editButton.setOnAction(event -> {
                            abonnement abonnement = getTableView().getItems().get(getIndex());
                            modifierAbonnement(abonnement);
                        });

                        deleteButton.setOnAction(event -> {
                            abonnement abonnement = getTableView().getItems().get(getIndex());
                            try {
                                int result = abonnementService.delete(abonnement.getid());
                                if (result > 0) {
                                    showAlert("Succès", "Abonnement supprimé avec succès !");
                                    loadAbonnements();
                                }
                            } catch (SQLException e) {
                                showAlert("Erreur", "Erreur lors de la suppression de l'abonnement.");
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : hBox);
                    }
                };
            }
        });
    }

    // Load all subscriptions
    private void loadAbonnements() throws SQLException {
        allAbonnements = abonnementService.showAll();
        tableAbonnements.setItems(FXCollections.observableArrayList(allAbonnements));
    }

    // Filter by search query
    private void filterAbonnements(String query) {
        if (query == null || query.trim().isEmpty()) {
            tableAbonnements.setItems(FXCollections.observableArrayList(allAbonnements));
        } else {
            List<abonnement> filteredList = allAbonnements.stream()
                    .filter(a -> a.gettype().toLowerCase().contains(query.toLowerCase()) ||
                            a.getetat().toLowerCase().contains(query.toLowerCase()) ||
                            String.valueOf(a.getprix()).contains(query) ||
                            a.getdate_debut().toString().contains(query) ||
                            a.getdate_fin().toString().contains(query) ||
                            String.valueOf(a.getid()).contains(query))
                    .collect(Collectors.toList());
            tableAbonnements.setItems(FXCollections.observableArrayList(filteredList));
        }
    }

    @FXML
    public void rechercherAbonnement() {
        filterAbonnements(tfSearch.getText());
    }

    @FXML
    public void trierAbonnements() {
        String critere = cbTri.getValue();
        List<abonnement> sortedList;

        switch (critere) {
            case "Prix croissant":
                sortedList = allAbonnements.stream()
                        .sorted((a1, a2) -> Double.compare(a1.getprix(), a2.getprix()))
                        .collect(Collectors.toList());
                break;
            case "Date de fin":
                sortedList = allAbonnements.stream()
                        .sorted((a1, a2) -> a1.getdate_fin().compareTo(a2.getdate_fin()))
                        .collect(Collectors.toList());
                break;
            case "Date de début":
                sortedList = allAbonnements.stream()
                        .sorted((a1, a2) -> a1.getdate_debut().compareTo(a2.getdate_debut()))
                        .collect(Collectors.toList());
                break;
            case "Type":
                sortedList = allAbonnements.stream()
                        .sorted((a1, a2) -> a1.gettype().compareToIgnoreCase(a2.gettype()))
                        .collect(Collectors.toList());
                break;
            case "Etat":
                sortedList = allAbonnements.stream()
                        .sorted((a1, a2) -> a1.getetat().compareToIgnoreCase(a2.getetat()))
                        .collect(Collectors.toList());
                break;
            default:
                sortedList = allAbonnements;
                break;
        }
        tableAbonnements.setItems(FXCollections.observableArrayList(sortedList));
    }

    @FXML
    public void retourAccueil() {
        try {
            // Load the ajoutAbonnement.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutAbonnement.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Ajouter un Abonnement");

            // Optionally, make it modal if you want it to block interaction with the previous window
            stage.initModality(Modality.APPLICATION_MODAL);

            // Get the current stage and close it (optional, if you want to replace the current window)
            Stage currentStage = (Stage) btnGeneratePdf.getScene().getWindow();
            currentStage.close();

            // Show the new stage
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du chargement de la page d'ajout d'abonnement : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void generatePdf() {
        // Get the selected abonnement from the TableView
        abonnement selectedAbonnement = tableAbonnements.getSelectionModel().getSelectedItem();

        if (selectedAbonnement == null) {
            showAlert("Erreur", "Veuillez sélectionner un abonnement dans le tableau.");
            return;
        }

        // Prompt user to choose ticket type (Bus or Metro)
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Bus", "Bus", "Metro");
        dialog.setTitle("Type de Ticket");
        dialog.setHeaderText("Choisissez le type de ticket");
        dialog.setContentText("Type:");
        java.util.Optional<String> result = dialog.showAndWait();

        if (!result.isPresent()) {
            showAlert("Annulé", "La génération du ticket a été annulée.");
            return;
        }

        String ticketType = result.get();
        String iconSymbol = ticketType.equals("Bus") ? "🚌" : "🚇"; // Unicode for bus and metro

        // HTML content for the ticket template with simple icon and barcode placeholder
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<!DOCTYPE html>\n")
                .append("<html>\n")
                .append("<head>\n")
                .append("<meta charset=\"UTF-8\"/>\n")
                .append("<style>\n")
                .append("body { margin: 0; padding: 20px; background: #e0e2e8; font-family: 'Open Sans', sans-serif; }\n")
                .append(".ticket { height: 600px; width: 270px; box-shadow: 5px 5px 30px rgba(0, 0, 0, 0.3); border-radius: 25px; margin: auto; }\n")
                .append(".top { height: 220px; background: #d40808; border-top-right-radius: 25px; border-top-left-radius: 25px; position: relative; text-align: center; }\n")
                .append(".top h1 { text-transform: uppercase; font-size: 14px; letter-spacing: 2px; position: absolute; top: 50px; left: 50%; transform: translateX(-50%); color: #000; }\n")
                .append(".icon { font-size: 90px; position: absolute; top: 100px; left: 50%; transform: translateX(-50%); }\n")
                .append(".bottom { height: 380px; background: #fff; border-bottom-right-radius: 25px; border-bottom-left-radius: 25px; }\n")
                .append(".bottom p { display: flex; flex-direction: column; font-size: 13px; font-weight: 700; }\n")
                .append(".bottom p span { font-weight: 400; font-size: 11px; color: #6c6c6c; }\n")
                .append(".column { margin: 0 auto; width: 80%; padding: 2rem 0; }\n")
                .append(".row { display: flex; justify-content: space-between; }\n")
                .append(".row--right { text-align: right; }\n")
                .append(".row--center { text-align: center; }\n")
                .append(".row-2 { margin: 30px 0 60px 0; position: relative; }\n")
                .append(".row-2::after { content: ''; position: absolute; width: 100%; bottom: -30px; left: 0; background: #000; height: 1px; }\n")
                .append(".barcode { text-align: center; margin-top: 20px; }\n")
                .append(".barcode img { width: 30%; height: auto; }\n")
                .append("</style>\n")
                .append("</head>\n")
                .append("<body>\n")
                .append("<div class=\"ticket\">\n")
                .append("<div class=\"top\">\n")
                .append("<h1>").append(ticketType).append(" Ticket</h1>\n")
                .append("<div class=\"icon\">").append(iconSymbol).append("</div>\n")
                .append("<h1> TUN </h1>\n")
                .append("</div>\n")
                .append("<div class=\"bottom\">\n")
                .append("<div class=\"column\">\n")
                .append("<div class=\"row row-1\">\n")
                .append("<p><span>NUM</span>").append(selectedAbonnement.getid()).append("</p>\n")
                .append("<p class=\"row--right\"><span>Type</span>").append(selectedAbonnement.gettype()).append("</p>\n")
                .append("</div>\n")
                .append("<div class=\"row row-2\">\n")
                .append("<p><span>Date Début</span>").append(selectedAbonnement.getdate_debut().toString()).append("</p>\n")
                .append("<p class=\"row--center\"><span>Date Fin</span>").append(selectedAbonnement.getdate_fin().toString()).append("</p>\n")
                .append("</div>\n")
                .append("<p class=\"row--right\"><span>Prix</span>").append(String.format("%.2f", selectedAbonnement.getprix())).append(" TND</p>\n")
                .append("<div class=\"barcode\">\n")
                .append("<img src=\"https://barcode.tec-it.com/barcode.ashx?data=").append(selectedAbonnement.getid()).append("&code=Code128&dpi=96\" alt=\"Barcode\"/>\n")
                .append("</div>\n")
                .append("</div>\n")
                .append("</div>\n")
                .append("</div>\n")
                .append("</body>\n")
                .append("</html>\n");

        // Prompt for email sending
        TextInputDialog emailDialog = new TextInputDialog();
        emailDialog.setTitle("Envoyer le Ticket par Email");
        emailDialog.setHeaderText("Entrez l'adresse email du destinataire");
        emailDialog.setContentText("Email:");
        java.util.Optional<String> emailResult = emailDialog.showAndWait();

        if (emailResult.isPresent()) {
            String recipientEmail = emailResult.get();

            // Create a FileChooser to let the user choose where to save the PDF
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le Ticket d'Abonnement " + ticketType);
            fileChooser.setInitialFileName(ticketType + "_Ticket_Abonnement_" + selectedAbonnement.getid() + ".pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
            Stage stage = (Stage) btnGeneratePdf.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try {
                    // Generate PDF
                    Document document = new Document();
                    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
                    document.open();

                    // Add barcode to PDF
                    Barcode128 barcode = new Barcode128();
                    barcode.setCode(String.valueOf(selectedAbonnement.getid()));
                    barcode.setCodeType(Barcode128.CODE128);
                    Image barcodeImage = barcode.createImageWithBarcode(writer.getDirectContent(), null, null);
                    barcodeImage.scaleToFit(200, 50);
                    barcodeImage.setAbsolutePosition(50, 50);
                    document.add(barcodeImage);

                    // Parse HTML content into PDF
                    XMLWorkerHelper.getInstance().parseXHtml(writer, document, new StringReader(htmlContent.toString()));
                    document.close();

                    showAlert("Succès", "Ticket PDF généré avec succès à : " + file.getAbsolutePath());

                    // Send HTML email
                    sendEmailWithHtmlPreview(recipientEmail, htmlContent.toString());
                } catch (DocumentException | IOException e) {
                    showAlert("Erreur", "Erreur lors de la génération du PDF : " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                showAlert("Annulé", "La génération du PDF a été annulée.");
                return;
            }
        } else {
            showAlert("Annulé", "L'envoi par email a été annulé.");
        }
    }

    // Method to send email with HTML content
    private void sendEmailWithHtmlPreview(String recipientEmail, String htmlContent) {
        // Email configuration (e.g., Gmail SMTP)
        String senderEmail = "sbissi.mohamed@esprit.tn"; // Replace with your email
        String senderPassword = "vojb jtsg aumr avrr"; // Replace with your app-specific password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create a session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a new email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Votre Ticket d'Abonnement - Aperçu");

            // Set HTML content
            message.setContent(htmlContent, "text/html; charset=utf-8");

            // Send the email
            Transport.send(message);

            showAlert("Succès", "Ticket envoyé avec succès à " + recipientEmail + " en tant que HTML.");
        } catch (MessagingException e) {
            showAlert("Erreur", "Erreur lors de l'envoi de l'email : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void modifierAbonnement(abonnement ab) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierAbonnement.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Modifier l'abonnement");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));

            ModifierAbonnementController editController = loader.getController();
            editController.setAbonnement(ab);

            stage.showAndWait();
            loadAbonnements();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}