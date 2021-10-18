package eventproessor

import createAccount
import createTestEnv
import environment.generateUUID
import environment.globalEnv
import events.PolicyEvent
import models.FinalParticipant
import models.Gender
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ChangeGenderEvent {

    private val participantID = "1"
    private val testAccount = createAccount().account!!

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
    }

    @Test
    fun `Change Gender`() {
        val testEvent = PolicyEvent.UpdatedGenderEvent(
            clientNumber = participantID,
            gender = Gender.Man,
            aggregateID = participantID,
            id = generateUUID(),
            createdBy = testAccount,
            appliesAt = globalEnv.clock.instant(),
            createdAt = globalEnv.clock.instant()
        )
        globalEnv.eventProcessor.process(testEvent)

        val actual = globalEnv.inMemoryDatabase.getParticipant(participantID)
        val expected = FinalParticipant(
            id = participantID,
            gender = Gender.Man,
            birthDate = actual.birthDate,
            ingOp = actual.ingOp,
            op = actual.op
        )

        assertEquals(expected, actual)
    }
}