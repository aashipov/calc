package org.dummy.calc;

import java.io.IOException;

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
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * App.
 */
public class App {

    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(App.class);
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
        bossGroup = new MultiThreadIoEventLoopGroup(1, factory);
        workerGroup = new MultiThreadIoEventLoopGroup(Math.max(1, capacity) * 8, factory);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ServerInitializer())
                .option(ChannelOption.SO_BACKLOG, 8_192);
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
            LOGGER.error(e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        launch();
    }
}
