package tn.esprit.jdbc.controllers ;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtil {

    private static final String FROM_EMAIL = "sbissi.mohamed@esprit.tn"; // Replace with your email
    private static final String EMAIL_PASSWORD = "vojb jtsg aumr avrr"; // Replace with your email password

    public static void sendVerificationCode(String toEmail, String code) {
        // Email configuration
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com"); // SMTP host for Gmail
        properties.put("mail.smtp.port", "587"); // Port for TLS
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Create a session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
            }
        });

        try {
            // Create a MimeMessage
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Password Reset Verification Code");
            message.setText("Your verification code is: " + code);

            // Send the email
            Transport.send(message);
            System.out.println("Verification code sent to " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
}