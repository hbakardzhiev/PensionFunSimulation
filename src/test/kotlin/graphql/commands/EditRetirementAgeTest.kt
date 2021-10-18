package graphql.commands

import commands.Command
import createAccount
import createTestEnv
import environment.globalEnv
import genID
import graphql.StubMutation
import models.Account
import models.Context
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import performQuery
import testAccount
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EditRetirementAgeTest {

    private val participantID = "1"
    private val testAccount = createAccount().account
    private val retirementAgeExpected = 50

    class MutationsForTest: StubMutation() {
        override fun editRetirementAgeCommand(viewer: Account?, clientID: String, newRetirementAge: Int): Command {

            assertEquals(createAccount().account, viewer)
            assertEquals("1", clientID)
            assertEquals(50, newRetirementAge)

            return super.editRetirementAgeCommand(viewer, clientID, newRetirementAge)
        }
    }

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
        globalEnv.inMemoryDatabase.migration()
    }

    @Test
    fun `Test working retirement age change`() {
        val schema = schema.schema(mutations=MutationsForTest())

        val mutation = """
            mutation {
                editRetirementAgeCommand(id: "$participantID", age: $retirementAgeExpected)
            }
        """.trimIndent()

        assertTrue { schema.performQuery(mutation).errors.size == 0 }
    }

    @Test
    fun `Test Unauthenticated`() {
        val schema = schema.schema()

        val mutation = """
            mutation {
                editRetirementAgeCommand(id: "$participantID", age: $retirementAgeExpected)
            }
        """.trimIndent()

        assertTrue { schema.performQuery(mutation, viewer = Context(null)).errors.size != 0 }
    }
}