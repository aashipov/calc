package org.dummy.calc

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val WELCOME = "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)"

fun calcRoutingConfiguration(): (Routing.() -> Unit) =
    {
        get("{...}") {
            call.respondText(WELCOME)
        }
        post("{...}") {
            val expr: String = call.receiveText()
            val path: String = call.request.path()
            call.respondText(JavaExprAdapter.calculate(expr, path).toString())
        }
    }

fun Application.calcModule(): RoutingRoot {
    return routing(calcRoutingConfiguration())
}
