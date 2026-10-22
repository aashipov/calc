package org.dummy.calc;

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
    static final String NAN = "NaN";

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
                result = "" + JavaExprAdapter.calculate(expr, url);
            } catch (Exception ex) {
                LOGGER.info(String.format("Can not evaluate expression %s because of %s", expr, ex.getMessage()));
                result = NAN;
            }
        }
        textResponse(ctx, result);
    }
}
