package graphql.commands

import assertk.assertThat
import createTestEnv
import environment.globalEnv
import genID
import graphql.StubQueries
import hasGraphQLResult
import models.Account
import models.Context
import models.FinalParticipant
import models.Gender
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import performQuery
import kotlin.test.assertTrue

internal class ChangeGenderTest {
    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
    }

    class QueriesForTest: StubQueries() {
        override fun updateParticipant(
            viewer: Account?,
            id: String,
            gender: Gender,
            birthDate: String,
            context: Context
        ): FinalParticipant {
            return super.updateParticipant(viewer, id, gender, birthDate, context)
        }
    }

    @Test
    fun `Change Gender male to female`() {
        val schema = schema.schema(QueriesForTest())
        val id = genID(schema)
        val mutation = """
            mutation {
                changeGenderCommand(id:"$id", gender:"Vrouw")
            }
        """.trimIndent()
        val result = schema.performQuery(mutation)

        assertThat(result).hasGraphQLResult(mapOf("changeGenderCommand" to  Gender.Vrouw.toString()))
    }



    @Test
    fun `Change Gender female to male`() {
        val schema = schema.schema(QueriesForTest())
        val id = genID(schema)
        val mutation = """
            mutation {
                changeGenderCommand(id:"$id", gender:"Man")
            }
        """.trimIndent()
        val result = schema.performQuery(mutation)

        assertThat(result).hasGraphQLResult(mapOf("changeGenderCommand" to Gender.Man.toString()))
    }


    @Test
    fun `Change Gender with incorrect data`() {
        val schema = schema.schema(QueriesForTest())
        val id = genID(schema)
        val mutation = """
            mutation {
                changeGenderCommand(id:"$id", gender:"749")
            }
        """.trimIndent()
        assertTrue { schema.performQuery(mutation).errors.size != 0 }
    }

    @Test
    fun `Test Unauthorized`() {
        val schema = schema.schema(QueriesForTest())
        val id = genID(schema)
        val mutation = """
            mutation {
                changeGenderCommand(id:"$id", gender:"Man")
            }
        """.trimIndent()

        assertTrue { schema.performQuery(mutation, viewer = Context(null)).errors.size != 0 }
    }
}