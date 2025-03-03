package tn.esprit.jdbc.entities;

import okhttp3.*; // Import OkHttp
import com.google.gson.*; // Import Gson
import java.io.IOException;

public class GeminiAPI {
    private static final String API_KEY = "xxxx"; // Replace with your actual OpenRouter API key
    private static final String GEMINI_API_URL = "https://openrouter.ai/api/v1/chat/completions";

    private final OkHttpClient client;
    private final Gson gson;

    public GeminiAPI() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    public String getChatResponse(String userMessage) {
        // Construct the JSON payload
        String jsonRequest = createJsonRequest(userMessage);

        // Build the POST request
        RequestBody body = RequestBody.create(jsonRequest, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(GEMINI_API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + "xxxxxxxxxxx")
                .addHeader("Content-Type", "application/json")
                .build();

        // Make the API request and handle the response
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                return parseResponse(responseBody);
            } else {
                return "Error: Unable to get response. Code: " + response.code();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    // Create the JSON request body for the API
    private String createJsonRequest(String userMessage) {
        JsonObject jsonObject = new JsonObject();

        // Create the 'messages' array with user input
        JsonArray messages = new JsonArray();
        JsonObject userMessageObject = new JsonObject();
        userMessageObject.addProperty("role", "user");

        // Add user message
        JsonArray content = new JsonArray();
        JsonObject textContent = new JsonObject();
        textContent.addProperty("type", "text");
        textContent.addProperty("text", userMessage);
        content.add(textContent);

        userMessageObject.add("content", content);
        messages.add(userMessageObject);

        // Add the model
        jsonObject.addProperty("model", "google/gemini-2.0-pro-exp-02-05:free");
        jsonObject.add("messages", messages);

        return jsonObject.toString();
    }

    // Parse the response from the API
    private String parseResponse(String jsonResponse) {
        try {
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            JsonArray choices = jsonObject.getAsJsonArray("choices");

            // Check if choices is not null and not empty
            if (choices != null && choices.size() > 0) {
                JsonObject firstChoice = choices.get(0).getAsJsonObject();
                JsonObject message = firstChoice.getAsJsonObject("message");

                // Check if the content is a JsonArray or JsonPrimitive
                if (message != null && message.has("content")) {
                    JsonElement contentElement = message.get("content");

                    // If "content" is a JsonArray, proceed
                    if (contentElement.isJsonArray()) {
                        JsonArray contentArray = contentElement.getAsJsonArray();
                        if (contentArray.size() > 0) {
                            JsonObject firstPart = contentArray.get(0).getAsJsonObject();
                            return firstPart.get("text").getAsString();
                        }
                    }
                    // If "content" is a JsonPrimitive (like a simple string), handle that case
                    else if (contentElement.isJsonPrimitive()) {
                        return contentElement.getAsString();
                    }
                }
            }
            return "Error: No valid response from Gemini.";
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return "Error: Failed to parse the response.";
        }
    }
}
