package org.dummy.calc

import zio.http.Method
import zio.http.Response
import zio.http.Request
import zio.http.handler

object CalcHandler {

  org.mariuszgromada.math.mxparser.License.iConfirmNonCommercialUse("dummy")

  private val WELCOME =
    "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)"

  private val welcome =
    Method.GET / zio.http.trailing -> handler(Response.text(WELCOME))

  private val viaEvalex =
    Method.POST / zio.http.trailing ->
      handler { (req: Request) =>
        for {
          expr <- req.body.asString
          result = com.udojava.evalex.Expression(expr).eval()
        } yield Response.text(result.toString)
      }.sandbox

  private val viaMxparser =
    Method.POST / "mxparser" ->
      handler { (req: Request) =>
        for {
          expr <- req.body.asString
          result = org.mariuszgromada.math.mxparser.Expression(expr).calculate()
        } yield Response.text(result.toString)
      }.sandbox

  private val viaExprtk =
    Method.POST / "exprtk" ->
      handler { (req: Request) =>
        for {
          expr <- req.body.asString
          result = JavaExprtkAdapter.calculate(expr)
        } yield Response.text(result.toString)
      }.sandbox

  val routes = zio.http.Routes(
    welcome,
    viaEvalex,
    viaMxparser,
    viaExprtk
  )
}
