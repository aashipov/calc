package org.dummy.calc;

import java.nio.charset.StandardCharsets;

import static org.dummy.calc.JavaExprAdapter.EVALEX;
import static org.dummy.calc.JavaExprAdapter.EXPRTK;
import static org.dummy.calc.JavaExprAdapter.MEVAL;
import static org.dummy.calc.JavaExprAdapter.MXPARSER;

import io.helidon.http.Status;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;

class CalcService implements HttpService {

    private static final String WELCOME = "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)";
    private static final byte[] WELCOME_BYTES = WELCOME.getBytes(StandardCharsets.UTF_8);
    private static final String NAN = "" + Double.NaN;

    @Override
    public void routing(HttpRules rules) {
        rules
                .get("/", this::respondWelcome)
                .post("/", this::respondViaEvalex)
                .post("/" + EVALEX, this::respondViaEvalex)
                .post("/" + MXPARSER, this::respondViaMxParser)
                .post("/" + EXPRTK, this::respondViaMxExprtk)
                .post("/" + MEVAL, this::respondViaMeval);
    }

    private void textResponse(ServerResponse response, Status status, byte[] body) {
        response.status(status).send(body);
    }

    private void respondWelcome(ServerRequest request, ServerResponse response) {
        textResponse(response, Status.OK_200, WELCOME_BYTES);
    }

    private void respondViaMxParser(ServerRequest request, ServerResponse response) {
        String result = NAN;
        if (request.content().hasEntity()) {
            String expr = request.content().as(String.class);
            result = "" + JavaExprAdapter.calculateViamXparser(expr);
        }
        textResponse(response, Status.OK_200, result.getBytes(StandardCharsets.UTF_8));
    }

    private void respondViaMxExprtk(ServerRequest request, ServerResponse response) {
        String result = NAN;
        if (request.content().hasEntity()) {
            String expr = request.content().as(String.class);
            result = "" + JavaExprAdapter.calculateViaExprtk(expr);
        }
        textResponse(response, Status.OK_200, result.getBytes(StandardCharsets.UTF_8));
    }

    private void respondViaEvalex(ServerRequest request, ServerResponse response) {
        String result = NAN;
        if (request.content().hasEntity()) {
            String expr = request.content().as(String.class);
            result = "" + JavaExprAdapter.calculateViaEvalEx(expr);
        }
        textResponse(response, Status.OK_200, result.getBytes(StandardCharsets.UTF_8));
    }

    private void respondViaMeval(ServerRequest request, ServerResponse response) {
        String result = NAN;
        if (request.content().hasEntity()) {
            String expr = request.content().as(String.class);
            result = "" + JavaExprAdapter.calculateViaMeval(expr);
        }
        textResponse(response, Status.OK_200, result.getBytes(StandardCharsets.UTF_8));
    }
}
