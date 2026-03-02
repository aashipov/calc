package org.dummy.calc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class CalcHandler implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(CalcHandler.class.getSimpleName());
    private static final String POST = "POST";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final String DEFAULT_CHARSET_NAME = DEFAULT_CHARSET.name();
    private static final String TEXT_PLAIN = "text/plain; charset=" + DEFAULT_CHARSET_NAME;
    private static final int OK = 200;
    private static final String NO_REQUEST_METHOD = "No request method";
    static final String WELCOME = "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)";
    private static final String MXPARSER = "mxparser";
    private static final Pattern MXPARSER_PATTERN = Pattern.compile(MXPARSER);
    private static final String EXPRTK = "exprtk";
    private static final Pattern EXPRTK_PATTERN = Pattern.compile(EXPRTK);
    private static final String NAN = "NaN";

    public CalcHandler() {
        org.mariuszgromada.math.mxparser.License.iConfirmNonCommercialUse("dummy");
    }

    @Override
    public void handle(HttpExchange exchange) {
        String requestMethod = exchange.getRequestMethod();
        if (isBlank(requestMethod)) {
            textResponse(exchange, OK, NO_REQUEST_METHOD);
        } else {
            if (POST.equalsIgnoreCase(exchange.getRequestMethod())) {
                String url = exchange.getRequestURI().toString();
                String result = NAN;
                try (InputStream inputStream = exchange.getRequestBody(); ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                    inputStream.transferTo(byteArrayOutputStream);
                    String expr = byteArrayOutputStream.toString(DEFAULT_CHARSET);
                    if (MXPARSER_PATTERN.matcher(url).find()) {
                        result = String.valueOf(new org.mariuszgromada.math.mxparser.Expression(expr).calculate());
                    } else if (EXPRTK_PATTERN.matcher(url).find()) {
                        result = "" + JavaExprtkAdapter.calculate(expr);
                    } else {
                        result = (new com.udojava.evalex.Expression(expr).eval()).toString();
                    }
                } catch (Throwable e) {
                    LOGGER.log(Level.SEVERE, "Can not evaluate {0}", e.getMessage());
                    result = e.getMessage();
                } finally {
                    textResponse(exchange, OK, result);
                }
            } else {
                textResponse(exchange, OK, WELCOME);
            }
        }
    }

    /**
     * Is {@link CharSequence} blank?.
     *
     * @param cs {@link CharSequence}
     * @return is blank?
     */
    static boolean isBlank(final CharSequence cs) {
        if (null != cs) {
            final int strLen = cs.length();
            if (0 != strLen) {
                for (int i = 0; i < strLen; i++) {
                    if (!Character.isWhitespace(cs.charAt(i))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Send text response.
     *
     * @param exchange {@link HttpExchange}
     * @param rCode status code
     * @param msg message
     */
    static void textResponse(HttpExchange exchange, int rCode, String msg) {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.getResponseHeaders().add(CONTENT_TYPE, TEXT_PLAIN);
            exchange.sendResponseHeaders(rCode, msg.length());
            os.write(msg.getBytes(DEFAULT_CHARSET));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error sending plain text response {0}", e.getMessage());
        } finally {
            exchange.close();
        }
    }
}
