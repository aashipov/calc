package org.dummy.calc;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import com.sun.net.httpserver.HttpServer;

/**
 * Docker-free {@link App} test.
 */
class AppTest extends AppTestBase {

    static ExecutorService TEST_EXECUTOR_SERVICE = null;

    static Future HTTP_SERVER_FUTURE = null;
    static HttpServer SERVER = null;

    public AppTest() {
        super();
    }

    @BeforeAll
    static void setUp() {
        TEST_EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
        HTTP_SERVER_FUTURE = TEST_EXECUTOR_SERVICE.submit(() -> {
            try {
                SERVER = App.launch();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    @AfterAll
    static void tearDown() {
        if (SERVER != null) {
            SERVER.stop(0);
        }
        if (HTTP_SERVER_FUTURE != null) {
            HTTP_SERVER_FUTURE.cancel(true);
        }
    }
}
