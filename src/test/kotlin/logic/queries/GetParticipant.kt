package logic.queries

import createAccount
import createTestEnv
import environment.globalEnv
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import queries.QueriesImplementation
import java.lang.Exception
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetParticipant {

    private val participantID = "1"
    private val testAccount = createAccount().account

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
        globalEnv.inMemoryDatabase.migration()
    }

    @Test
    fun `Test not unauthenticated`() {
        val query = QueriesImplementation()
        val error = assertFailsWith<Exception> {
            query.getParticipant(null, participantID, null)
        }

        assertEquals(expected = "Unauthenticated", actual = error.message.toString())
    }

    @Test
    fun `Test working get of participant`() {
        val query = QueriesImplementation()
        val participantActual = query.getParticipant(testAccount, participantID, null)
        val participantExpected = createTestEnv().inMemoryDatabase.participants.get(participantID)

        assertEquals(participantExpected, participantActual)
    }
}