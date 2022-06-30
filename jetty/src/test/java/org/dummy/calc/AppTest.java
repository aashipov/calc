package org.dummy.calc;

import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.concurrent.TimeUnit;

public class AppTest extends AppTestBase {
    static Server SERVER = null;

    @BeforeClass
    public static void setUp() throws Exception {
        TimeUnit.SECONDS.sleep(1L);
        SERVER = App.jetty();
        SERVER.start();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (SERVER != null) {
            SERVER.stop();
        }
    }
}
