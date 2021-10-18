package logic.queries

import createAccount
import createTestEnv
import environment.globalEnv
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import participants
import queries.QueriesImplementation
import kotlin.test.assertEquals

class GetParticipants {

    private val testAccount = createAccount().account

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
        globalEnv.inMemoryDatabase.migration()
    }

    @Test
    fun `Get all participants`() {
        val query = QueriesImplementation()
        val actual = query.getParticipants(testAccount)
        val expected = participants.values.toList()

        assertEquals(expected, actual)
    }
}