package sky.survey.apis.survey;

import QuerryManager.QueryManager;
import com.google.gson.Gson;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class CreateSurvey implements HttpHandler {

    private final QueryManager queryManager;

    public CreateSurvey(QueryManager queryManager) {
        this.queryManager = queryManager;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        exchange.getRequestReceiver().receiveFullString(this::handle, this::error);
    }

    public void handle(HttpServerExchange exchange, String message) {
        var surveyData = new LinkedHashMap<String, Object>();
        Gson gson = new Gson();

        LinkedHashMap<String, Object> requestBodyMap = gson.fromJson(message, LinkedHashMap.class);

        try {
            String insertQuery = "INSERT INTO survey_details " +
                    "(tittle, survey_detail) " +
                    "VALUES (?, ?)";

            LinkedHashMap<String, Object> values = new LinkedHashMap<>();
            values.put("1", requestBodyMap.get("tittle"));
            values.put("2", requestBodyMap.get("survey_detail"));

            int rowsAffected = queryManager.insert(insertQuery, values);

            if (rowsAffected > 0) {
                surveyData.put("status", "Survey created successfully");
                exchange.setStatusCode(201);
            } else {
                surveyData.put("error", "Failed to create survey");
                exchange.setStatusCode(400);
            }
        } catch (SQLException | ClassNotFoundException e) {
            surveyData.put("error", "Failed to create survey");
            surveyData.put("details", e.getMessage());
            exchange.setStatusCode(400);
        }

        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.getResponseSender().send(gson.toJson(surveyData));
    }

    private void error(HttpServerExchange exchange, IOException error) {
        exchange.setStatusCode(400);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.getResponseSender().send("Error in request");
    }
}
