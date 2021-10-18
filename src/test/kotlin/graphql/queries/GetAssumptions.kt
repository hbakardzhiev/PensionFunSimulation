package graphql.queries

import assertk.assertThat
import createAccount
import createTestEnv
import environment.globalEnv
import graphql.StubQueries
import hasGraphQLResult
import models.Account
import models.Assumptions
import models.Context
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import performQuery
import testAccount
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetAssumptions {

    private val queryAssumption = "getAssumptions { date }"
    private val testAccount = createAccount().account

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
        globalEnv.inMemoryDatabase.migration()
    }

    class QueriesForTest : StubQueries() {
        override fun getAssumptions(viewer: Account?): Assumptions {

            assertEquals(testAccount, viewer)

            return super.getAssumptions(viewer)
        }
    }

    @Test
    fun `Get Assumption`() {
        val schema = schema.schema(QueriesForTest())
        val fullQuery = "query { $queryAssumption }"
        val result = schema.performQuery(fullQuery)

        assertThat(result).hasGraphQLResult(mapOf("getAssumptions" to mapOf("date" to "2020-01-01")))
    }

    @Test
    fun `Test Unauthenticated`() {
        val schema = schema.schema()
        val mutation = """
            query {
                $queryAssumption
            }
        """.trimIndent()

        assertTrue { schema.performQuery(mutation, viewer = Context(null)).errors.size != 0 }
    }
}