package undertowserver;

import Rest.Routes;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.handlers.PathHandler;
import utilities.ConnectionsXmlReader;

import java.nio.charset.StandardCharsets;

public class UndertowTest {
    public static void main(String[] args) throws RuntimeException {
        System.out.println("Starting REST API");

        try {
            String portRest = ConnectionsXmlReader.getPortRest();
            String hostRest = ConnectionsXmlReader.getHostRest();
            String basePathRest = ConnectionsXmlReader.getBasePathRest();

            try {
                PathHandler routeHandler = Handlers.path()
                        .addPrefixPath(basePathRest + "/survey", Routes.Survey())
                        .addPrefixPath(basePathRest + "/questions", Routes.Questions())
                        .addPrefixPath(basePathRest + "/questions/options", Routes.Options())
                        .addPrefixPath(basePathRest + "/questions/responses", Routes.Responses())
                        .addPrefixPath(basePathRest + "/questions/responses/responders", Routes.Responders())
                        .addPrefixPath(basePathRest + "/questions/responses/certificates", Routes.Certificates());

                Undertow server = Undertow.builder()
                        .setServerOption(UndertowOptions.DECODE_URL, true)
                        .setServerOption(UndertowOptions.URL_CHARSET, StandardCharsets.UTF_8.name())
                        .setServerOption(UndertowOptions.ENABLE_HTTP2, true)
                        .addHttpListener(Integer.parseInt(portRest), hostRest)
                        .setHandler(routeHandler)
                        .build();
                server.start();
                System.out.println("Server Started at " + hostRest + ":" + portRest);
            } catch (Exception e) {
                System.err.println("Failed to Start REST API");
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error in establishing the database connection or starting the server.");
        }
    }
}
