package graphql.commands

import assertk.assertThat
import commands.Command
import createAccount
import createTestEnv
import environment.globalEnv
import graphql.StubMutation
import hasGraphQLResult
import models.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import performQuery
import testAccount
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AddPartnerTest {

    private val participantID = "1"
    private val expectedInput = InputParticipant(participantID, LocalDate.now(), Gender.Vrouw)

    class MutationForTest: StubMutation() {
        private val expectedInput = InputParticipant("1", LocalDate.now(), Gender.Vrouw)
        override fun addPartnerCommand(
            viewer: Account?,
            inputParticipant: InputParticipant
        ): Command {

            assertEquals(testAccount, viewer)
            assertEquals(expectedInput, inputParticipant)
            return super.addPartnerCommand(viewer, inputParticipant)
        }
    }

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
        globalEnv.inMemoryDatabase.migration()
    }

    @Test
    fun `Test working partner addition`() {
        val schema = schema.schema(mutations = MutationForTest())
        val mutation = """
            mutation {
                addPartnerCommand(id: "$participantID", date: "${expectedInput.birthdate}", gender: "${expectedInput.gender}")
            }
        """.trimIndent()

        val actual = schema.performQuery(mutation)
        assertThat(actual).hasGraphQLResult(mapOf("addPartnerCommand" to participantID))
    }

    @Test
    fun `Test Unauthenticated`() {
        val schema = schema.schema()
        val mutation = """
            mutation {
                addPartnerCommand(id: "$participantID", date: "${expectedInput.birthdate}", gender: "${expectedInput.gender}")
            }
        """.trimIndent()

        assertTrue { schema.performQuery(mutation, viewer = Context(null)).errors.size != 0 }
    }
}