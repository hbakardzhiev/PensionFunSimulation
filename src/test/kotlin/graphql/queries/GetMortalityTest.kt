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
import kotlin.test.assertTrue


internal class GetMortalityTest {
    private val queryMortality = "getMortality(age:2, year:5)"
    private val result = "{\"data\": { \"getMortality\": 5.0 } }"


    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
    }

    class QueriesForTest : StubQueries() {
        override fun getMortalityRate(viewer: Account?, age: Int, year: Int): Double {
            return super.getMortalityRate(viewer, age, year)
        }
    }


    @Test
    fun `Mortality test`() {
        val schema = schema.schema(QueriesForTest())
        val mutation = "query { getMortality(age:2, year:5) }"
        assertThat(schema.performQuery(mutation, viewer = createAccount())).hasGraphQLResult(mapOf("getMortality" to 5.0))
    }

    @Test
    fun `Test Unauthenticated`() {
        val schema = schema.schema(QueriesForTest())
        val mutation = """
            query {
                $queryMortality
            }
        """.trimIndent()

        assertTrue { schema.performQuery(mutation, viewer = Context(null)).errors.size != 0 }
    }
}