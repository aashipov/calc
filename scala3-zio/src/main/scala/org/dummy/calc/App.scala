package org.dummy.calc

import com.udojava.evalex.Expression
import zio.*
import zio.http.*

object App extends ZIOAppDefault {

  override def run: ZIO[ZIOAppArgs & Scope, Any, Any] =
    Server.serve(CalcHandler.routes).provide(Server.default)
}
