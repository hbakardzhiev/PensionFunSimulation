package logic.mutations

import commands.EditCalculationDateCommand
import createAccount
import createTestEnv
import environment.globalEnv
import models.FinalParticipant
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class EditCalculateTest {

    private val participantID = "1"
    private val testAccount = createAccount().account

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
        globalEnv.inMemoryDatabase.migration()
    }

    @Test
    fun `Successfully edit date`() {
        EditCalculationDateCommand(testAccount, participantID, LocalDate.now()).execute()

        val actual = globalEnv.inMemoryDatabase.getParticipant(participantID)
        val expected = FinalParticipant(
            id = participantID,
            birthDate = actual.birthDate,
            ingOp = actual.ingOp,
            gender = actual.gender,
            op = actual.op,
            startDate = LocalDate.now().toString()
        )

        assertEquals(expected, actual)
    }

}