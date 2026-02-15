package org.dummy.calc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class CalcHandler implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(CalcHandler.class.getSimpleName());
    private static final String EXPRTK_SHARED_LIBRARY_NAME = "libc-exprtk-adapter.so";
    private static final String EXPRTK_SHARED_LIBRARY_FUNCTION = "calculate";
    private static final String POST = "POST";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final String DEFAULT_CHARSET_NAME = DEFAULT_CHARSET.name();
    private static final String TEXT_PLAIN = "text/plain; charset=" + DEFAULT_CHARSET_NAME;
    private static final int OK = 200;
    private static final String NO_REQUEST_METHOD = "No request method";
    static final String WELCOME = "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)";
    private static final String MXPARSER = "mxparser";
    private static final String EXPRTK = "exprtk";
    private static final String JNI = "JNI";
    private static final Boolean IS_C_EXPRTK_ADAPTER_HARNESS_JNI = isCExprtkAdapterJniHarness();

    static String getCExprtkAdapterHarnessName() {
        return (System.getenv("C_EXPRTK_ADAPTER_HARNESS") == null || System.getenv("C_EXPRTK_ADAPTER_HARNESS").isBlank()) ? JNI : System.getenv("C_EXPRTK_ADAPTER_HARNESS");
    }

    static boolean isCExprtkAdapterJniHarness() {
        return JNI.equalsIgnoreCase(getCExprtkAdapterHarnessName());
    }

    static double calculateFfm(String expression) throws Throwable {
        FunctionDescriptor functionDescriptor = FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS);
        Linker linker = Linker.nativeLinker();
        Arena arena = Arena.ofAuto();
        SymbolLookup lookup = SymbolLookup.libraryLookup(EXPRTK_SHARED_LIBRARY_NAME, arena);
        Optional<MemorySegment> memorySegmentOptional = lookup.find(EXPRTK_SHARED_LIBRARY_FUNCTION);
        if (memorySegmentOptional.isEmpty()) {
            throw new IllegalStateException(EXPRTK_SHARED_LIBRARY_NAME + " or its function " + EXPRTK_SHARED_LIBRARY_FUNCTION + " not found");
        }
        MemorySegment funcArg = arena.allocateFrom(expression);
        return (double) linker.downcallHandle(memorySegmentOptional.get(), functionDescriptor).invoke(funcArg);
    }

    public CalcHandler() {
        org.mariuszgromada.math.mxparser.License.iConfirmNonCommercialUse("dummy");
        LOGGER.log(Level.INFO, "C_EXPRTK_ADAPTER_HARNESS {0}", getCExprtkAdapterHarnessName());
    }

    @Override
    public void handle(HttpExchange exchange) {
        String requestMethod = exchange.getRequestMethod();
        if (isBlank(requestMethod)) {
            textResponse(exchange, OK, NO_REQUEST_METHOD);
        } else {
            if (POST.equalsIgnoreCase(exchange.getRequestMethod())) {
                try (InputStream inputStream = exchange.getRequestBody(); ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                    inputStream.transferTo(byteArrayOutputStream);
                    String expr = byteArrayOutputStream.toString(DEFAULT_CHARSET);
                    String result;
                    if (exchange.getRequestURI().toString().contains(MXPARSER)) {
                        result = String.valueOf(new org.mariuszgromada.math.mxparser.Expression(expr).calculate());
                    } else if (exchange.getRequestURI().toString().contains(EXPRTK)) {
                        if (IS_C_EXPRTK_ADAPTER_HARNESS_JNI) {
                            result = "" + ExprtkAdapter.calculate(expr);
                        } else {
                            result = "" + calculateFfm(expr);
                        }
                    } else {
                        result = (new com.udojava.evalex.Expression(expr).eval()).toString();
                    }
                    textResponse(exchange, OK, result);
                } catch (Throwable e) {
                    LOGGER.log(Level.SEVERE, "Can not evaluate {0}", e.getMessage());
                    textResponse(exchange, OK, e.getMessage());
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
