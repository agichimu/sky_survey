package sky.survey.apis.questions;

import QuerryManager.QueryManager;
import com.google.gson.Gson;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class CreateQuestions implements HttpHandler {

    private final QueryManager queryManager;

    public CreateQuestions(QueryManager queryManager) {
        this.queryManager = queryManager;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        exchange.getRequestReceiver().receiveFullString(this::handle, this::error);
    }

    public void handle(HttpServerExchange exchange, String message) {
        var questionData = new LinkedHashMap<String, Object>();
        Gson gson = new Gson();

        LinkedHashMap<String, Object> requestBodyMap = gson.fromJson(message, LinkedHashMap.class);

        try {
            String insertQuery = "INSERT INTO questions_details " +
                    "(survey_id, question_name, question_type, required, question_text, description) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            LinkedHashMap<String, Object> values = new LinkedHashMap<>();
            values.put("1", requestBodyMap.get("survey_id"));
            values.put("2", requestBodyMap.get("question_name"));
            values.put("3", requestBodyMap.get("question_type"));
            values.put("4", requestBodyMap.get("required"));
            values.put("5", requestBodyMap.get("question_text"));
            values.put("6", requestBodyMap.get("description"));

            int rowsAffected = queryManager.insert(insertQuery, values);

            if (rowsAffected > 0) {
                questionData.put("status", "Question created successfully");
                exchange.setStatusCode(201);
            } else {
                questionData.put("error", "Failed to create question");
                exchange.setStatusCode(400);
            }
        } catch (SQLException | ClassNotFoundException e) {
            questionData.put("error", "Failed to create question");
            questionData.put("details", e.getMessage());
            exchange.setStatusCode(400);
        }

        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.getResponseSender().send(gson.toJson(questionData));
    }

    private void error(HttpServerExchange exchange, IOException error) {
        exchange.setStatusCode(400);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.getResponseSender().send("Error in request");
    }
}
