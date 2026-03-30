package org.dummy.calc

import io.undertow.Undertow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.xnio.Options

class CalcHandlerTest {

  private val EXPRESSION: String =
    "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2"
  private val EVALEX_EXPECTED: String = "19.9884"
  private val MXPARSER_EXPECTED: String = "19.98843289048526"
  private val NOT_AN_EXPRESSION: String = "abc"
  private val EXPRTK_EXPECTED: String = "19.988432890485228"

  private def withServer[T](example: cask.main.Main)(f: String => T): T = {
    val server = Undertow.builder
      .addHttpListener(8080, "0.0.0.0")
      .setSocketOption(Options.REUSE_ADDRESSES, java.lang.Boolean.TRUE)
      .setHandler(example.defaultHandler)
      .build
    server.start()
    try f("http://0.0.0.0:8080")
    finally server.stop()
  }

  private def withApp[T](f: String => T): T = withServer(org.dummy.calc.App)(f)

  private def buildUrl(host: String, path: String): String =
    if path.isEmpty then s"$host/" else s"$host/$path"

  @Test
  def welcomeTest(): Unit = withApp { host =>
    val url = buildUrl(host, "")
    val actual = requests.get(url, check = false).text()
    assertEquals(CalcHandler.WELCOME, actual)
  }

  @Test
  def evalexTest(): Unit = withApp { host =>
    val url = buildUrl(host, "")
    val actual = requests.post(url, data = EXPRESSION).text()
    assertEquals(EVALEX_EXPECTED, actual)
  }

  @Test
  def evalexNotAnExpressionTest(): Unit = withApp { host =>
    val url = buildUrl(host, "")
    val actual = requests.post(url, data = NOT_AN_EXPRESSION).text()
    assertEquals(CalcHandler.NAN, actual)
  }

  @Test
  def mxparserTest(): Unit = withApp { host =>
    val url = buildUrl(host, CalcHandler.MXPARSER_PATH)
    val actual = requests.post(url, data = EXPRESSION).text()
    assertEquals(MXPARSER_EXPECTED, actual)
  }

  @Test
  def mxparserNotAnExpressionTest(): Unit = withApp { host =>
    val url = buildUrl(host, CalcHandler.MXPARSER_PATH)
    val actual = requests.post(url, data = NOT_AN_EXPRESSION).text()
    assertEquals(CalcHandler.NAN, actual)
  }

  @Test
  def exprtkTest(): Unit = withApp { host =>
    val url = buildUrl(host, CalcHandler.EXPRTK_PATH)
    val actual = requests.post(url, data = EXPRESSION).text()
    assertEquals(EXPRTK_EXPECTED, actual)
  }

  @Test
  def exprtkNotAnExpressionTest(): Unit = withApp { host =>
    val url = buildUrl(host, CalcHandler.EXPRTK_PATH)
    val actual = requests.post(url, data = NOT_AN_EXPRESSION).text()
    assertEquals(CalcHandler.NAN, actual)
  }
}
