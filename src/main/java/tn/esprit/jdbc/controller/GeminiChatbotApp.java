package tn.esprit.jdbc.controller;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.jdbc.entities.GeminiAPI;



// Example usage in your UI controller
public class GeminiChatbotApp extends Application {
    private TextArea chatArea;
    private TextField inputField;
    private GeminiAPI geminiAPI;

    @Override
    public void start(Stage primaryStage) {
        geminiAPI = new GeminiAPI(); // Initialize Gemini API Client

        chatArea = new TextArea();
        chatArea.setEditable(false);

        inputField = new TextField();
        inputField.setPromptText("Type your message...");

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> sendMessage());

        VBox layout = new VBox(10, chatArea, inputField, sendButton);
        layout.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(layout, 400, 500));
        primaryStage.setTitle("Gemini Chatbot");
        primaryStage.show();
    }

    private void sendMessage() {
        String userInput = inputField.getText();
        if (!userInput.isEmpty()) {
            chatArea.appendText("You: " + userInput + "\n");
            inputField.clear();

            // Fetch AI response
            String response = geminiAPI.getChatResponse(userInput);
            chatArea.appendText("Gemini: " + response + "\n");
        }
    }
}

