package org.dummy.calc;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletInfo;
import jakarta.servlet.ServletException;

/**
 * App.
 */
public class App {

    static final String HTTP_HOST = "0.0.0.0";
    static final int HTTP_PORT = 8080;

    static Undertow undertow() throws ServletException {
        DeploymentInfo servletBuilder = Servlets.deployment()
                .setClassLoader(App.class.getClassLoader())
                .setDeploymentName("calc")
                .setContextPath("/")
                .addServlets(new ServletInfo("CalcServlet", CalcServlet.class).addMapping("/*"));

        DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
        manager.deploy();
        PathHandler path = Handlers.path(Handlers.redirect("/"))
                .addPrefixPath("/", manager.start());
        int capacity = Runtime.getRuntime().availableProcessors();
        return Undertow.builder()
                .setIoThreads(capacity)
                .setWorkerThreads(capacity)
                .addHttpListener(HTTP_PORT, HTTP_HOST)
                .setHandler(path)
                .build();
    }

    public static void main(String[] args) throws Exception {
        undertow().start();
    }
}
