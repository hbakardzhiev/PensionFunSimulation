package logic.queries

import createAccount
import createTestEnv
import environment.globalEnv
import models.Gender
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import queries.QueriesImplementation
import kotlin.test.assertEquals

class GetInterest {

    private val testAccount = createAccount().account

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
        globalEnv.inMemoryDatabase.migration()
    }

    @Test
    fun `Get interest working`() {
        val query = QueriesImplementation()
        val actual = query.getInterest(testAccount, 1)
        val expected = 0.03
        assertEquals(expected, actual)
    }
}