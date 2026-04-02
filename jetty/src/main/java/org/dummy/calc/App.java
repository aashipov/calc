package org.dummy.calc;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.util.thread.VirtualThreadPool;

/**
 * App.
 */
public class App {

    static final int HTTP_PORT = 8080;
    static final int MIN_THREADS = 2;

    static Server jetty() {
        int capacity = Math.max(MIN_THREADS, Runtime.getRuntime().availableProcessors());
        boolean useVirtualThreads = System.getProperty("jdk.virtualThreadScheduler.parallelism") != null;
        ThreadPool pool = useVirtualThreads ? new VirtualThreadPool(capacity) : new QueuedThreadPool(capacity, MIN_THREADS);
        Server server = new Server(pool, null, null);
        ServerConnector connector = new ServerConnector(server);
        connector.setHost("0.0.0.0");
        connector.setPort(HTTP_PORT);
        server.setConnectors(new Connector[]{connector});
        ServletContextHandler ctxHandler = new ServletContextHandler();
        ctxHandler.addServlet(CalcServlet.class, "/");
        server.setDefaultHandler(ctxHandler);
        return server;
    }

    public static void main(String[] args) throws Exception {
        Server server = jetty();
        server.start();
    }
}
