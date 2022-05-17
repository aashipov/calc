package org.dummy.calc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CalcControllerTest {

    private static final String EXPRESSION = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
    private static final String EVALEX_EXPECTED = "19.9884";
    private static final String MXPARSER_EXPECTED = "19.98843289048526";
    private static final String NOT_AN_EXPRESSION = "abc";


    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    static String getTestAddress(int port) throws UnknownHostException {
        return "http://" + InetAddress.getLocalHost().getHostName() + ":" + port;
    }

    void evaluate(String path, String expression, String expected) throws UnknownHostException {
        ResponseEntity<String> responseEntity = this.testRestTemplate.postForEntity(getTestAddress(this.port) + path, expression, String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful(), "HTTP success");
        assertTrue(responseEntity.getBody().contains(expected), expected);
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
