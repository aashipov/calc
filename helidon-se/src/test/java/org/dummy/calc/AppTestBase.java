package org.dummy.calc;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Common ancestor {@link Test}.
 */
public abstract class AppTestBase {

    protected static final Logger LOG = Logger.getLogger(AppTestBase.class.getSimpleName());
    protected static final String WELCOME = "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)";
    protected static final int DEFAULT_HTTP_PORT = 8080;
    protected static final String EMPTY_STRING = "";
    private static final String MXPARSER_ENDPOINT = "/mxparser";
    private static final String EXPRESSION = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
    private static final String EVALEX_EXPECTED = "19.9884";
    private static final String MXPARSER_EXPECTED = "19.98843289048526";
    private static final String EXPRTK_EXPECTED = "19.988432890485228";
    private static final String EXPRTK_ENDPOINT = "/exprtk";
    private static final String NOT_AN_EXPRESSION = "abc";
    private static final String NAN = "NaN";
    protected static final String BASE_URL = "http://0.0.0.0:" + DEFAULT_HTTP_PORT;

    @Test
    public void welcomeTest() {
        String actual = doGet(BASE_URL);
        assertEquals(WELCOME, actual);
    }

    @Test
    public void evalexTest() {
        evaluatePost(BASE_URL, EXPRESSION, EVALEX_EXPECTED);
    }

    @Test
    public void evalexNotAnExpressionTest() {
        evaluatePost(BASE_URL, NOT_AN_EXPRESSION, "Unknown operator or function: " + NOT_AN_EXPRESSION);
    }

    @Test
    public void mxparserTest() {
        evaluatePost(BASE_URL + MXPARSER_ENDPOINT, EXPRESSION, MXPARSER_EXPECTED);
    }

    @Test
    public void mxparserNotAnExpressionTest() {
        evaluatePost(BASE_URL + MXPARSER_ENDPOINT, NOT_AN_EXPRESSION, NAN);
    }

    @Test
    public void exprtkExpressionTest() {
        evaluatePost(BASE_URL + EXPRTK_ENDPOINT, EXPRESSION, EXPRTK_EXPECTED);
    }

    @Test
    public void exprtkNotAnExpressionTest() {
        evaluatePost(BASE_URL + EXPRTK_ENDPOINT, NOT_AN_EXPRESSION, NAN);
    }

    /**
     * Perform HTTP GET via {@link HttpClient}.
     *
     * @param url URL
     * @return {@link String} of {@link HttpResponse#body()}
     */
    static String doGet(String url) {
        String body = EMPTY_STRING;
        try {
            final HttpClient httpClient = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
            body = response.body();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Can not HTTP GET " + url, e);
        }
        return body;
    }

    /**
     * Perform HTTP POST via {@link HttpClient}.
     *
     * @param url URL
     * @param content content
     * @return {@link String} of {@link HttpResponse#body()}
     */
    static String doPost(String url, String content) {
        String body = EMPTY_STRING;
        try {
            final HttpClient httpClient = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .POST(HttpRequest.BodyPublishers.ofString(content, StandardCharsets.UTF_8))
                    .setHeader("Content-Type", "text/plain")
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
            body = response.body();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Can not HTTP POST URL " + url + " with content " + content, e);
        }
        return body;
    }

    void evaluatePost(String path, String expression, String expected) {
        String actual = doPost(path, expression);
        assertEquals(expected, actual);
    }
}
