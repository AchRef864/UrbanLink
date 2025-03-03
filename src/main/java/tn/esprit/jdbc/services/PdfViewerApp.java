package tn.esprit.jdbc.services;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;


public class PdfViewerApp extends Application {

    private String pdfFilePath;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Button generateButton = new Button("Generate and Display PDF");
        generateButton.setOnAction(event -> {
            try {
                // Fetch database metadata
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "username", "password");
                DatabaseMetadataService metadataService = new DatabaseMetadataService(connection);
                List<DatabaseMetadataService.TableSpecification> tableSpecs = metadataService.getTableSpecifications();

                // Generate PDF
                PdfGenerator pdfGenerator = new PdfGenerator();
                pdfFilePath = "database_specifications.pdf";
                pdfGenerator.generatePdf(tableSpecs, pdfFilePath);

                // Display PDF
                displayPdf(pdfFilePath);
            } catch (SQLException | IOException | com.itextpdf.text.DocumentException e) {
                e.printStackTrace();
            }
        });

        VBox root = new VBox(generateButton);
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Database Specifications PDF Viewer");
        primaryStage.show();
    }

    private void displayPdf(String filePath) {
        try {
            PDDocument document = PDDocument.load(new File(filePath));
            PDFRenderer renderer = new PDFRenderer(document);

            // Render the first page as an image
            BufferedImage image = renderer.renderImage(0);

            // Save the image to a temporary file (optional)
            File tempFile = File.createTempFile("pdf_page", ".png");
            ImageIO.write(image, "PNG", tempFile);

            // Open the image in the default image viewer
            java.awt.Desktop.getDesktop().open(tempFile);

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
