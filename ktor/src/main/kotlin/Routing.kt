package org.dummy.calc

import com.udojava.evalex.Expression
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val WELCOME = "Welcome to calc service\nHTTP POST your expression / (via evalex) or /mxparser (via mxparser)"

fun Application.routingTable() {
    routing {
        get("{...}") {
            call.respondText(WELCOME)
        }
        post("/") {
            val expr: String = call.receiveText()
            val result: String = (Expression(expr).eval()).toString()
            call.respondText(result)
        }
        post("/mxparser") {
            val expr: String = call.receiveText()
            val result: String = org.mariuszgromada.math.mxparser.Expression(expr).calculate().toString()
            call.respondText(result)
        }
    }
}
