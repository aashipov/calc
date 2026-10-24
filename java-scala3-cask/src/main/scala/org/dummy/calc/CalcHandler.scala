package org.dummy.calc

object CalcHandler {
  val WELCOME: String =
    "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)"
  val NAN: String = "NaN"
}

case class CalcHandler()(implicit cc: castor.Context, log: cask.Logger)
  extends cask.Routes {
  org.mariuszgromada.math.mxparser.License.iConfirmNonCommercialUse("dummy")

  @cask.get("/")
  def welcome(): String = {
    CalcHandler.WELCOME
  }

  @cask.post("/")
  def evalex(request: cask.Request): String = {
    val expr: String = request.text()
    JavaExprAdapterManaged.calculateViaEvalEx(expr).toString
  }

  @cask.post("/" + JavaExprAdapter.EVALEX)
  def evalexEvalex(request: cask.Request): String = {
    val expr: String = request.text()
    JavaExprAdapterManaged.calculateViaEvalEx(expr).toString
  }

  @cask.post("/" + JavaExprAdapter.MXPARSER)
  def mxparser(request: cask.Request): String = {
    val expr: String = request.text()
    JavaExprAdapterManaged.calculateViamXparser(expr).toString
  }

  @cask.post("/" + JavaExprAdapter.EXPRTK)
  def exprtk(request: cask.Request): String = {
    val expr: String = request.text()
    JavaExprAdapter.calculateViaExprtk(expr).toString
  }

  @cask.post("/" + JavaExprAdapter.MEVAL)
  def meval(request: cask.Request): String = {
    val expr: String = request.text()
    JavaExprAdapter.calculateViaMeval(expr).toString
  }

  initialize()
}
