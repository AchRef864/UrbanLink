package tn.esprit.jdbc.controllers.abonnement;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import tn.esprit.jdbc.entities.abonnement;
import tn.esprit.jdbc.entities.reservation;



import  tn.esprit.jdbc.services.reservationservice;


import java.awt.*;
import java.net.URI;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
    private TextField abonnementIdField;

    private reservationservice reservationService;

    // PayPal API credentials (sandbox/test credentials provided)
    private static final String CLIENT_ID = "Aa0D42A3SVLwZU2YtV3J7IXEOkPCndJk7X7HXrmAFkyRV33Q8tMHSy3b75dj5HA6SzK3QPe0DU1dMPZz";
    private static final String CLIENT_SECRET = "EI9yQI91-sETiQyJNGcdF_LzdHaM2dDx1ijLFcCpffiAaSf_-n5sJx9xp7VEvDSRMBiIGNwz7yYCPmv_";
    private static final String MODE = "sandbox"; // Use "live" for production

    private APIContext apiContext;

    public ControllerReservationAjout() {
        reservationService = new reservationservice();
        apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
    }

    @FXML
    private void initialize() {
        // Set statut to "En attente" and make it non-editable
        statutField.setText("En attente");
        statutField.setEditable(false);
    }

    @FXML
    private void handleAddReservation() {
        try {
            int userId = Integer.parseInt(userIdField.getText());
            Date dateDebut = Date.valueOf(dateDebutPicker.getValue());
            Date dateFin = Date.valueOf(dateFinPicker.getValue());
            String statut = statutField.getText(); // Will always be "En attente" due to non-editable field
            int abonnementId = Integer.parseInt(abonnementIdField.getText());

            if (userId <= 0 || dateDebut == null || dateFin == null || abonnementId <= 0) {
                showAlert("Erreur", "Veuillez remplir tous les champs avec des valeurs valides.", Alert.AlertType.ERROR);
                return;
            }

            if (dateFin.before(dateDebut)) {
                showAlert("Erreur", "La date de fin doit être postérieure à la date de début.", Alert.AlertType.ERROR);
                return;
            }

            abonnement abonnement = new abonnement();
            abonnement.setid(abonnementId);

            reservation reservation = new reservation(
                    0, // ID auto-generated
                    userId,
                    dateDebut,
                    dateFin,
                    statut,
                    abonnement
            );

            int id = reservationService.insert(reservation);
            if (id != -1) {
                showAlert("Succès", "Réservation ajoutée avec succès. ID: " + id + ". Paiement en cours avec PayPal.", Alert.AlertType.INFORMATION);
                double amount = calculateAmountBasedOnAbonnement(abonnementId);
                initiatePayPalPayment(id, amount); // Auto-send payment
                clearForm();
            } else {
                showAlert("Erreur", "Échec de l'ajout de la réservation.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs numériques valides pour les IDs.", Alert.AlertType.ERROR);
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
        userIdField.clear();
        dateDebutPicker.setValue(null);
        dateFinPicker.setValue(null);
        statutField.setText("En attente"); // Reset to default
        abonnementIdField.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}