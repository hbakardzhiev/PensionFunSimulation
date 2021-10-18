package logic.queries

import createAccount
import createTestEnv
import environment.globalEnv
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import queries.QueriesImplementation
import kotlin.test.assertEquals

class GetMortalityRateTest {

    private val participantID = "1"
    private val testAccount = createAccount().account

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
        globalEnv.inMemoryDatabase.migration()
    }

    @Test
    fun `Test working mortality`() {
        val query = QueriesImplementation()
        val mortalityActual = query.getMortalityRate(testAccount, 2, 5)

        assertEquals(5.0, mortalityActual)
    }
}