package logic.mutations

import commands.AddPartnerCommand
import createAccount
import createTestEnv
import environment.globalEnv
import models.FinalParticipant
import models.Gender
import models.InputParticipant
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class AddPartnerTest {
    private val participantID = "3"
    private val testAccount = createAccount().account
    private val input = InputParticipant(participantID, LocalDate.now(), Gender.Vrouw)


    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
        globalEnv.inMemoryDatabase.migration()
    }

    @Test
    fun `Add Partner successfully`() {
        AddPartnerCommand(testAccount, input).execute()

        val actual = globalEnv.inMemoryDatabase.getParticipant(participantID)
        val expected = FinalParticipant(
            id = participantID,
            ingOp = actual.ingOp,
            birthDate = LocalDate.now().toString(),
            op = actual.op,
            gender = Gender.Vrouw
        )

        assertEquals(expected, actual)
    }
}