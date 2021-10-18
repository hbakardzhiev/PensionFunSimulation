package logic.mutations

import commands.ChangeBirthdateCommand
import createAccount
import createTestEnv
import environment.globalEnv
import models.FinalParticipant
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class ChangeBirthdateTest {

    private val participantID = "1"
    private val testAccount = createAccount().account

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
        globalEnv.inMemoryDatabase.migration()
    }

    @Test
    fun `Successfully change birthdate`() {
        ChangeBirthdateCommand(testAccount, participantID, LocalDate.now()).execute()

        val actual = globalEnv.inMemoryDatabase.getParticipant(participantID)
        val expected = FinalParticipant(
            id = participantID,
            birthDate = LocalDate.now().toString(),
            op=actual.op,
            ingOp = actual.ingOp,
            gender = actual.gender
        )

        assertEquals(expected, actual)
    }
}