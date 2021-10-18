package eventproessor

import createAccount
import createTestEnv
import environment.generateUUID
import environment.globalEnv
import events.PolicyEvent
import models.FinalParticipant
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AddPersonalInfoTest {
    private val participantID = "1"
    private val testAccount = createAccount().account!!

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
    }

    @Test
    fun `Successfully adding info`() {
        val testEvent = PolicyEvent.AddPersonalInfoEvent(
            yearlySalary = 10,
            employed = true,
            clientNumber = participantID,
            createdBy = testAccount,
            id = generateUUID(),
            createdAt = globalEnv.clock.instant(),
            aggregateID = participantID,
            appliesAt = globalEnv.clock.instant()
        )

        globalEnv.eventProcessor.process(testEvent)

        val actual = globalEnv.inMemoryDatabase.getParticipant(participantID)
        val expected = FinalParticipant(
            id = participantID,
            ingOp = actual.ingOp,
            op = actual.op,
            salary = 10,
            employed = true,
            gender = actual.gender,
            birthDate = actual.birthDate
        )

        assertEquals(expected, actual)
    }
}
