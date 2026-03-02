package org.dummy.calc;

import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class AppTest extends AppTestBase {

    static Server SERVER = null;

    @BeforeAll
    public static void setUp() throws Exception {
        TimeUnit.SECONDS.sleep(1L);
        SERVER = App.jetty();
        SERVER.start();
    }

    @AfterAll
    public static void tearDown() throws Exception {
        if (SERVER != null) {
            SERVER.stop();
        }
    }
}
