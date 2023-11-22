package sky.survey.apis.questions;

import QuerryManager.QueryManager;
import Rest.RestUtils;
import com.google.gson.Gson;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.sql.SQLException;
import java.util.*;

public class GetQuestion implements HttpHandler {

    private final QueryManager queryManager;

    public GetQuestion(QueryManager queryManager) {
        this.queryManager = queryManager;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }

        Gson gson = new Gson();
        String questionId = RestUtils.getPathVar(exchange, "questionId");

        if (questionId != null) {
            try {
                Map<String, Object> questionMap;
                questionMap = Collections.unmodifiableMap(getQuestion(questionId));
                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                exchange.getResponseSender().send(gson.toJson(questionMap));
            } catch (SQLException e) {
                e.printStackTrace();
                String errorResponse = "Failed to fetch question";
                exchange.setStatusCode(500);
                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                exchange.getResponseSender().send(errorResponse);
            }
        } else {
            String errorResponse = "Question ID not provided";
            exchange.setStatusCode(400);
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            exchange.getResponseSender().send(errorResponse);
        }
    }

    private Map<String, Object> getQuestion(String questionId) throws SQLException {
        Map<String, Object> questionMap = new HashMap<>();

        String selectQuery = "SELECT * FROM questions_details WHERE question_id = ?";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("questionId", questionId);

        try {
            List<LinkedHashMap<String, Object>> results = queryManager.select(selectQuery, paramMap);

            if (!results.isEmpty()) {
                questionMap = results.get(0);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return questionMap;
    }
}
