package tn.esprit.jdbc.services;

import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import org.json.JSONArray;
import org.json.JSONObject;
import tn.esprit.jdbc.entities.Course;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class EmailService {

    // Remplacez ces valeurs par vos clés Mailjet (disponible en free tier)
    private static final String API_KEY = "693a97b9e606a510fefe5d85b2cf70de";
    private static final String API_SECRET = "ab43de088280c16420b0c521571cd6df";

    // Détails de l'expéditeur (à personnaliser)
    private static final String SENDER_EMAIL = "hammami.mohamedmalek@esprit.tn";
    private static final String SENDER_NAME = "ArribaTec";

    /**
     * Génère le contenu HTML de l'email pour afficher la course modifiée.
     *
     * @param userName       Nom de l'utilisateur
     * @param localImagePath Chemin de l'image dans les resources (ex: "/images/myProjectImage.png")
     * @param course         La course modifiée à afficher
     * @return               Contenu HTML de l'email
     */
    public static String generateCourseEmailBody(String userName, String localImagePath, Course course) {
        // Charger l'image locale et l'encoder en base64
        String base64Image = loadLocalImageAsBase64(localImagePath);
        String imageDataUrl = "data:image/png;base64," + base64Image;

        return "<html>"
                + "<body>"
                + "<h2>Bonjour " + userName + ",</h2>"
                + "<img src='" + imageDataUrl + "' alt='Image du projet' style='width:200px; height:auto;'/>"
                + "<p>Voici les détails de votre course modifiée :</p>"
                + "<table border='1' cellpadding='5' cellspacing='0'>"
                + "<tr><th>Ville de départ</th><td>" + course.getVille_depart() + "</td></tr>"
                + "<tr><th>Ville d'arrivée</th><td>" + course.getVille_arrivee() + "</td></tr>"
                + "<tr><th>Date de course</th><td>" + course.getDate_course().toLocalDate() + "</td></tr>"
                + "<tr><th>Distance (km)</th><td>" + course.getDistance_km() + "</td></tr>"
                + "<tr><th>Montant</th><td>" + course.getMontant() + "</td></tr>"
                + "<tr><th>Statut</th><td>" + course.getStatut() + "</td></tr>"
                + "</table>"
                + "</body>"
                + "</html>";
    }

    /**
     * Charge une image depuis les resources et la convertit en chaîne base64.
     *
     * @param imagePath Chemin de l'image dans les resources (commence par "/")
     * @return          L'image encodée en base64, ou une chaîne vide en cas d'erreur
     */
    private static String loadLocalImageAsBase64(String imagePath) {
        try (InputStream is = EmailService.class.getResourceAsStream(imagePath);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            if (is == null) {
                throw new IOException("Image non trouvée : " + imagePath);
            }
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Envoie un email avec contenu texte et HTML.
     *
     * @param recipientEmail Adresse email du destinataire.
     * @param recipientName  Nom du destinataire.
     * @param subject        Objet de l'email.
     * @param htmlBody       Corps HTML de l'email.
     */
    public static void sendEmail(String recipientEmail, String recipientName, String subject, String htmlBody) {
        MailjetClient client = new MailjetClient(API_KEY, API_SECRET);
        try {
            MailjetRequest request = new MailjetRequest(com.mailjet.client.resource.Email.resource)
                    .property(com.mailjet.client.resource.Email.FROMEMAIL, SENDER_EMAIL)
                    .property(com.mailjet.client.resource.Email.FROMNAME, SENDER_NAME)
                    .property(com.mailjet.client.resource.Email.RECIPIENTS, new JSONArray()
                            .put(new JSONObject()
                                    .put("Email", recipientEmail)
                                    .put("Name", recipientName)))
                    .property(com.mailjet.client.resource.Email.SUBJECT, subject)
                    .property(com.mailjet.client.resource.Email.TEXTPART, "Veuillez consulter votre client email pour voir le contenu HTML.")
                    .property(com.mailjet.client.resource.Email.HTMLPART, htmlBody);
            MailjetResponse response = client.post(request);
            System.out.println("Status: " + response.getStatus());
            System.out.println("Data: " + response.getData());
        } catch (MailjetException e) {
            System.err.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
            e.printStackTrace();
        }
    }
}