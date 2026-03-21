package org.dummy.calc

import org.dummy.calc.CalcHandler
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// With Loom
// java --add-opens java.base/java.lang=ALL-UNNAMED -Dcask.virtual-threads.enabled=true -jar target/calc-shaded.jar
object App extends cask.MainRoutes {

  def buildExecutor(): ExecutorService = {
    val capacity: Int = Math.max(2, Runtime.getRuntime().availableProcessors())
    Executors.newFixedThreadPool(capacity)
  }

  override protected def handlerExecutor(): Option[ExecutorService] = {
    super.handlerExecutor().orElse(Some(buildExecutor()))
  }
  
  override
  val allRoutes: Seq[CalcHandler] = Seq(CalcHandler())
}
