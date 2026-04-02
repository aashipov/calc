package org.dummy.calc

import org.junit.jupiter.api.BeforeAll
import zio.http.Server
import zio.Unsafe
import java.net.Inet4Address
import java.net.InetSocketAddress
import org.junit.jupiter.api.TestInstance
import java.util.concurrent.TimeUnit
import org.junit.jupiter.api.AfterAll
import zio.ExitCode

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CalcHandlerTest extends AppTestBase:

  private val executor: java.util.concurrent.ExecutorService =
    java.util.concurrent.Executors.newSingleThreadExecutor()

  @BeforeAll
  def setUp(): Unit = {
    executor.execute(() => App.main(Array.empty))
    TimeUnit.SECONDS.sleep(1L)
  }

  @AfterAll
  def tearDown(): Unit = {
    App.exit(ExitCode.success)
    executor.shutdownNow()
  }
