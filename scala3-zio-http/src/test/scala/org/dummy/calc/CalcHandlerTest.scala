package org.dummy.calc

import org.junit.jupiter.api.Test
import zio.http.{Client, Request, TestClient, URL}
import zio.{Unsafe, ZIO}
import org.junit.jupiter.api.Assertions

class CalcHandlerTest:

  private val EXPRESSION: String =
    "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2"
  private val EVALEX_EXPECTED: String = "19.9884"
  private val MXPARSER_EXPECTED: String = "19.98843289048526"
  private val NOT_AN_EXPRESSION: String = "abc"
  private val EXPRTK_EXPECTED: String = "19.988432890485228"

  private def doRequest(req: Request): String =
    Unsafe.unsafe { implicit u =>
      zio.Runtime.default.unsafe
        .run {
          (for
            client <- ZIO.service[Client]
            _ <- TestClient.addRoutes(CalcHandler.routes)
            response <- client.batched(req)
            body <- response.body.asString
          yield body).provide(TestClient.layer)
        }
        .getOrElse(e => throw e.squash)
    }

  @Test
  def welcomeTest(): Unit = {
    val req = Request.get(URL.root)
    val body = doRequest(req)
    Assertions.assertEquals(CalcHandler.WELCOME, body)
  }

  @Test
  def evalexTest(): Unit = {
    val req = Request.post(URL.root, zio.http.Body.fromString(EXPRESSION))
    val body = doRequest(req)
    Assertions.assertEquals(EVALEX_EXPECTED, body)
  }

  @Test
  def evalexNotAnExpressionTest(): Unit = {
    val req =
      Request.post(URL.root, zio.http.Body.fromString(NOT_AN_EXPRESSION))
    val body = doRequest(req)
    Assertions.assertEquals(CalcHandler.NAN, body)
  }

  @Test
  def mxparserTest(): Unit = {
    val req = Request.post(
      URL.root / CalcHandler.MXPARSER_PATH,
      zio.http.Body.fromString(EXPRESSION)
    )
    val body = doRequest(req)
    Assertions.assertEquals(MXPARSER_EXPECTED, body)
  }

  @Test
  def mxparserNotAnExpressionTest(): Unit = {
    val req = Request.post(
      URL.root / CalcHandler.MXPARSER_PATH,
      zio.http.Body.fromString(NOT_AN_EXPRESSION)
    )
    val body = doRequest(req)
    Assertions.assertEquals(CalcHandler.NAN, body)
  }

  @Test
  def exprtkTest(): Unit = {
    val req = Request.post(
      URL.root / CalcHandler.EXPRTK_PATH,
      zio.http.Body.fromString(EXPRESSION)
    )
    val body = doRequest(req)
    Assertions.assertEquals(EXPRTK_EXPECTED, body)
  }

  @Test
  def exprtkNotAnExpressionTest(): Unit = {
    val req = Request.post(
      URL.root / CalcHandler.EXPRTK_PATH,
      zio.http.Body.fromString(NOT_AN_EXPRESSION)
    )
    val body = doRequest(req)
    Assertions.assertEquals(CalcHandler.NAN, body)
  }
