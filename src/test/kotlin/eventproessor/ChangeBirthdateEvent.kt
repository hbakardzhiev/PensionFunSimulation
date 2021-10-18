package eventproessor

import createAccount
import createTestEnv
import environment.generateUUID
import environment.globalEnv
import events.PolicyEvent
import models.FinalParticipant
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class ChangeBirthdateEvent {

    private val participantID = "1"
    private val testAccount = createAccount().account!!

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
    }

    @Test
    fun `Change Birthdate`() {
        val testEvent = PolicyEvent.UpdatedBirthdateEvent(
            clientNumber = participantID,
            aggregateID = participantID,
            createdAt = globalEnv.clock.instant(),
            appliesAt = globalEnv.clock.instant(),
            createdBy = testAccount,
            newBirthdate = LocalDate.now(),
            id = generateUUID()
        )

        globalEnv.eventProcessor.process(testEvent)

        val actual = globalEnv.inMemoryDatabase.getParticipant(participantID)
        val expected = FinalParticipant(
            id = participantID,
            birthDate = LocalDate.now().toString(),
            ingOp = actual.ingOp,
            op = actual.op,
            gender = actual.gender
        )

        assertEquals(expected, actual)
    }
}