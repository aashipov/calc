package org.dummy.calc;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

/**
 * App.
 */
public class App {
    static final int HTTP_PORT = 8080;

    static Server jetty() {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(HTTP_PORT);
        server.setConnectors(new Connector[]{connector});
        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServletWithMapping(CalcServlet.class, "/");
        server.setHandler(servletHandler);
        return server;
    }

    public static void main(String[] args) throws Exception {
        Server server = jetty();
        server.start();
    }
}
