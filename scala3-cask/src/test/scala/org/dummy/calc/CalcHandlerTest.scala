package org.dummy.calc

import io.undertow.Undertow
import org.xnio.Options
import utest._

class CalcHandlerTest extends TestSuite {

  private val EXPRESSION: String =
    "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2"
  private val EVALEX_EXPECTED: String = "19.9884"
  private val MXPARSER_EXPECTED: String = "19.98843289048526"
  private val NOT_AN_EXPRESSION: String = "abc"
  private val EXPRTK_EXPECTED: String = "19.988432890485228"

  def withServer[T](example: cask.main.Main)(f: String => T): T = {
    val server = Undertow.builder
      .addHttpListener(8081, "0.0.0.0")
      .setSocketOption(Options.REUSE_ADDRESSES, java.lang.Boolean.TRUE)
      .setHandler(example.defaultHandler)
      .build
    server.start()
    val res =
      try f("http://0.0.0.0:8081")
      finally server.stop()
    res
  }

  val tests = Tests {
    test("CalcHandlerTest") - withServer(org.dummy.calc.App) { host =>
      requests.get(s"$host/", check = false).text() ==> CalcHandler.WELCOME

      requests.post(s"$host/", data = EXPRESSION).text() ==> EVALEX_EXPECTED

      requests
        .post(s"$host/", data = NOT_AN_EXPRESSION)
        .text() ==> CalcHandler.NAN

      requests
        .post(s"$host/" + CalcHandler.MXPARSER_PATH, data = EXPRESSION)
        .text() ==> MXPARSER_EXPECTED

      requests
        .post(s"$host/" + CalcHandler.MXPARSER_PATH, data = NOT_AN_EXPRESSION)
        .text() ==> CalcHandler.NAN

      requests
        .post(s"$host/" + CalcHandler.EXPRTK_PATH, data = EXPRESSION)
        .text() ==> EXPRTK_EXPECTED

      requests
        .post(s"$host/" + CalcHandler.EXPRTK_PATH, data = NOT_AN_EXPRESSION)
        .text() ==> CalcHandler.NAN
    }
  }
}
