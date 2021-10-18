package graphql.commands

import assertk.assertThat
import commands.Command
import commands.CommandFactoryImplementation
import createAccount
import createTestEnv
import environment.globalEnv
import hasGraphQLResult
import models.Account
import models.Context
import models.PersonalInfoInput
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import performQuery
import testAccount
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AddPersonalInfoTest {

    private val participantID = "1"
    private val expectedInfo = PersonalInfoInput(participantID, true, 10)
    private val testAccount = createAccount().account

    class MutationsForTest(): CommandFactoryImplementation() {
        private val expectedInfo = PersonalInfoInput("1", true, 10)
        override fun addPersonalInfoCommand(
            viewer: Account?,
           personalInfoInput: PersonalInfoInput
        ): Command {

            assertEquals(testAccount, viewer)
            assertEquals(expectedInfo, personalInfoInput)

            return super.addPersonalInfoCommand(viewer, personalInfoInput)
        }
    }

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
        globalEnv.inMemoryDatabase.migration()
    }

    @Test
    fun `Test working addition of personal data`() {
        val schema = schema.schema(mutations = MutationsForTest())

        val mutation = """
            mutation {
                addPersonalInfoCommand(id: "$participantID", employed: ${expectedInfo.employed}, salary: ${expectedInfo.salary})
            }
        """.trimIndent()

        val actual = schema.performQuery(mutation)
        assertThat(actual).hasGraphQLResult(mapOf("addPersonalInfoCommand" to participantID))
    }

    @Test
    fun `Test Unauthenticated`() {
        val schema = schema.schema()

        val mutation = """
            mutation {
                addPersonalInfoCommand(id: "$participantID", employed: ${expectedInfo.employed}, salary: ${expectedInfo.salary})
            }
        """.trimIndent()

        assertTrue { schema.performQuery(mutation, viewer = Context(null)).errors.size != 0 }
    }
}