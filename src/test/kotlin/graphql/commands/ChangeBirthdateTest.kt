package graphql.commands

import assertk.assertThat
import commands.Command
import createAccount
import createTestEnv
import environment.globalEnv
import graphql.StubMutation
import hasGraphQLResult
import models.Account
import models.Context
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import performQuery
import testAccount
import java.time.Instant
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ChangeBirthdateTest {

    private val participantID = "1"
    private val testAccount = createAccount().account
    private val date = LocalDate.now()

    class MutationForTest: StubMutation() {

        override fun changeBirthdateCommand(viewer: Account?, clientID: String, birthdate: LocalDate, timestamp: Instant?): Command {

            assertEquals(expected = testAccount, actual = viewer)
            assertEquals("1", clientID)
            assertEquals(LocalDate.now(), birthdate)

            return super.changeBirthdateCommand(viewer, clientID, birthdate, timestamp)
        }
    }

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
    }

    @Test
    fun `Test working date change`() {
        val schema = schema.schema(mutations = MutationForTest())
        val mutation = """
            mutation {
                changeBirthdateCommand(id: "$participantID", date: "$date")
            }
        """.trimIndent()

        assertThat(schema.performQuery(mutation)).hasGraphQLResult(mapOf("changeBirthdateCommand" to date.toString()))
    }

    @Test
    fun `Test Unauthenticated`() {
        val schema = schema.schema()
        val mutation = """
            mutation {
                changeBirthdateCommand(id: "$participantID", date: "$date")
            }
        """.trimIndent()

        assertTrue { schema.performQuery(mutation, viewer = Context(null)).errors.size != 0 }
    }
}