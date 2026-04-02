package org.dummy.calc

import java.net.InetSocketAddress
import java.nio.channels.ServerSocketChannel

import org.junit.jupiter.api.{AfterAll, BeforeAll, TestInstance}
import java.util.concurrent.TimeUnit

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CalcHandlerTest extends AppTestBase {

  private val executor: java.util.concurrent.ExecutorService =
    java.util.concurrent.Executors.newSingleThreadExecutor()

  @BeforeAll
  def setUp(): Unit = {
    executor.execute(() => App.main(Array.empty))
    TimeUnit.SECONDS.sleep(1L)
  }

  @AfterAll
  def tearDown(): Unit = {
    executor.shutdownNow()
  }
}
