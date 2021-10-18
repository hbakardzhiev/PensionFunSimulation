package graphql.commands

import createServer
import createTestEnv
import environment.globalEnv
import io.ktor.http.*
import org.junit.jupiter.api.Test
import io.ktor.server.testing.withTestApplication
import kotlin.test.assertEquals
import org.junit.jupiter.api.BeforeEach
import schema.schema


internal class AuthenticationTest {

    private val viewerQuery = "{ viewer { op } }"
    private val createServer = createServer(schema())

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
    }
    @Test
    fun `Test unauthenticated`() {
        withTestApplication(createServer) {
            with(handleRequest {
                uri = urlString("query" to viewerQuery)
                method = HttpMethod.Get

            }) {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }


    private fun urlString(vararg queryParams: Pair<String, String>): String {
        var route = "/graphql"
        if (queryParams.isNotEmpty()) {
            route +="?${queryParams.toList().formUrlEncode()}"
        }
        return route
    }
}