package org.dummy.calc

object CalcHandler {
  val WELCOME: String =
    "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)"
  val MXPARSER_PATH: String = "mxparser"
  val EXPRTK_PATH: String = "exprtk"
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
  def viaEvalex(request: cask.Request): String = {
    val expr: String = request.text()
    try {
      com.udojava.evalex.Expression(expr).eval().toString
    } catch {
      case ex: Exception => CalcHandler.NAN
    }
  }

  @cask.post("/" + CalcHandler.MXPARSER_PATH)
  def viaMxparser(request: cask.Request): String = {
    val expr: String = request.text()
    org.mariuszgromada.math.mxparser.Expression(expr).calculate().toString
  }

  @cask.post("/" + CalcHandler.EXPRTK_PATH)
  def viaExprtk(request: cask.Request): String = {
    val expr: String = request.text()
    JavaExprtkAdapter.calculate(expr).toString
  }

  initialize()
}
