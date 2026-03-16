package org.dummy.calc

import org.junit.runner.RunWith
import zio.ZIO
import zio.http.{Client, Request, TestClient, URL}
import zio.test.junit.ZTestJUnitRunner
import zio.test.{TestResult, ZIOSpecDefault, assertTrue}

@RunWith(classOf[ZTestJUnitRunner])
class CalcHandlerTest extends ZIOSpecDefault {

  private val EXPRESSION: String = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2"
  private val EVALEX_EXPECTED: String = "19.9884"
  private val MXPARSER_EXPECTED: String = "19.98843289048526"
  private val NOT_AN_EXPRESSION: String = "abc"
  private val EXPRTK_EXPECTED: String = "19.988432890485228"
  private val NAN: String = "NaN"

  private def doRequest(routes: zio.http.Routes[Any, Nothing], req: Request, expected: String): ZIO[TestClient & Client, Throwable, TestResult] = {
    for {
      client <- ZIO.service[Client]
      _ <- TestClient.addRoutes {
        routes
      }
      response <- client.batched(req)
      body <- response.body.asString
    } yield assertTrue(expected == body)
  }

  def spec = suite("Dummy ZIO Test Suite")(

    test("welcomeTest") {
      doRequest(CalcHandler.routes, Request.get(URL.root), CalcHandler.WELCOME)
    }.provide(TestClient.layer),

    test("evalexTest") {
      doRequest(CalcHandler.routes, Request.post(URL.root, zio.http.Body.fromString(EXPRESSION)), EVALEX_EXPECTED)
    }.provide(TestClient.layer),

    test("evalexNotAnExpressionTest") {
      doRequest(CalcHandler.routes, Request.post(URL.root, zio.http.Body.fromString(NOT_AN_EXPRESSION)), "")
    }.provide(TestClient.layer),

    test("mxparserTest") {
      doRequest(CalcHandler.routes, Request.post(URL.root / CalcHandler.MXPARSER_PATH, zio.http.Body.fromString(EXPRESSION)), MXPARSER_EXPECTED)
    }.provide(TestClient.layer),

    test("mxparserNotAnExpressionTest") {
      doRequest(CalcHandler.routes, Request.post(URL.root / CalcHandler.MXPARSER_PATH, zio.http.Body.fromString(NOT_AN_EXPRESSION)), NAN)
    }.provide(TestClient.layer),

    test("exprtkTest") {
      doRequest(CalcHandler.routes, Request.post(URL.root / CalcHandler.EXPRTK_PATH, zio.http.Body.fromString(EXPRESSION)), EXPRTK_EXPECTED)
    }.provide(TestClient.layer),

    test("exprtkNotAnExpressionTest") {
      doRequest(CalcHandler.routes, Request.post(URL.root / CalcHandler.EXPRTK_PATH, zio.http.Body.fromString(NOT_AN_EXPRESSION)), NAN)
    }.provide(TestClient.layer),
  )
}
