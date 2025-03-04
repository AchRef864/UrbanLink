package tn.esprit.jdbc.controllers.abonnement;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import tn.esprit.jdbc.entities.abonnement;
import tn.esprit.jdbc.entities.reservation;
import tn.esprit.jdbc.entities.User;
import tn.esprit.jdbc.services.abonnementservices;
import tn.esprit.jdbc.services.reservationservice;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.awt.Desktop;
import java.net.URI;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerReservationAjout {

    @FXML
    private TextField userIdField;
    @FXML
    private DatePicker dateDebutPicker;
    @FXML
    private DatePicker dateFinPicker;
    @FXML
    private TextField statutField;
    @FXML
    private ComboBox<String> abonnementCombo;

    private reservationservice reservationService;
    private abonnementservices abonnementService;
    private User loggedInUser;
    private List<abonnement> availableAbonnements;

    private static final String CLIENT_ID = "Aa0D42A3SVLwZU2YtV3J7IXEOkPCndJk7X7HXrmAFkyRV33Q8tMHSy3b75dj5HA6SzK3QPe0DU1dMPZz";
    private static final String CLIENT_SECRET = "EI9yQI91-sETiQyJNGcdF_LzdHaM2dDx1ijLFcCpffiAaSf_-n5sJx9xp7VEvDSRMBiIGNwz7yYCPmv_";
    private static final String MODE = "sandbox";
    private APIContext apiContext;

    public ControllerReservationAjout() {
        reservationService = new reservationservice();
        abonnementService = new abonnementservices();
        apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        if (user != null) {
            userIdField.setText(String.valueOf(user.getUserId()));
            userIdField.setEditable(false);
            userIdField.setStyle("-fx-background-color: #e0e0e0;");
        }
    }

    @FXML
    private void initialize() {
        statutField.setText("En attente");
        statutField.setEditable(false);
        try {
            // Load abonnements from the database
            availableAbonnements = abonnementService.showAll();
            System.out.println("Loaded abonnements: " + availableAbonnements);

            // Validate abonnements list
            if (availableAbonnements == null || availableAbonnements.isEmpty()) {
                showAlert("Erreur", "Aucun abonnement disponible dans la base de données.", Alert.AlertType.ERROR);
                abonnementCombo.setDisable(true);
                return;
            }

            // Populate ComboBox
            List<String> abonnementItems = availableAbonnements.stream()
                    .map(ab -> ab.getid() + " - " + ab.gettype())
                    .collect(Collectors.toList());
            System.out.println("ComboBox items: " + abonnementItems);

            abonnementCombo.setItems(FXCollections.observableArrayList(abonnementItems));
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des abonnements : " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddReservation() {
        try {
            // Validate user input
            int userId = Integer.parseInt(userIdField.getText());
            java.sql.Date dateDebut = Date.valueOf(dateDebutPicker.getValue());
            java.sql.Date dateFin = Date.valueOf(dateFinPicker.getValue());
            String statut = statutField.getText();
            String selectedAbonnement = abonnementCombo.getValue();

            if (userId <= 0 || dateDebut == null || dateFin == null || selectedAbonnement == null) {
                showAlert("Erreur", "Veuillez remplir tous les champs avec des valeurs valides.", Alert.AlertType.ERROR);
                return;
            }

            if (dateFin.before(dateDebut)) {
                showAlert("Erreur", "La date de fin doit être postérieure à la date de début.", Alert.AlertType.ERROR);
                return;
            }

            // Parse the selected abonnement
            System.out.println("Selected abonnement: " + selectedAbonnement);
            int abonnementId;
            try {
                selectedAbonnement = selectedAbonnement.trim();
                if (!selectedAbonnement.contains(" - ")) {
                    throw new IllegalArgumentException("Format invalide pour l'abonnement sélectionné : " + selectedAbonnement);
                }
                String[] parts = selectedAbonnement.split("\\s*-\\s*"); // Handle varying spaces
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Format invalide pour l'abonnement sélectionné : " + selectedAbonnement);
                }
                abonnementId = Integer.parseInt(parts[0]);
            } catch (Exception e) {
                showAlert("Erreur", "Format d'abonnement invalide : " + selectedAbonnement, Alert.AlertType.ERROR);
                e.printStackTrace();
                return;
            }
            System.out.println("Parsed abonnement ID: " + abonnementId);

            // Find the abonnement object
            abonnement abonnement = availableAbonnements.stream()
                    .filter(ab -> ab.getid() == abonnementId)
                    .findFirst()
                    .orElse(null);

            if (abonnement == null) {
                System.out.println("Available abonnements: " + availableAbonnements);
                showAlert("Erreur", "Abonnement avec ID " + abonnementId + " introuvable.", Alert.AlertType.ERROR);
                return;
            }

            // Create the reservation
            reservation reservation = new reservation(
                    0, // ID auto-generated
                    userId,
                    dateDebut,
                    dateFin,
                    statut,
                    abonnement
            );

            // Insert the reservation
            int id = reservationService.insert(reservation);
            if (id != -1) {
                showAlert("Succès", "Réservation ajoutée avec succès. ID: " + id + ". Paiement en cours avec PayPal.", Alert.AlertType.INFORMATION);
                double amount = calculateAmountBasedOnAbonnement(abonnementId);
                initiatePayPalPayment(id, amount);
                clearForm();
            } else {
                showAlert("Erreur", "Échec de l'ajout de la réservation.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs numériques valides pour les IDs.", Alert.AlertType.ERROR);
            e.printStackTrace();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'insertion de la réservation : " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur inattendue : " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private double calculateAmountBasedOnAbonnement(int abonnementId) {
        switch (abonnementId % 3) {
            case 0: return 100.00; // Annuel
            case 1: return 20.00;  // Mensuel
            case 2: return 60.00;  // Semestriel
            default: return 50.00; // Default
        }
    }

    private void initiatePayPalPayment(int reservationId, double amount) {
        try {
            Amount paymentAmount = new Amount();
            paymentAmount.setCurrency("USD");
            paymentAmount.setTotal(String.valueOf(amount));

            Transaction transaction = new Transaction();
            transaction.setAmount(paymentAmount);
            transaction.setDescription("Paiement pour la réservation #" + reservationId);

            List<Transaction> transactions = new ArrayList<>();
            transactions.add(transaction);

            Payer payer = new Payer();
            payer.setPaymentMethod("paypal");

            Payment payment = new Payment();
            payment.setIntent("sale");
            payment.setPayer(payer);
            payment.setTransactions(transactions);

            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setReturnUrl("https://your-website.com/success?reservationId=" + reservationId);
            redirectUrls.setCancelUrl("https://your-website.com/cancel?reservationId=" + reservationId);
            payment.setRedirectUrls(redirectUrls);

            Payment createdPayment = payment.create(apiContext);

            String approvalUrl = null;
            for (Links link : createdPayment.getLinks()) {
                if ("approval_url".equals(link.getRel())) {
                    approvalUrl = link.getHref();
                    break;
                }
            }

            if (approvalUrl != null) {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(approvalUrl));
                } else {
                    showAlert("Erreur", "Impossible d'ouvrir le navigateur pour le paiement.", Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Erreur", "Aucun lien de paiement trouvé.", Alert.AlertType.ERROR);
            }
        } catch (PayPalRESTException e) {
            showAlert("Erreur", "Erreur lors de la création du paiement PayPal : " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur inattendue lors du paiement : " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void clearForm() {
        dateDebutPicker.setValue(null);
        dateFinPicker.setValue(null);
        statutField.setText("En attente");
        abonnementCombo.getSelectionModel().clearSelection();
        if (loggedInUser != null) {
            userIdField.setText(String.valueOf(loggedInUser.getUserId()));
            userIdField.setEditable(false);
            userIdField.setStyle("-fx-background-color: #e0e0e0;");
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}