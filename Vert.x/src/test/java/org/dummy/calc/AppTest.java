package org.dummy.calc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

/**
 * Docker-free {@link App} test.
 */
class AppTest extends AppTestBase {

    static ExecutorService TEST_EXECUTOR_SERVICE = null;

    static Future HTTP_SERVER_FUTURE = null;

    public AppTest() {
        super();
    }

    @BeforeAll
    static void setUp() throws InterruptedException {
        TEST_EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
        HTTP_SERVER_FUTURE = TEST_EXECUTOR_SERVICE.submit(App::launch);
        TimeUnit.SECONDS.sleep(1L);
    }

    @AfterAll
    static void tearDown() {
        if (HTTP_SERVER_FUTURE != null) {
            HTTP_SERVER_FUTURE.cancel(true);
        }
    }
}
