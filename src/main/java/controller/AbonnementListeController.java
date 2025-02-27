package controller;


import entity.abonnement;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import services.abonnementservices;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import java.time.LocalDate;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.scene.Scene;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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
    private ComboBox<String> cbType;
    @FXML
    private TextField tfPrix;
    @FXML
    private DatePicker dpDateDebut;
    @FXML
    private DatePicker dpDateFin;
    @FXML
    private ComboBox<String> cbEtat;

    private abonnementservices abonnementService;

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
        cbEtat.setItems(FXCollections.observableArrayList("Actif", "Suspendu", "Expiré"));
        cbType.setItems(FXCollections.observableArrayList("Annuel", "Mensuel", "Trimestriel"));

        // Configuration du DatePicker pour empêcher la sélection de dates passées (pour la date de début)
        dpDateDebut.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        // Pour la date de fin, on désactive les dates antérieures à la date de début si celle-ci est sélectionnée
        dpDateFin.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (dpDateDebut.getValue() != null) {
                    setDisable(empty || date.isBefore(dpDateDebut.getValue()));
                } else {
                    setDisable(empty || date.isBefore(LocalDate.now()));
                }
            }
        });

        // Configuration de la colonne d'actions avec les boutons Editer et Supprimer
        colAction.setCellFactory(new Callback<TableColumn<abonnement, Void>, TableCell<abonnement, Void>>() {
            @Override
            public TableCell<abonnement, Void> call(TableColumn<abonnement, Void> param) {
                return new TableCell<abonnement, Void>() {
                    private final Button editButton = new Button("Editer");
                    private final Button deleteButton = new Button("Supprimer");
                    private final HBox hBox = new HBox(10, editButton, deleteButton);

                    {
                        // Style pour le bouton Editer
                        editButton.setStyle(
                                "-fx-background-color: #4CAF50; " +
                                        "-fx-text-fill: white; " +
                                        "-fx-font-size: 14px; " +
                                        "-fx-padding: 10px 20px; " +
                                        "-fx-border-radius: 5px; " +
                                        "-fx-background-radius: 5px;"
                        );

                        // Style pour le bouton Supprimer
                        deleteButton.setStyle(
                                "-fx-background-color: #f44336; " +
                                        "-fx-text-fill: white; " +
                                        "-fx-font-size: 14px; " +
                                        "-fx-padding: 10px 20px; " +
                                        "-fx-border-radius: 5px; " +
                                        "-fx-background-radius: 5px;"
                        );

                        // Action sur le bouton Editer
                        editButton.setOnAction(event -> {
                            abonnement abonnement = getTableView().getItems().get(getIndex());
                            modifierAbonnement(abonnement);
                        });

                        // Action sur le bouton Supprimer
                        deleteButton.setOnAction(event -> {
                            abonnement abonnement = getTableView().getItems().get(getIndex());
                            try {
                                int result = abonnementService.delete(abonnement);
                                if (result > 0) {
                                    showAlert("Succès", "Abonnement supprimé avec succès !");
                                    loadAbonnements(); // Recharge le tableau
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
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(hBox);
                        }
                    }
                };
            }
        });
    }

    // Méthode pour charger tous les abonnements dans le TableView
    private void loadAbonnements() throws SQLException {
        List<abonnement> abonnements = abonnementService.getAll();
        tableAbonnements.setItems(FXCollections.observableArrayList(abonnements));
    }

    // Méthode pour ajouter un nouvel abonnement avec contrôle de saisie
    @FXML
    public void ajouterAbonnement() {
        String type = cbType.getValue();
        String prixText = tfPrix.getText();
        String etat = cbEtat.getValue();
        LocalDate localDateDebut = dpDateDebut.getValue();
        LocalDate localDateFin = dpDateFin.getValue();

        // Vérification que tous les champs sont renseignés
        if (type == null || type.isEmpty() ||
                prixText.isEmpty() ||
                etat == null ||
                localDateDebut == null ||
                localDateFin == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        // Vérification que la date de fin est postérieure à la date de début
        if (localDateFin.isBefore(localDateDebut)) {
            showAlert("Erreur", "La date de fin doit être postérieure à la date de début.");
            return;
        }

        // Vérification du prix (doit être un nombre positif)
        double prix;
        try {
            prix = Double.parseDouble(prixText);
            if (prix <= 0) {
                showAlert("Erreur", "Le prix doit être un nombre positif.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un nombre valide.");
            return;
        }

        // Conversion des LocalDate en java.sql.Date
        java.sql.Date dateDebut = java.sql.Date.valueOf(localDateDebut);
        java.sql.Date dateFin = java.sql.Date.valueOf(localDateFin);

        // Création et insertion du nouvel abonnement
        try {
            abonnement newAbonnement = new abonnement(type, prix, dateDebut, dateFin, etat);
            int result = abonnementService.insert(newAbonnement);
            if (result > 0) {
                showAlert("Succès", "Abonnement ajouté avec succès!");
                loadAbonnements(); // Recharge le tableau
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'ajout de l'abonnement.");
            e.printStackTrace();
        }
    }

    // Méthode pour retourner à l'accueil (ou fermer la fenêtre actuelle)
    @FXML
    public void retourAccueil() {
        System.out.println("Retour à l'accueil");
    }

    // Méthode utilitaire pour afficher des alertes
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour ouvrir la fenêtre de modification d'un abonnement
    private void modifierAbonnement(abonnement ab) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierAbonnement.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Modifier l'abonnement");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));

            // Passage de l'abonnement sélectionné au contrôleur de modification
            ModifierAbonnementController editController = loader.getController();
            editController.setAbonnement(ab);

            // Affiche la fenêtre de modification et attend sa fermeture
            stage.showAndWait();

            // Recharge les données après modification
            loadAbonnements();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}