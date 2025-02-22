package tn.esprit.jdbc.services;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PdfGenerator {

    public void generatePdf(List<DatabaseMetadataService.TableSpecification> tableSpecs, String filePath) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        for (DatabaseMetadataService.TableSpecification tableSpec : tableSpecs) {
            document.add(new Paragraph("Table: " + tableSpec.getTableName()));
            for (DatabaseMetadataService.ColumnSpecification column : tableSpec.getColumns()) {
                document.add(new Paragraph(
                        "Column: " + column.getName() +
                                ", Type: " + column.getType() +
                                ", Size: " + column.getSize()
                ));
            }
            document.add(new Paragraph("\n")); // Add space between tables
        }

        document.close();
    }
}