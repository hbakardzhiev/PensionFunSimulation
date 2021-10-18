package graphql.queries

import assertk.assertThat
import createAccount
import createTestEnv
import environment.globalEnv
import graphql.StubQueries
import hasGraphQLResult
import models.Account
import models.Context
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import performQuery
import schema.schema
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class InterestRateTest {

    private val ageExpected = 1
    private val queryInterest = "getInterestRate(age: $ageExpected)"
    private val testAccount = createAccount().account

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
    }

    inner class QueriesForTest : StubQueries() {
        override fun getInterest(viewer: Account?, age: Int): Double {

            assertEquals(testAccount, viewer)
            assertEquals(ageExpected, age)

            return super.getInterest(viewer, age)
        }
    }

    @Test
    fun `Get interest`() {
        val schema = schema.schema(QueriesForTest())
        val mutation = "query { $queryInterest }"
        val result = schema.performQuery(mutation)

        assertThat(result).hasGraphQLResult(mapOf("getInterestRate" to 0.03))
    }

    @Test
    fun `Test Unauthenticated`() {
        val schema = schema()
        val query = """
            query {
                $queryInterest
            }
        """.trimIndent()

        assertTrue { schema.performQuery(query, viewer = Context(null)).errors.size != 0 }
    }
}