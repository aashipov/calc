package org.dummy.calc;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import io.undertow.Undertow;

public class AppTest extends AppTestBase {

    static Undertow SERVER = null;

    @BeforeAll
    public static void setUp() throws Exception {
        TimeUnit.SECONDS.sleep(1L);
        SERVER = App.undertow();
        SERVER.start();
    }

    @AfterAll
    public static void tearDown() {
        if (SERVER != null) {
            SERVER.stop();
        }
    }
}
