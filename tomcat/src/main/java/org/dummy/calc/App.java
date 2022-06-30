package org.dummy.calc;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.nio.file.Paths;

/**
 * App.
 */
public class App {
    static final int HTTP_PORT = 8080;

    /**
     * @see <a href="https://www.codejava.net/servers/tomcat/how-to-embed-tomcat-server-into-java-web-applications">CodeJava</a>
     */
    static Tomcat tomcat() {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("tmp");
        tomcat.setPort(HTTP_PORT);
        tomcat.getConnector();

        String contextPath = "";
        Context context = tomcat.addContext(contextPath, Paths.get(".").toAbsolutePath().toString());
        CalcServlet servlet = new CalcServlet();

        String servletName = "CalcServlet";
        tomcat.addServlet(contextPath, servletName, servlet);
        context.addServletMappingDecoded("/", servletName);
        return tomcat;
    }

    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = tomcat();
        tomcat.start();
        tomcat.getServer().await();
    }
}
