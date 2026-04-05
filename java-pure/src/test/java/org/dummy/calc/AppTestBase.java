package org.dummy.calc;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

/**
 * Common ancestor {@link Test}.
 */
public abstract class AppTestBase {

    protected static final Logger LOG = Logger.getLogger(AppTestBase.class.getSimpleName());
    protected static final String WELCOME = "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)";
    protected static final int DEFAULT_HTTP_PORT = 8080;
    protected static final String EMPTY_STRING = "";
    private static final String MXPARSER_ENDPOINT = "/mxparser";
    private static final String COMPLEX_EXPRESSION = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
    private static final String EVALEX_EXPECTED = "19.9884";
    private static final String MXPARSER_EXPECTED = "19.98843289048526";
    private static final String EXPRTK_EXPECTED = "19.988432890485228";
    private static final String EXPRTK_ENDPOINT = "/exprtk";
    private static final String INVALID_EXPRESSION = "abc";
    protected static final String BASE_URL = "http://0.0.0.0:" + DEFAULT_HTTP_PORT;
    private static final String NAN = "NaN";
    private static final String SIMPLE_EXPRESSION = "2+2";
    private static final String SIMPLE_EXPRESSION_RESULT_INT = "4";
    private static final String SIMPLE_EXPRESSION_RESULT_FLOAT = "4.0";

    static enum RequestMethod {
        GET, POST
    }

    static class CalcTestConfig {

        String name;
        RequestMethod requestMethod;
        String url;
        String expression;
        String expected;

        public CalcTestConfig(String name, RequestMethod requestMethod, String url, String expression, String expected) {
            this.name = name;
            this.requestMethod = requestMethod;
            this.url = url;
            this.expression = expression;
            this.expected = expected;
        }

    }

    static class CalcTestConfigProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(Arguments.of(new CalcTestConfig("welcome", RequestMethod.GET, BASE_URL, null, WELCOME)),
                    Arguments.of(new CalcTestConfig("evalexSimpleExpression", RequestMethod.POST, BASE_URL, SIMPLE_EXPRESSION, SIMPLE_EXPRESSION_RESULT_INT)),
                    Arguments.of(new CalcTestConfig("evalexComplexExpression", RequestMethod.POST, BASE_URL, COMPLEX_EXPRESSION, EVALEX_EXPECTED)),
                    Arguments.of(new CalcTestConfig("evalexInvalidExpression", RequestMethod.POST, BASE_URL, INVALID_EXPRESSION, NAN)),
                    Arguments.of(new CalcTestConfig("mxparserSimpleExpression", RequestMethod.POST, BASE_URL + MXPARSER_ENDPOINT, SIMPLE_EXPRESSION, SIMPLE_EXPRESSION_RESULT_FLOAT)),
                    Arguments.of(new CalcTestConfig("mxparserComplexExpression", RequestMethod.POST, BASE_URL + MXPARSER_ENDPOINT, COMPLEX_EXPRESSION, MXPARSER_EXPECTED)),
                    Arguments.of(new CalcTestConfig("mxparserInvalidExpression", RequestMethod.POST, BASE_URL + MXPARSER_ENDPOINT, INVALID_EXPRESSION, NAN)),
                    Arguments.of(new CalcTestConfig("exprtkSimpleExpression", RequestMethod.POST, BASE_URL + EXPRTK_ENDPOINT, SIMPLE_EXPRESSION, SIMPLE_EXPRESSION_RESULT_FLOAT)),
                    Arguments.of(new CalcTestConfig("exprtkComplexExpression", RequestMethod.POST, BASE_URL + EXPRTK_ENDPOINT, COMPLEX_EXPRESSION, EXPRTK_EXPECTED)),
                    Arguments.of(new CalcTestConfig("exprtkInvalidExpression", RequestMethod.POST, BASE_URL + EXPRTK_ENDPOINT, INVALID_EXPRESSION, NAN))
            );
        }
    }

    @ParameterizedTest
    @ArgumentsSource(CalcTestConfigProvider.class)
    void test(CalcTestConfig tt) {
        if (RequestMethod.GET == tt.requestMethod) {
            String actual = doGet(tt.url);
            assertEquals(tt.expected, actual);
        } else {
            evaluatePost(tt.url, tt.expression, tt.expected);
        }
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
