package org.dummy.calc;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import java.net.UnknownHostException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CalcControllerTest {

    private static final String EXPRESSION = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
    private static final String EVALEX_EXPECTED = "19.9884";
    private static final String MXPARSER_EXPECTED = "19.98843289048526";
    private static final String NOT_AN_EXPRESSION = "abc";

    @Autowired
    private WebTestClient webClient;


    @Test
    void rootTest() {
        this.webClient.get().uri("/").exchange().expectStatus().isOk()
                .expectBody(String.class).isEqualTo(CalcController.WELCOME);
    }

    void evaluate(String path, String expression, String expected) {
        this.webClient.post().uri(path)
                .body(Mono.just(expression), String.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(expected);
    }

    @Test
    void testEvalex() throws UnknownHostException {
        evaluate("/", EXPRESSION, EVALEX_EXPECTED);
    }

    @Test
    void testEvalexN() throws UnknownHostException {
        evaluate("/", NOT_AN_EXPRESSION, "Unknown operator or function: " + NOT_AN_EXPRESSION);
    }

    @Test
    void testMxparser() throws UnknownHostException {
        evaluate("/mxparser", EXPRESSION, MXPARSER_EXPECTED);
    }

    @Test
    void testMxparserN() throws UnknownHostException {
        evaluate("/mxparser", NOT_AN_EXPRESSION, "NaN");
    }
}
