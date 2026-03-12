package org.dummy.calc

import zio.http.Method
import zio.http.Response
import zio.http.Request
import zio.http.handler

object CalcHandler {

  org.mariuszgromada.math.mxparser.License.iConfirmNonCommercialUse("dummy")

  private val WELCOME =
    "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)"

  val welcome =
    Method.GET / "" -> handler(Response.text(WELCOME))

  val viaEvalex =
    Method.POST / "" ->
      handler { (req: Request) =>
        for {
          expr <- req.body.asString
          result = com.udojava.evalex.Expression(expr).eval()
        } yield Response.text(result.toString)
      }.sandbox

  val viaMxparser =
    Method.POST / "mxparser" ->
      handler { (req: Request) =>
        for {
          expr <- req.body.asString
          result = org.mariuszgromada.math.mxparser.Expression(expr).calculate()
        } yield Response.text(result.toString)
      }.sandbox

  val viaExprtk =
    Method.POST / "exprtk" ->
      handler { (req: Request) =>
        for {
          expr <- req.body.asString
          result = JavaExprtkAdapter.calculate(expr)
        } yield Response.text(result.toString)
      }.sandbox

}
