package graphql.commands

import createTestEnv
import environment.globalEnv
import graphql.StubQueries
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import performQuery
import schema.schema
import models.*
import kotlin.test.assertTrue

internal class UpdateAssumptionsTest {
    private val updateMutation =
        "updateAssumptions(age:5, month: 5, date:\"2020-05-01\", pensionYear:60, pensionMonth:5, sex:\"Man\")"

    class QueriesForTest : StubQueries() {
        override fun updateAssumptions(viewer: Account?, assumptions: Assumptions, context: Context): Assumptions {
            return super.updateAssumptions(viewer, assumptions, context)
        }
    }

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
        globalEnv.inMemoryDatabase.migration()
    }

    @Test
    fun `Test Unauthenticated`() {
        val schema = schema(QueriesForTest())
        val query = """
            mutation {
                $updateMutation
            }
        """.trimIndent()
        assertTrue { schema.performQuery(query, viewer = Context(null)).errors.size != 0 }
    }

    @Test
    fun `Update assumptions`() {
        val schema = schema.schema(QueriesForTest())
        val mutation = """
            mutation {
                $updateMutation
            }
        """.trimIndent()
        assertTrue { schema.performQuery(mutation).errors.size == 0 }
    }

}