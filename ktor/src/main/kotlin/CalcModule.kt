package org.dummy.calc

import com.udojava.evalex.Expression
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val WELCOME = "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)"

const val MXPARSER_PATH = "/mxparser"

const val EXPRTK_PATH = "/exprtk"

fun calcRoutingConfiguration(): (Routing.() -> Unit) =
    {
        get("{...}") {
            call.respondText(WELCOME)
        }
        post("/") {
            val expr: String = call.receiveText()
            try {
                call.respondText((Expression(expr).eval()).toString())
            } catch (e: Exception) {
                e.message?.let { call.respondText(it) }
            }
        }
        post(MXPARSER_PATH) {
            val expr: String = call.receiveText()
            try {
                call.respondText(org.mariuszgromada.math.mxparser.Expression(expr).calculate().toString())
            } catch (e: Exception) {
                e.message?.let { call.respondText(it) }
            }
        }
        post(EXPRTK_PATH) {
            val expr: String = call.receiveText()
            try {
                call.respondText("" + JavaExprtkAdapter.calculate(expr))
            } catch (e: Exception) {
                e.message?.let { call.respondText(it) }
            }
        }
    }

fun Application.calcModule(): RoutingRoot {
    org.mariuszgromada.math.mxparser.License.iConfirmNonCommercialUse("dummy")
    return routing(calcRoutingConfiguration())
}
