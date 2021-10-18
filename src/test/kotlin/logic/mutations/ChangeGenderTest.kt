package logic.mutations

import commands.ChangeGenderCommand
import createAccount
import createTestEnv
import environment.globalEnv
import models.FinalParticipant
import models.Gender
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ChangeGenderTest {

    private val participantID = "1"
    private val testAccount = createAccount().account

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
        globalEnv.inMemoryDatabase.migration()
    }

    @Test
    fun `Successfully change gender`() {
        ChangeGenderCommand(testAccount, participantID, Gender.Vrouw).execute()

        val actual = globalEnv.inMemoryDatabase.getParticipant(participantID)
        val expected = FinalParticipant(
            id = participantID,
            birthDate = actual.birthDate,
            ingOp = actual.ingOp,
            op = actual.op,
            gender = Gender.Vrouw
        )

        assertEquals(expected, actual)
    }
}