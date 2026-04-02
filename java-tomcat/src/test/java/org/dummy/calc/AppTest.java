package org.dummy.calc;

import java.util.concurrent.TimeUnit;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class AppTest extends AppTestBase {

    static Tomcat SERVER = null;

    @BeforeAll
    public static void setUp() throws InterruptedException, LifecycleException {
        TimeUnit.SECONDS.sleep(1L);
        SERVER = App.tomcat();
        SERVER.start();
    }

    @AfterAll
    public static void tearDown() throws LifecycleException {
        if (SERVER != null) {
            SERVER.stop();
        }
    }
}
