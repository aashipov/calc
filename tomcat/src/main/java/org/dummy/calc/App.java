package org.dummy.calc;

import java.nio.file.Paths;
import java.util.logging.Logger;

import org.apache.catalina.Context;
import org.apache.catalina.Executor;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardThreadExecutor;
import org.apache.catalina.core.StandardVirtualThreadExecutor;
import org.apache.catalina.startup.Tomcat;

/**
 * App.
 */
public class App {

    private static final Logger LOG = Logger.getLogger(App.class.getSimpleName());
    private static final String CALC = "calc";
    static final int HTTP_PORT = 8080;

    static Executor buildStandardThread() {
        StandardThreadExecutor executor = new StandardThreadExecutor();
        executor.setName(CALC);
        int capacity = Math.max(2, Runtime.getRuntime().availableProcessors());
        executor.setMinSpareThreads(1);
        executor.setMaxThreads(capacity);
        return executor;
    }

    // java -Djdk.virtualThreadScheduler.parallelism=`getconf _NPROCESSORS_ONLN` -jar target/calc-shaded.jar
    static Executor buildVirtualThread() {
        StandardVirtualThreadExecutor executor = new StandardVirtualThreadExecutor();
        executor.setName(CALC);
        return executor;
    }

    /**
     * @see
     * <a href="https://www.codejava.net/servers/tomcat/how-to-embed-tomcat-server-into-java-web-applications">CodeJava</a>
     */
    static Tomcat tomcat() {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("tmp");
        tomcat.setPort(HTTP_PORT);

        String contextPath = "";
        Context context = tomcat.addContext(contextPath, Paths.get(".").toAbsolutePath().toString());
        CalcServlet servlet = new CalcServlet();

        String servletName = "CalcServlet";
        tomcat.addServlet(contextPath, servletName, servlet);
        context.addServletMappingDecoded("/", servletName);

        Executor executor = System.getProperty("jdk.virtualThreadScheduler.parallelism") == null ? buildStandardThread() : buildVirtualThread();
        try {
            executor.start();
        } catch (LifecycleException ex) {
            LOG.severe(ex.getMessage());
            System.exit(1);
        }

        tomcat.getConnector().getProtocolHandler().setExecutor(executor);
        return tomcat;
    }

    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = tomcat();
        tomcat.start();
        tomcat.getServer().await();
    }
}
