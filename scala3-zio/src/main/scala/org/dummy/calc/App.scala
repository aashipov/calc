package org.dummy.calc

import com.udojava.evalex.Expression
import zio.*
import zio.http.*

object App extends ZIOAppDefault {
  
  private val app = Routes(
    CalcHandler.welcome,
    CalcHandler.viaEvalex,
    CalcHandler.viaMxparser,
    CalcHandler.viaExprtk
  )

  override def run: ZIO[ZIOAppArgs & Scope, Any, Any] =
    Server.serve(app).provide(Server.default)
}
