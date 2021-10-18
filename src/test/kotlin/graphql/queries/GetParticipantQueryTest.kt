package graphql.queries

import assertk.assertThat
import createAccount
import createTestEnv
import environment.globalEnv
import graphql.StubQueries
import hasGraphQLResult
import models.Account
import models.Context
import models.FinalParticipant
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import performQuery
import schema.schema
import testAccount
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetParticipantQueryTest {

    private val participantID = "1"
    private val participantQuery = "getParticipant(id: \"$participantID\") { birthDate }"

    class QueriesForTest : StubQueries() {
        override fun getParticipant(viewer: Account?, id: String, timestamp: Instant?): FinalParticipant {

            assertEquals("1", id)
            assertEquals(testAccount, viewer)

            return super.getParticipant(viewer, id, timestamp)
        }
    }

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
    }

    @Test
    fun `Get Participant works`() {
        val schema = schema(QueriesForTest())
        val query = """
            query {
                $participantQuery
            }
        """.trimIndent()
        val result = schema.performQuery(query)

        assertThat(result).hasGraphQLResult(
            mapOf(
                "getParticipant" to mapOf(
                    "birthDate" to "2020-01-01"
                )
            )
        )
    }

    @Test
    fun `Test Unauthenticated`() {
        val schema = schema()
        val query = """
            query {
                $participantQuery
            }
        """.trimIndent()
        assertTrue { schema.performQuery(query, viewer = Context(null)).errors.size != 0 }
    }


}