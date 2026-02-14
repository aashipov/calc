package org.dummy.calc;

import java.io.IOException;
import java.util.logging.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.IoHandlerFactory;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * App.
 */
public class App {

    private static final Logger LOGGER = Logger.getLogger(App.class.getSimpleName());
    static final int HTTP_PORT = 8080;
    private static EventLoopGroup bossGroup;
    private static EventLoopGroup workerGroup;
    private static ChannelFuture channelFuture;

    static {
        org.mariuszgromada.math.mxparser.License.iConfirmNonCommercialUse("dummy");
    }

    static class ServerInitializer extends ChannelInitializer<Channel> {

        @Override
        protected void initChannel(Channel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast(new HttpServerCodec());
            pipeline.addLast(new HttpObjectAggregator(Integer.MAX_VALUE));
            pipeline.addLast(new CalcHandler());
        }
    }

    static void launch() throws IOException, InterruptedException {
        IoHandlerFactory factory = NioIoHandler.newFactory();
        int capacity = Runtime.getRuntime().availableProcessors();
        bossGroup = new MultiThreadIoEventLoopGroup(capacity, factory);
        workerGroup = new MultiThreadIoEventLoopGroup(capacity * 2, factory);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.ERROR))
                .childHandler(new ServerInitializer())
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        channelFuture = serverBootstrap.bind(HTTP_PORT)
                .sync();
        channelFuture.channel()
                .closeFuture()
                .sync();
    }

    static void shutdown() {
        try {
            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        launch();
    }
}
