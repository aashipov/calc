package org.dummy.calc

import io.ktor.server.netty.*
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class AppTest : AppTestBase() {

    companion object {
        private val serverExecutor: ExecutorService = Executors.newSingleThreadExecutor()

        @JvmStatic
        @BeforeAll
        fun startServer() {
            serverExecutor.submit {
                EngineMain.main(arrayOf("src/main/resources/application.yaml"))
            }
            TimeUnit.SECONDS.sleep(1L)
        }

        @JvmStatic
        @AfterAll
        fun stopServer() {
            serverExecutor.shutdown()
        }
    }
}
