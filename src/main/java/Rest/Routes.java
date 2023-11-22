package Rest;

import QuerryManager.QueryManager;
import io.undertow.Handlers;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.util.Methods;
import sky.survey.apis.questions.*;
import sky.survey.apis.survey.CreateSurvey;

public class Routes {

    public static RoutingHandler Survey() {
        QueryManager queryManager = new QueryManager();
        return Handlers.routing()
               // .get("", new Dispatcher(new GetQuestions(queryManager)))
                //.get("/{questionId }", new Dispatcher(new GetQuestion(queryManager)))
                .post("", new BlockingHandler(new CreateSurvey(queryManager)))
                //.add(Methods.PATCH, "/{questionId }", new BlockingHandler(new UpdateQuestions(queryManager)))
                //.delete("/{questionId }", new Dispatcher(new DeleteQuestions(queryManager)))
                .add(Methods.OPTIONS, "/*", new CorsHandler())
                .setInvalidMethodHandler(new Dispatcher(new InvalidMethod()))
                .setFallbackHandler(new Dispatcher(new FallBack()));
    }
    public static RoutingHandler Questions() {
        QueryManager queryManager = new QueryManager();
        return Handlers.routing()
                .get("", new Dispatcher(new GetQuestions(queryManager)))
                .get("/{questionId }", new Dispatcher(new GetQuestion(queryManager)))
                .post("", new BlockingHandler(new CreateQuestions(queryManager)))
                .add(Methods.PATCH, "/{questionId }", new BlockingHandler(new UpdateQuestions(queryManager)))
                .delete("/{questionId }", new Dispatcher(new DeleteQuestions(queryManager)))
                .add(Methods.OPTIONS, "/*", new CorsHandler())
                .setInvalidMethodHandler(new Dispatcher(new InvalidMethod()))
                .setFallbackHandler(new Dispatcher(new FallBack()));
    }

    public static RoutingHandler Options() {
        QueryManager queryManager = new QueryManager();
        return Handlers.routing()
                /*.get("", new Dispatcher(new GetQuestions(queryManager)))
                .get("/{questionId }", new Dispatcher(new GetQuestion(queryManager)))
                .post("", new BlockingHandler(new CreateQuestions(queryManager)))
                .add(Methods.PATCH, "/{questionId }", new BlockingHandler(new UpdateQuestions(queryManager)))
                .delete("/{questionId }", new Dispatcher(new DeleteQuestions(queryManager)))*/
                .add(Methods.OPTIONS, "/*", new CorsHandler())
                .setInvalidMethodHandler(new Dispatcher(new InvalidMethod()))
                .setFallbackHandler(new Dispatcher(new FallBack()));
    }

    public static RoutingHandler Responses() {
        QueryManager queryManager = new QueryManager();
        return Handlers.routing()
                //.get("", new Dispatcher(new GetChoices()))
                // .get("/{choiceId}", new Dispatcher(new GetChoice()))
                //.post("", new BlockingHandler(new CreateChoices(queryManager)))
                //.add(Methods.PATCH, "/{choiceId}", new BlockingHandler(new UpdateChoices(queryManager)))
                //.delete("/{choiceId}", new Dispatcher(new DeleteChoices(queryManager)))
                .add(Methods.OPTIONS, "/*", new CorsHandler())
                .setInvalidMethodHandler(new Dispatcher(new InvalidMethod()))
                .setFallbackHandler(new Dispatcher(new FallBack()));
    }
    public static RoutingHandler Responders() {
        QueryManager queryManager = new QueryManager();
        return Handlers.routing()
                //.get("", new Dispatcher(new GetChoices()))
                // .get("/{choiceId}", new Dispatcher(new GetChoice()))
                //.post("", new BlockingHandler(new CreateChoices(queryManager)))
                //.add(Methods.PATCH, "/{choiceId}", new BlockingHandler(new UpdateChoices(queryManager)))
                //.delete("/{choiceId}", new Dispatcher(new DeleteChoices(queryManager)))
                .add(Methods.OPTIONS, "/*", new CorsHandler())
                .setInvalidMethodHandler(new Dispatcher(new InvalidMethod()))
                .setFallbackHandler(new Dispatcher(new FallBack()));
    }

    public static RoutingHandler Certificates() {
        QueryManager queryManager = new QueryManager();
        return Handlers.routing()
               // .get("", new Dispatcher(new GetSubjects()))
                // .get("/{subjectId}", new Dispatcher(new GetSubjects()))
                //.post("", new BlockingHandler(new CreateSubjects(queryManager)))
                //.add(Methods.PATCH, "/{subjectId}", new BlockingHandler(new UpdateSubjects(queryManager)))
                //.delete("/{subjectId}", new Dispatcher(new DeleteSubjects(queryManager)))
                .add(Methods.OPTIONS, "/*", new CorsHandler())
                .setInvalidMethodHandler(new Dispatcher(new InvalidMethod()))
                .setFallbackHandler(new Dispatcher(new FallBack()));
    }
}
