package org.dummy.calc;

import io.helidon.microprofile.tests.junit5.HelidonTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@HelidonTest
class CalcControllerTest {

    @Inject
    private WebTarget target;

    private static final String EXPRESSION = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
    private static final String EVALEX_EXPECTED = "19.9884";
    private static final String MXPARSER_EXPECTED = "19.98843289048526";
    private static final String NOT_AN_EXPRESSION = "abc";

    private void post(String url, String expression, String expected) {
        Response response = target
                .path(url)
                .request()
                .post(Entity.entity(expression, MediaType.TEXT_PLAIN_TYPE));
        Assertions.assertEquals(expected, response.readEntity(String.class), "default message");
    }

    @Test
    void testEvalex() {
        post("/", EXPRESSION, EVALEX_EXPECTED);
    }

    @Test
    void testEvalexN() {
        post("/", NOT_AN_EXPRESSION, "Unknown operator or function: " + NOT_AN_EXPRESSION);
    }

    @Test
    void testMxparser() {
        post("/mxparser", EXPRESSION, MXPARSER_EXPECTED);
    }

    @Test
    void testMxparserN() {
        post("/mxparser", NOT_AN_EXPRESSION, "NaN");
    }
}
