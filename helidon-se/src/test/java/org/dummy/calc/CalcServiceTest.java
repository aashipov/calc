package org.dummy.calc;

import org.junit.jupiter.api.BeforeAll;

import io.helidon.webserver.WebServer;
import io.helidon.webserver.testing.junit5.ServerTest;

@ServerTest
public class CalcServiceTest extends AppTestBase {

    private static WebServer webServer = null;

    @BeforeAll
    static void setUp() {
        webServer = Main.buildWebServer();
        webServer.start();
    }

    static void tearDown() {
        if (webServer != null) {
            webServer.stop();
        }
    }
}
