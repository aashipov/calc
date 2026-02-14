package org.dummy.calc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpServer;

/**
 * App.
 */
public class App {

    static final int HTTP_PORT = 8080;
    private static final ExecutorService HTTP_EXECUTOR_SERVICE
            = Executors.newWorkStealingPool(Math.max(Runtime.getRuntime().availableProcessors(), 2) * 8);
    private static final Logger LOGGER = Logger.getLogger(App.class.getSimpleName());

    static HttpServer launch() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(HTTP_PORT), 0);
        server.createContext("/", new CommonHandler());
        server.setExecutor(HTTP_EXECUTOR_SERVICE);
        server.start();
        return server;
    }

    public static void main(String[] args) throws IOException {
        launch();
    }
}
