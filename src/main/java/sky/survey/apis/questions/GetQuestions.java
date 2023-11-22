package sky.survey.apis.questions;

import QuerryManager.QueryManager;
import com.google.gson.Gson;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GetQuestions implements HttpHandler {

    private final QueryManager queryManager;

    public GetQuestions(QueryManager queryManager) {
        this.queryManager = queryManager;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }

        Gson gson = new Gson();

        try {
            List<LinkedHashMap<String, Object>> questionsList = getQuestions();
            Map<String, Object> questionsMap = createQuestionsMap(questionsList);

            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/xml");
            exchange.getResponseSender().send(gson.toJson(questionsMap));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            String errorResponse = "Failed to fetch questions";
            exchange.setStatusCode(500);
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            exchange.getResponseSender().send(errorResponse);
        }
    }

    private List<LinkedHashMap<String, Object>> getQuestions() throws SQLException, ClassNotFoundException {
        String selectQuery = "SELECT * FROM questions_details";
        return queryManager.select(selectQuery, new HashMap<>());
    }

    private Map<String, Object> createQuestionsMap(List<LinkedHashMap<String, Object>> questionsList) {
        Map<String, Object> questionsMap = new HashMap<>();
        questionsMap.put("questions", questionsList);
        return questionsMap;
    }
}
