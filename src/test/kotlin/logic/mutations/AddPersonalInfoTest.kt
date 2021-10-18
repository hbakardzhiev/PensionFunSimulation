package logic.mutations

import commands.AddPersonalInfoCommand
import createAccount
import createTestEnv
import environment.globalEnv
import models.FinalParticipant
import models.PersonalInfoInput
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AddPersonalInfoTest {

    private val participantID = "1"
    private val testAccount = createAccount().account
    private val input = PersonalInfoInput(participantID, true, 10)


    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
        globalEnv.inMemoryDatabase.migration()
    }

    @Test
    fun `Add Personal Info Command Works`() {
        AddPersonalInfoCommand(testAccount, input).execute()

        val actual = globalEnv.inMemoryDatabase.getParticipant(participantID)
        val expected = FinalParticipant(
            id = participantID,
            gender = actual.gender,
            employed = true,
            salary = 10,
            birthDate = actual.birthDate,
            ingOp = actual.ingOp,
            op = actual.op
        )

        assertEquals(expected, actual)
    }
}