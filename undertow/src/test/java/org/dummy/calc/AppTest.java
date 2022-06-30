package org.dummy.calc;

import io.undertow.Undertow;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.concurrent.TimeUnit;

public class AppTest extends AppTestBase {
    static Undertow SERVER = null;

    @BeforeClass
    public static void setUp() throws Exception {
        TimeUnit.SECONDS.sleep(1L);
        SERVER = App.undertow();
        SERVER.start();
    }

    @AfterClass
    public static void tearDown() {
        if (SERVER != null) {
            SERVER.stop();
        }
    }
}
