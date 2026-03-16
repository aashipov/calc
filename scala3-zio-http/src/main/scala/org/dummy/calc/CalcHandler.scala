package org.dummy.calc

import zio.http.*

object CalcHandler {

  org.mariuszgromada.math.mxparser.License.iConfirmNonCommercialUse("dummy")

  val WELCOME: String =
    "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)"
  val MXPARSER_PATH: String = "mxparser"
  val EXPRTK_PATH: String = "exprtk"

  private val welcome: Route[Any, Nothing] =
    Method.GET / zio.http.trailing -> handler(Response.text(WELCOME))

  private val viaEvalex: Route[Any, Nothing] =
    Method.POST / zio.http.trailing ->
      handler { (req: Request) =>
        for {
          expr <- req.body.asString
          result = com.udojava.evalex.Expression(expr).eval()
        } yield Response.text(result.toString)
      }.sandbox

  private val viaMxparser: Route[Any, Nothing] =
    Method.POST / MXPARSER_PATH ->
      handler { (req: Request) =>
        for {
          expr <- req.body.asString
          result = org.mariuszgromada.math.mxparser.Expression(expr).calculate()
        } yield Response.text(result.toString)
      }.sandbox

  private val viaExprtk: Route[Any, Nothing] =
    Method.POST / EXPRTK_PATH ->
      handler { (req: Request) =>
        for {
          expr <- req.body.asString
          result = JavaExprtkAdapter.calculate(expr)
        } yield Response.text(result.toString)
      }.sandbox

  val routes: Routes[Any, Nothing] = zio.http.Routes(
    welcome,
    viaEvalex,
    viaMxparser,
    viaExprtk
  )
}
