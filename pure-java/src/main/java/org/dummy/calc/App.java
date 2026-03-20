package org.dummy.calc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

/**
 * App.
 */
public class App {

    private static final ExecutorService HTTP_EXECUTOR_SERVICE
            = buildExecutorService();
    static final int HTTP_PORT = 8080;

    /**
     * {@link java.lang.VirtualThread#createDefaultScheduler}.
     *
     * @return {@link ExecutorService}
     */
    // https://github.com/openjdk/jdk21u/blob/master/src/java.base/share/classes/java/lang/VirtualThread.java#L1152
    // java -Djdk.virtualThreadScheduler.parallelism=`getconf _NPROCESSORS_ONLN` -jar target/calc-shaded.jar
    // no difference in CPU load/socket error count with a consumer grade PC
    private static ExecutorService buildExecutorService() {
        int capacity = Math.max(2, Runtime.getRuntime().availableProcessors());
        return System.getProperty("jdk.virtualThreadScheduler.parallelism") == null ? Executors.newFixedThreadPool(capacity) : Executors.newVirtualThreadPerTaskExecutor();
    }

    static HttpServer launch() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(HTTP_PORT), 0);
        server.createContext("/", new CalcHandler());
        server.setExecutor(HTTP_EXECUTOR_SERVICE);
        server.start();
        return server;
    }

    public static void main(String[] args) throws IOException {
        launch();
    }
}
