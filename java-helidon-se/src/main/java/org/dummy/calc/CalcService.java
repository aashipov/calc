package org.dummy.calc;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.helidon.http.Status;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;

class CalcService implements HttpService {

    private static final Logger LOGGER = Logger.getLogger(CalcService.class.getSimpleName());
    private static final String WELCOME = "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)";
    private static final byte[] WELCOME_BYTES = WELCOME.getBytes(StandardCharsets.UTF_8);
    private static final String MXPARSER = "mxparser";
    private static final String EXPRTK = "exprtk";
    private static final String NAN = "NaN";

    static {
        org.mariuszgromada.math.mxparser.License.iConfirmNonCommercialUse("dummy");
    }

    @Override
    public void routing(HttpRules rules) {
        rules
                .get("/", this::respondWelcome)
                .post("/", this::respondViaEvalex)
                .post("/" + MXPARSER, this::respondViaMxParser)
                .post("/" + EXPRTK, this::respondViaMxExprtk);
    }

    private void textResponse(ServerResponse response, Status status, byte[] body) {
        response.status(status).send(body);
    }

    private void respondWelcome(ServerRequest request, ServerResponse response) {
        textResponse(response, Status.OK_200, WELCOME_BYTES);
    }

    private void respondViaMxParser(ServerRequest request, ServerResponse response) {
        String expr = request.content().as(String.class);
        String result = NAN;
        try {
            result = String.valueOf(new org.mariuszgromada.math.mxparser.Expression(expr).calculate());
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, ex.getMessage());
        } finally {
            textResponse(response, Status.OK_200, result.getBytes(StandardCharsets.UTF_8));
        }
    }

    private void respondViaMxExprtk(ServerRequest request, ServerResponse response) {
        String expr = request.content().as(String.class);
        String result = NAN;
        try {
            result = "" + JavaExprtkAdapter.calculate(expr);
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, ex.getMessage());
        } finally {
            textResponse(response, Status.OK_200, result.getBytes(StandardCharsets.UTF_8));
        }
    }

    private void respondViaEvalex(ServerRequest request, ServerResponse response) {
        String expr = request.content().as(String.class);
        String result = NAN;
        try {
            result = (new com.udojava.evalex.Expression(expr).eval()).toString();
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, ex.getMessage());
        } finally {
            textResponse(response, Status.OK_200, result.getBytes(StandardCharsets.UTF_8));
        }
    }
}
