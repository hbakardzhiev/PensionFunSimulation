package graphql.commands

import createAccount
import createTestEnv
import environment.globalEnv
import graphql.StubQueries
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue
import models.Account
import models.Context
import org.junit.jupiter.api.BeforeEach
import performQuery
import testAccount
import kotlin.test.assertEquals

internal class DeleteParticipantTest {

    private val participantID = "1"
    private val testAccount = createAccount().account

    class QueriesForTest : StubQueries() {
        override fun deleteParticipant(viewer: Account?, id: String, context: Context): String {

            assertEquals(testAccount, viewer)
            assertEquals("1", id)
            assertEquals(Context(testAccount), context)

            return super.deleteParticipant(viewer, id, context)
        }
    }

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
    }

    @Test
    fun `Test unauthenticated`() {
        val schema = schema.schema()
        val mutation = """
            mutation {
                removeParticipant(id:"$participantID")
            }
        """.trimIndent()

        assertTrue { schema.performQuery(mutation, viewer = Context(null)).errors.size != 0 }
    }

    @Test
    fun `Delete participant`() {
        val schema = schema.schema(QueriesForTest())
        val mutation = """
                mutation {
                    removeParticipant(id: "$participantID")
                }
                """.trimIndent()
        assertTrue { schema.performQuery(mutation).errors.size == 0 }
    }

}