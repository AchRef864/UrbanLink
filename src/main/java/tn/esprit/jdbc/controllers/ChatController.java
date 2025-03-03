package tn.esprit.jdbc.controllers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tn.esprit.jdbc.entities.Maintenance;
import tn.esprit.jdbc.entities.Rating;
import tn.esprit.jdbc.entities.User;
import tn.esprit.jdbc.entities.Vehicle;
import tn.esprit.jdbc.services.RatingService;
import tn.esprit.jdbc.services.UserService;
import tn.esprit.jdbc.services.maintenanceService;
import tn.esprit.jdbc.services.vehicleService;

public class ChatController {

    @FXML
    private TextArea chatHistory;

    @FXML
    private TextField questionField;

    // Replace with your Together AI API key
    private final String togetherApiKey = "097e089c74156c2c0da542fd849922b24db70d00c611064984d4947a480e2f73"; // Use the same API key as in Python

    // Together AI API endpoint
    private final String endpoint = "https://api.together.xyz/v1/chat/completions";

    // Create instances of services
    private final UserService userService = new UserService();
    private final RatingService ratingService = new RatingService();
    private final vehicleService vehicleService = new vehicleService();
    private final maintenanceService maintenanceService = new maintenanceService();

    @FXML
    public void handleAskQuestion() {
        String question = questionField.getText().toLowerCase(); // Convert to lowercase for case-insensitive matching
        chatHistory.appendText("Admin: " + question + "\n");

        try {
            // Check if the question is about the number of clients
            if (question.contains("how many") && (question.contains("client") || question.contains("clients"))) {
                int clientCount = userService.countClients();
                chatHistory.appendText("System: We have " + clientCount + " clients.\n");
            }
            // Check if the question is about listing clients
            else if (question.contains("list") && (question.contains("client") || question.contains("clients"))) {
                List<User> clients = userService.getAllClients();
                chatHistory.appendText("System: Clients: " + clients.toString() + "\n");
            }
            // Check if the question is about the number of admins
            else if (question.contains("how many") && (question.contains("admin") || question.contains("admins"))) {
                int adminCount = userService.countAdmins();
                chatHistory.appendText("System: We have " + adminCount + " admins.\n");
            }
            // Check if the question is about listing admins
            else if (question.contains("list") && (question.contains("admin") || question.contains("admins"))) {
                List<User> admins = userService.getAllAdmins();
                chatHistory.appendText("System: Admins: " + admins.toString() + "\n");
            }
            // Check if the question is about the number of vehicles
            else if (question.contains("how many") && (question.contains("vehicle") || question.contains("vehicles"))) {
                int vehicleCount = vehicleService.countVehicles();
                chatHistory.appendText("System: We have " + vehicleCount + " vehicles.\n");
            }
            // Check if the question is about listing vehicles
            else if (question.contains("list") && (question.contains("vehicle") || question.contains("vehicles"))) {
                List<Vehicle> vehicles = vehicleService.getAllVehicles();
                chatHistory.appendText("System: Vehicles: " + vehicles.toString() + "\n");
            }
            else if (question.contains("rating") || question.contains("ratings")) {
                if (question.contains("average")) {
                    double averageRating = ratingService.getAverageRating();
                    chatHistory.appendText("System: The average rating is " + averageRating + ".\n");
                } else if (question.contains("list")) {
                    List<Rating> ratings = ratingService.getAllRatings();
                    chatHistory.appendText("System: Ratings: " + ratings.toString() + "\n");
                }
            }
            if (question.contains("maintenance")) {
                if (question.contains("how many")) {
                    int maintenanceCount = maintenanceService.countMaintenanceRecords();
                    chatHistory.appendText("System: We have " + maintenanceCount + " maintenance records.\n");
                } else if (question.contains("list")) {
                    List<Maintenance> maintenanceRecords = maintenanceService.getAllMaintenanceRecords();
                    chatHistory.appendText("System: Maintenance Records: " + maintenanceRecords.toString() + "\n");
                }
            }
            // Check if the question is about your team or project
            else if (question.contains("who are you") || question.contains("urbanlink") ||  question.contains("what is urbanlink") || question.contains("tell me about arribatec")) {
                chatHistory.appendText("System: We are 'ArribaTec' from Esprit School of Engineers, and this is the UrbanLink project. UrbanLink is a transportation management system designed to streamline urban mobility.\n");
            }
            // If the question is not recognized, send it to Together AI for a general response
            else {
                // Send the question to Together AI for a general response
                String requestBody = String.format(
                        "{\"model\": \"mistralai/Mistral-7B-Instruct-v0.1\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}]}",
                        question
                );

                // Build the HTTP request
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(endpoint))
                        .header("Authorization", "Bearer " + togetherApiKey)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                        .build();

                // Send the request and get the response
                HttpClient client = HttpClient.newHttpClient();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                // Check the response status code
                if (response.statusCode() == 200) {
                    // Parse the response
                    String responseBody = response.body();
                    String answer = extractAnswerFromResponse(responseBody);
                    chatHistory.appendText("System: " + answer + "\n");
                } else {
                    chatHistory.appendText("System: Error - " + response.body() + "\n");
                }
            }
        } catch (Exception e) {
            chatHistory.appendText("System: Sorry, an error occurred. Please try again.\n");
            e.printStackTrace();
        }

        questionField.clear();
    }

    // Add this method to parse the JSON response
    private String extractAnswerFromResponse(String responseBody) {
        try {
            // Parse the JSON response
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

            // Extract the "content" field from the response
            JsonObject choices = jsonResponse.getAsJsonArray("choices").get(0).getAsJsonObject();
            JsonObject message = choices.getAsJsonObject("message");
            String content = message.get("content").getAsString();

            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return "Could not parse the response.";
        }
    }
}