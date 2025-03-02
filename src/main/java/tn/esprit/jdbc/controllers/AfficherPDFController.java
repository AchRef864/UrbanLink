package tn.esprit.jdbc.controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AfficherPDFController {

    @FXML
    private VBox vboxContainer;

    private String pdfFilePath;

    public void setPdfFilePath(String pdfFilePath) {
        this.pdfFilePath = pdfFilePath;
        displayPDF();
    }

    private void displayPDF() {
        try {
            PDDocument document = PDDocument.load(new File(pdfFilePath));
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            int pageCount = document.getNumberOfPages();

            if (pageCount == 0) {
                System.out.println("Le PDF est vide !");
                return;
            }

            for (int i = 0; i < pageCount; i++) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(i, 150);
                Image fxImage = SwingFXUtils.toFXImage(bim, null);
                ImageView imageView = new ImageView(fxImage);
                imageView.setFitWidth(600);
                imageView.setPreserveRatio(true);
                vboxContainer.getChildren().add(imageView);
            }
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'affichage du PDF !");
        }
    }

}
