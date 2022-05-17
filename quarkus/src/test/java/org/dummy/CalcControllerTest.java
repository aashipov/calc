package org.dummy;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

@QuarkusTest
class CalcControllerTest {
    private static final String EXPRESSION = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
    private static final String EVALEX_EXPECTED = "19.9884";
    private static final String MXPARSER_EXPECTED = "19.98843289048526";
    private static final String NOT_AN_EXPRESSION = "abc";

    private static void evaluate(String url, String expression, String expected) {
        RequestSpecification requestSpec = new RequestSpecBuilder().setBody(expression).build();
        given().spec(requestSpec)
                .when().post(url)
                .then()
                .statusCode(200)
                .body(is(expected));
    }

    @Test
    void testEvalex() {
        evaluate("/", EXPRESSION, EVALEX_EXPECTED);
    }

    @Test
    void testEvalexN() {
        evaluate("/", NOT_AN_EXPRESSION, "Unknown operator or function: " + NOT_AN_EXPRESSION);
    }

    @Test
    void testMxparser() {
        evaluate("/mxparser", EXPRESSION, MXPARSER_EXPECTED);
    }

    @Test
    void testMxparserN() {
        evaluate("/mxparser", NOT_AN_EXPRESSION, "NaN");
    }
}
