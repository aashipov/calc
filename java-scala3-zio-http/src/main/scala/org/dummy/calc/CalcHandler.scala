package org.dummy.calc

import zio.http.*
import zio.ZIO

object CalcHandler {

  org.mariuszgromada.math.mxparser.License.iConfirmNonCommercialUse("dummy")

  private val WELCOME: String =
    "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)"
  private val NAN: String = "NaN"

  private val welcome: Route[Any, Nothing] =
    Method.GET / zio.http.trailing -> handler(Response.text(WELCOME))

  private val evalex: Route[Any, Nothing] =
    Method.POST / zio.http.trailing ->
      handler { (req: Request) =>
        for {
          expr <- req.body.asString
          result <- ZIO
            .attempt(JavaExprAdapterManaged.calculateViaEvalEx(expr))
            .catchAll(_ => ZIO.succeed(NAN))
        } yield Response.text(result.toString)
      }.sandbox

  private val evalexEvalex: Route[Any, Nothing] =
    Method.POST / JavaExprAdapter.EVALEX ->
      handler { (req: Request) =>
        for {
          expr <- req.body.asString
          result <- ZIO
            .attempt(
              JavaExprAdapterManaged.calculateViaEvalEx(expr)
            )
            .catchAll(_ => ZIO.succeed(NAN))
        } yield Response.text(result.toString)
      }.sandbox

  private val mxparser: Route[Any, Nothing] =
    Method.POST / JavaExprAdapter.MXPARSER ->
      handler { (req: Request) =>
        for {
          expr <- req.body.asString
          result <- ZIO
            .attempt(
              JavaExprAdapterManaged.calculateViamXparser(expr)
            )
            .catchAll(_ => ZIO.succeed(NAN))
        } yield Response.text(result.toString)
      }.sandbox

  private val exprtk: Route[Any, Nothing] =
    Method.POST / JavaExprAdapter.EXPRTK ->
      handler { (req: Request) =>
        for {
          expr <- req.body.asString
          result <- ZIO
            .attempt(
              JavaExprAdapter.calculateViaExprtk(expr)
            )
            .catchAll(_ => ZIO.succeed(NAN))
        } yield Response.text(result.toString)
      }.sandbox

  private val meval: Route[Any, Nothing] =
    Method.POST / JavaExprAdapter.MEVAL ->
      handler { (req: Request) =>
        for {
          expr <- req.body.asString
          result <- ZIO
            .attempt(
              JavaExprAdapter.calculateViaMeval(expr)
            )
            .catchAll(_ => ZIO.succeed(NAN))
        } yield Response.text(result.toString)
      }.sandbox

  val routes: Routes[Any, Nothing] = zio.http.Routes(
    welcome,
    evalex,
    mxparser,
    exprtk,
    meval
  )
}
