package org.dummy.calc

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.test.dispatcher.*
import io.ktor.utils.io.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(InternalAPI::class)
class AppTest {

    companion object {

        const val EXPRESSION: String = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2"
        const val EVALEX_EXPECTED: String = "19.9884"
        const val MXPARSER_EXPECTED: String = "19.98843289048526"
        const val NOT_AN_EXPRESSION: String = "abc"
        const val EXPRTK_EXPECTED: String = "19.988432890485228"

        var testApp: TestApplication = TestApplication {
            application {
                calcModule()
            }
        }
    }

    @Test
    fun welcomeTest() = testSuspend {
        val response = testApp.client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(WELCOME, response.bodyAsText())
    }

    @Test
    fun evalexTest() = testSuspend {
        val response = testApp.client.post("/") { body = EXPRESSION }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(EVALEX_EXPECTED, response.bodyAsText())
    }

    @Test
    fun evalexNotAnExpressionTest() = testSuspend {
        val response = testApp.client.post("/") { body = NOT_AN_EXPRESSION }
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains(NOT_AN_EXPRESSION))
    }

    @Test
    fun mxparserTest() = testSuspend {
        val response = testApp.client.post(MXPARSER_PATH) { body = EXPRESSION }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(MXPARSER_EXPECTED, response.bodyAsText())
    }

    @Test
    fun mxparserNotAnExpressionTest() = testSuspend {
        val response = testApp.client.post(MXPARSER_PATH) { body = NOT_AN_EXPRESSION }
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("NaN"))
    }

    @Test
    fun exprtkTest() = testSuspend {
        val response = testApp.client.post(EXPRTK_PATH) { body = EXPRESSION }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(EXPRTK_EXPECTED, response.bodyAsText())
    }

    @Test
    fun exprtkNotAnExpressionTest() = testSuspend {
        val response = testApp.client.post(EXPRTK_PATH) { body = NOT_AN_EXPRESSION }
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("NaN"))
    }
}
