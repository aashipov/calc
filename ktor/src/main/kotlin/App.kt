package org.dummy.calc

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    org.mariuszgromada.math.mxparser.License.iConfirmNonCommercialUse("dummy")
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::routingTable)
        .start(wait = true)
}
