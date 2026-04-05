package org.dummy.calc;

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

    static HttpServer SERVER = null;
    static Future<Void> HTTP_SERVER_FUTURE = null;

    public AppTest() {
        super();
    }

    @BeforeAll
    static void setUp() {
        TEST_EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
        HTTP_SERVER_FUTURE = TEST_EXECUTOR_SERVICE.submit(() -> {
            SERVER = App.server();
            SERVER.start();
            return null;
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
