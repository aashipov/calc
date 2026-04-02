package org.dummy.calc;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

public class CalcHandler implements Handler<RoutingContext> {

    private static final Logger LOG = Logger.getLogger(CalcHandler.class.getSimpleName());

    private static final String CONTENT_TYPE = "Content-Type";
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final String DEFAULT_CHARSET_NAME = DEFAULT_CHARSET.name();
    private static final String TEXT_PLAIN = "text/plain; charset=" + DEFAULT_CHARSET_NAME;
    static final String WELCOME = "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)";
    private static final String MXPARSER = "mxparser";
    private static final Pattern MXPARSER_PATTERN = Pattern.compile(MXPARSER);
    private static final String EXPRTK = "exprtk";
    private static final Pattern EXPRTK_PATTERN = Pattern.compile(EXPRTK);
    private static final String NAN = "NaN";

    public CalcHandler() {

    }

    static void textResponse(RoutingContext ctx, String body) {
        HttpServerResponse response = ctx.response();
        response.putHeader(CONTENT_TYPE, TEXT_PLAIN);
        response.end(body);
    }

    @Override
    public void handle(RoutingContext ctx) {
        if (HttpMethod.POST.equals(ctx.request().method())) {
            String url = ctx.request().uri();
            String result = NAN;
            String expr = ctx.body().asString();
            if (expr != null) {
                try {
                    if (MXPARSER_PATTERN.matcher(url).find()) {
                        result = String.valueOf(new org.mariuszgromada.math.mxparser.Expression(expr).calculate());
                    } else if (EXPRTK_PATTERN.matcher(url).find()) {
                        result = "" + JavaExprtkAdapter.calculate(expr);
                    } else {
                        result = (new com.udojava.evalex.Expression(expr).eval()).toString();
                    }
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "Can not evaluate {0}", e.getMessage());
                }
            }
            textResponse(ctx, result);
        } else {
            textResponse(ctx, WELCOME);
        }
    }
}
