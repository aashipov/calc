package org.dummy.calc;

import java.util.concurrent.TimeUnit;
import io.helidon.webclient.WebClient;
import io.helidon.webserver.WebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CalcServiceTest {

    private static final String EXPRESSION = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
    private static final String EVALEX_EXPECTED = "19.9884";
    private static final String MXPARSER_EXPECTED = "19.98843289048526";
    private static final String NOT_AN_EXPRESSION = "abc";

    private static WebServer webServer;
    private static WebClient webClient;

    @BeforeAll
    static void startTheServer() {
        webServer = Main.startServer().await();
        webClient = WebClient.builder()
                .baseUri("http://localhost:" + webServer.port())
                .build();
    }

    @AfterAll
    static void stopServer() throws Exception {
        if (webServer != null) {
            webServer.shutdown()
                    .toCompletableFuture()
                    .get(10, TimeUnit.SECONDS);
        }
    }

    void evaluate(String path, String expression, String expected) {
        webClient.post().path(path).submit(expression)
                .thenAccept(response -> {
                    assertEquals(200, response.status().code());
                    response.content().as(String.class).thenAccept(actual -> assertEquals(expected, actual));
                });
    }

    @Test
    void rootTest() {
        webClient.get().path("/").request(String.class).thenAccept(response -> assertEquals(CalcService.WELCOME, response));
    }

    @Test
    void testEvalex()  {
        evaluate("/", EXPRESSION, EVALEX_EXPECTED);
    }

    @Test
    void testEvalexN()  {
        evaluate("/", NOT_AN_EXPRESSION, "Unknown operator or function: " + NOT_AN_EXPRESSION);
    }

    @Test
    void testMxparser()  {
        evaluate("/mxparser", EXPRESSION, MXPARSER_EXPECTED);
    }

    @Test
    void testMxparserN()  {
        evaluate("/mxparser", NOT_AN_EXPRESSION, "NaN");
    }
}
