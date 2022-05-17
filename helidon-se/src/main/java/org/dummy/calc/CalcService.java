package org.dummy.calc;

import java.util.logging.Level;
import java.util.logging.Logger;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;

public class CalcService implements Service {

    private static final Logger LOGGER = Logger.getLogger(CalcService.class.getName());
    public static final String WELCOME = "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)";

    @Override
    public void update(Routing.Rules rules) {
        rules
            .get("/", this::getDefaultMessageHandler)
            .post("/", this::evalex)
            .post("/mxparser", this::mxparser);
    }

    private void getDefaultMessageHandler(ServerRequest request, ServerResponse response) {
        response.send(WELCOME);
    }

    void evalex(ServerRequest request, ServerResponse response) {
        request.content().as(String.class)
                .thenAccept(expr -> response.send(new com.udojava.evalex.Expression(expr).eval().toString()))
                .exceptionally(e -> {
                    LOGGER.log(Level.INFO, null, e);
                    response.send(e.getMessage());
                    return null;
                });
    }

    void mxparser(ServerRequest request, ServerResponse response) {
        request.content().as(String.class).thenAccept(expr -> response.send(String.valueOf(new org.mariuszgromada.math.mxparser.Expression(expr).calculate())));
    }
}