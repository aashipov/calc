package org.dummy.calc;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dummy.calc.JavaExprAdapterTest.TestConfig;
import org.dummy.calc.JavaExprAdapterTest.TestConfig.TestConfigProvider;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

/**
 * Common ancestor {@link Test}.
 */
public abstract class AppTestBase {

    protected static final Logger LOG = Logger.getLogger(AppTestBase.class.getSimpleName());
    protected static final String WELCOME = "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)";
    protected static final int DEFAULT_HTTP_PORT = 8080;
    protected static final String EMPTY_STRING = "";
    protected static final String BASE_URL = "http://0.0.0.0:" + DEFAULT_HTTP_PORT;

    @ParameterizedTest
    @ArgumentsSource(TestConfigProvider.class)
    void testPost(TestConfig tc) {
        String url = BASE_URL + "/" + (tc.mayBeAdapteeName == null || tc.mayBeAdapteeName.isBlank() ? "" : tc.mayBeAdapteeName);
        evaluatePost(url, "" + tc.expression, "" + tc.expected);
    }

    @Test
    void testGet() {
        String actual = doGet(BASE_URL);
        assertEquals(WELCOME, actual);
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
