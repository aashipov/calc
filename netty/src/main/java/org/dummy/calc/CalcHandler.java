package org.dummy.calc;

import java.util.regex.Pattern;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class CalcHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(CalcHandler.class);
    private static final String WELCOME = "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)";
    private static final String MXPARSER = "mxparser";
    private static final Pattern MXPARSER_PATTERN = Pattern.compile(MXPARSER);
    private static final String EXPRTK = "exprtk";
    private static final Pattern EXPRTK_PATTERN = Pattern.compile(EXPRTK);

    static void textResponse(ChannelHandlerContext ctx, String responseText) {
        FullHttpResponse httpResponse = new DefaultFullHttpResponse(HTTP_1_1, OK,
                Unpooled.copiedBuffer(responseText, CharsetUtil.UTF_8));
        httpResponse.headers()
                .set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        httpResponse.headers()
                .setInt(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content()
                        .readableBytes());
        httpResponse.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        ctx.writeAndFlush(httpResponse).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.info(cause.getMessage());
        ctx.flush();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
        String result = WELCOME;
        if (HttpMethod.POST.equals(msg.method())) {
            String url = msg.uri();
            String expr = msg.content().toString(CharsetUtil.UTF_8);
            try {
                if (MXPARSER_PATTERN.matcher(url).find()) {
                    result = String.valueOf(new org.mariuszgromada.math.mxparser.Expression(expr).calculate());
                } else if (EXPRTK_PATTERN.matcher(url).find()) {
                    result = "" + JavaExprtkAdapter.calculate(expr);
                } else {
                    result = (new com.udojava.evalex.Expression(expr).eval()).toString();
                }
            } catch (Exception ex) {
                LOGGER.info(String.format("Can not evaluate expression %s because of %s", expr, ex.getMessage()));
                result = ex.getMessage();
            }
        }
        textResponse(ctx, result);
    }
}
