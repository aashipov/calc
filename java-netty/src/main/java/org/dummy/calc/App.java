package org.dummy.calc;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.util.concurrent.DefaultThreadFactory;
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
            ch.pipeline()
                    .addLast(new HttpServerCodec())
                    .addLast(new HttpObjectAggregator(65_536))
                    .addLast(new CalcHandler());
        }
    }

    static void launch() throws IOException, InterruptedException {
        ThreadFactory factory = new DefaultThreadFactory("calc");
        boolean useVirtualThreads = System.getProperty("jdk.virtualThreadScheduler.parallelism") != null;
        Class<? extends io.netty.channel.ServerChannel> channelClass = useVirtualThreads
                ? EpollServerSocketChannel.class
                : NioServerSocketChannel.class;
        EventLoopGroup bossGroupLocal = useVirtualThreads
                ? new EpollEventLoopGroup(1, factory)
                : new NioEventLoopGroup(1, factory);
        int capacity = Math.max(2, Runtime.getRuntime().availableProcessors());
        EventLoopGroup workerGroupLocal = useVirtualThreads
                ? new EpollEventLoopGroup(capacity, Executors.newVirtualThreadPerTaskExecutor())
                : new NioEventLoopGroup(capacity, factory);
        bossGroup = bossGroupLocal;
        workerGroup = workerGroupLocal;
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(channelClass)
                .childHandler(new ServerInitializer())
                .option(ChannelOption.SO_BACKLOG, 8_192)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_REUSEADDR, true);
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

    // java -Djdk.virtualThreadScheduler.parallelism=`getconf _NPROCESSORS_ONLN` -jar target/calc-shaded.jar
    public static void main(String[] args) throws Exception {
        launch();
    }
}
