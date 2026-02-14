package org.dummy.calc;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Docker-free {@link App} test.
 */
public class AppTest extends AppTestBase {
    static ExecutorService TEST_EXECUTOR_SERVICE = null;

    static Future<Void> HTTP_SERVER_FUTURE = null;

    public AppTest() {
        super();
    }

    @BeforeClass
    public static void setUp() {
        TEST_EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
        HTTP_SERVER_FUTURE = TEST_EXECUTOR_SERVICE.submit(() -> {
            try {
                App.launch();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            return null;
        });
    }

    @AfterClass
    public static void tearDown() {
        if (HTTP_SERVER_FUTURE != null) {
            HTTP_SERVER_FUTURE.cancel(true);
        }
        App.shutdown();
    }
}
