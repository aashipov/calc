package org.dummy.calc;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.concurrent.TimeUnit;

public class AppTest extends AppTestBase {
    static Tomcat SERVER = null;

    @BeforeClass
    public static void setUp() throws InterruptedException, LifecycleException {
        TimeUnit.SECONDS.sleep(1L);
        SERVER = App.tomcat();
        SERVER.start();
    }

    @AfterClass
    public static void tearDown() throws LifecycleException {
        if (SERVER != null) {
            SERVER.stop();
        }
    }
}
