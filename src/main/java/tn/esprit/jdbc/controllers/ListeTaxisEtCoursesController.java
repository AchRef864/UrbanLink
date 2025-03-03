package tn.esprit.jdbc.controllers;
import com.itextpdf.text.Image;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
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
import tn.esprit.jdbc.entities.Course;
import tn.esprit.jdbc.entities.Taxi;
import com.itextpdf.text.Image;
import tn.esprit.jdbc.entities.User;
import tn.esprit.jdbc.services.ServiceCourse;
import tn.esprit.jdbc.services.ServiceTaxi;
import tn.esprit.jdbc.services.UserService;
import tn.esprit.jdbc.entities.QRCodeGenerator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ListeTaxisEtCoursesController {

    @FXML private TableView<Taxi> tableTaxis;
    @FXML private TableColumn<Taxi, String> colTaxiMarque;
    @FXML private TableColumn<Taxi, String> colTaxiModele;
    @FXML private TableColumn<Taxi, Integer> colTaxiCapacite;
    @FXML private TableColumn<Taxi, String> colTaxiStatut;
    @FXML private Button btnReserverTaxi;

    @FXML private TableView<Course> tableCourses;
    @FXML private TableColumn<Course, String> colVilleDepart;
    @FXML private TableColumn<Course, String> colVilleArrivee;
    @FXML private TableColumn<Course, Double> colDistance;
    @FXML private TableColumn<Course, Double> colMontant;
    @FXML private Button btnModifierCourse;
    @FXML private Button btnSupprimerCourse;

    private final ServiceTaxi serviceTaxi = new ServiceTaxi();
    private final ServiceCourse serviceCourse = new ServiceCourse();
    private final UserService userService = new UserService();
    private int userId; // Stocke l'ID de l'utilisateur connecté

    public void setUserId(int userId) {
        this.userId = userId;
        chargerTaxisDisponibles();
        chargerCoursesUtilisateur();
    }

    @FXML
    public void initialize() {
        configurerColonnes();
    }

    private void configurerColonnes() {
        // Configuration des colonnes pour les taxis
        colTaxiMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colTaxiModele.setCellValueFactory(new PropertyValueFactory<>("modele"));
        colTaxiCapacite.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        colTaxiStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Configuration des colonnes pour les courses
        colVilleDepart.setCellValueFactory(new PropertyValueFactory<>("ville_depart"));
        colVilleArrivee.setCellValueFactory(new PropertyValueFactory<>("ville_arrivee"));
        colDistance.setCellValueFactory(new PropertyValueFactory<>("distance_km"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
    }

    private void chargerTaxisDisponibles() {
        try {
            List<Taxi> taxisDisponibles = serviceTaxi.showAll().stream()
                    .filter(taxi -> taxi.getStatut().equalsIgnoreCase("Disponible"))
                    .collect(Collectors.toList());
            tableTaxis.setItems(FXCollections.observableArrayList(taxisDisponibles));
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Problème lors du chargement des taxis disponibles.");
        }
    }

    private void chargerCoursesUtilisateur() {
        try {
            List<Course> coursesUser = serviceCourse.showAll().stream()
                    .filter(course -> course.getUser_id() == userId)
                    .collect(Collectors.toList());
            tableCourses.setItems(FXCollections.observableArrayList(coursesUser));
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Problème lors du chargement des courses de l'utilisateur.");
        }
    }

    @FXML
    private void handleAjouterCourse() {
        Taxi selectedTaxi = tableTaxis.getSelectionModel().getSelectedItem();
        if (selectedTaxi != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutCourse.fxml"));
                Parent root = loader.load();

                AjoutCourseController controller = loader.getController();
                controller.setTaxiEtUtilisateur(selectedTaxi, userId);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Ajouter une Course");

                stage.setOnHidden(event -> {
                    selectedTaxi.setStatut("En réservation");
                    try {
                        serviceTaxi.update(selectedTaxi);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    chargerTaxisDisponibles();
                    chargerCoursesUtilisateur();
                });

                stage.show();
            } catch (IOException e) {
                afficherAlerte("Erreur", "Problème lors du chargement de la fenêtre d'ajout de course.");
            }
        } else {
            afficherAlerte("Sélection requise", "Veuillez sélectionner un taxi disponible.");
        }
    }

    @FXML
    private void handleModifierCourse() {
        Course selectedCourse = tableCourses.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierCourse.fxml"));
                Parent root = loader.load();

                ModifierCourseController controller = loader.getController();
                controller.setCourse(selectedCourse);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier la Course");

                stage.setOnHidden(event -> chargerCoursesUtilisateur());

                stage.show();
            } catch (IOException e) {
                afficherAlerte("Erreur", "Problème lors du chargement de la modification.");
            }
        } else {
            afficherAlerte("Sélection requise", "Veuillez sélectionner une course à modifier.");
        }
    }

    @FXML
    private void handleSupprimerCourse() throws SQLException {
        Course selectedCourse = tableCourses.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            serviceCourse.delete(selectedCourse.getId_course());
            chargerCoursesUtilisateur();
        } else {
            afficherAlerte("Sélection requise", "Veuillez sélectionner une course à supprimer.");
        }
    }

    @FXML
    private void handleGenererPDF() {
        // Définir un répertoire pour stocker le PDF
        String directoryPath = "C:/MesPDFs";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Définir le chemin complet du fichier PDF
        String filePath = directoryPath + "/facture_" + userId + ".pdf";
        System.out.println("Le PDF sera généré à : " + filePath);

        // Récupérer les courses de l'utilisateur
        List<Course> coursesUser = tableCourses.getItems();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Ajouter un titre
            document.add(new Paragraph("Facture de Paiement"));
            document.add(new Paragraph("Utilisateur : " + userService.getUserById(userId).getName()));
            document.add(new Paragraph(" ")); // Ligne vide

            // Création du tableau avec les détails des courses
            PdfPTable table = new PdfPTable(4);
            table.addCell("Ville Départ");
            table.addCell("Ville Arrivée");
            table.addCell("Distance (km)");
            table.addCell("Montant");

            // Remplir le tableau avec les données des courses
            for (Course course : coursesUser) {
                table.addCell(course.getVille_depart());
                table.addCell(course.getVille_arrivee());
                table.addCell(String.valueOf(course.getDistance_km()));
                table.addCell(String.valueOf(course.getMontant()));
            }
            document.add(table);

            // Calcul du total à payer
            double total = coursesUser.stream().mapToDouble(Course::getMontant).sum();
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Total à payer: " + total + " DT"));

            // Générer et ajouter le QR Code
            String qrCodePath = directoryPath + "/qr_facture_" + userId + ".png";
            String pdfUrl = "http://192.168.56.1:8000/facture_" + userId + ".pdf";
            QRCodeGenerator.generateQRCode(pdfUrl, qrCodePath);


            // Ajouter l'image du QR Code dans le PDF
            Image qrImage = Image.getInstance(qrCodePath);
            qrImage.scaleAbsolute(100, 100); // Taille du QR Code
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Scannez ce QR Code pour télécharger la facture :"));
            document.add(qrImage);

            document.close();

            afficherAlerte("Succès", "Le PDF a été généré avec succès : " + filePath);

            // Ouvrir l'interface d'affichage du PDF
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPDF.fxml"));
            Parent root = loader.load();
            AfficherPDFController controller = loader.getController();
            controller.setPdfFilePath(filePath);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Affichage du PDF");
            stage.show();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            afficherAlerte("Erreur", "Problème lors de la génération du PDF : " + e.getMessage());
        }
    }



    private void afficherPDF(String filePath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPDF.fxml"));
            Parent root = loader.load();

            AfficherPDFController controller = loader.getController();
            controller.setPdfFilePath(filePath);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Affichage du PDF");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            afficherAlerte("Erreur", "Problème lors de l'ouverture du PDF.");
        }
    }


    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}