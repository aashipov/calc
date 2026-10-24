package org.dummy.calc;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

public class CalcHandler implements Handler<RoutingContext> {

    private static final String CONTENT_TYPE = "Content-Type";
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final String DEFAULT_CHARSET_NAME = DEFAULT_CHARSET.name();
    private static final String TEXT_PLAIN = "text/plain; charset=" + DEFAULT_CHARSET_NAME;
    static final String WELCOME = "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)";

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
            String expr = ctx.body().asString();
            String result = "" + JavaExprAdapter.calculate(expr, url);
            textResponse(ctx, result);
        } else {
            textResponse(ctx, WELCOME);
        }
    }
}
