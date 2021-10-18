package logic.queries

import assumptions
import createAccount
import createTestEnv
import environment.globalEnv
import models.Assumptions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import queries.QueriesImplementation
import kotlin.test.assertEquals

class GetAssumptionsTest {

    private val participantID = "1"
    private val testAccount = createAccount().account

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
        globalEnv.inMemoryDatabase.migration()
    }

    @Test
    fun `Test working get assumptions`() {
        val query = QueriesImplementation()
        val actual = query.getAssumptions(testAccount)
        val expected = Assumptions(
            assumptions.age,
            assumptions.month,
            assumptions.date.toString(),
            assumptions.maleBeforeStart,
            assumptions.maleAfterStart,
            assumptions.femaleBeforeStart,
            assumptions.femaleAfterStart,
            assumptions.pensionYear,
            assumptions.pensionMonth,
            assumptions.sex
        )


        assertEquals(expected, actual)
    }
}