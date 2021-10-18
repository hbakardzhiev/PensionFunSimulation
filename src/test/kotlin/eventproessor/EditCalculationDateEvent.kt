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

class EditCalculationDateEvent {

    private val participantID = "1"
    private val testAccount = createAccount().account!!

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
    }

    @Test
    fun `Change calculation date`() {
        val testEvent = PolicyEvent.EditCalculationDateEvent(
            clientNumber = participantID,
            newDate = LocalDate.now(),
            id = generateUUID(),
            createdAt = globalEnv.clock.instant(),
            appliesAt = globalEnv.clock.instant(),
            createdBy = testAccount,
            aggregateID = participantID
        )

        globalEnv.eventProcessor.process(testEvent)

        val actual = globalEnv.inMemoryDatabase.getParticipant(participantID)
        val expected = FinalParticipant(
            id = participantID,
            gender = actual.gender,
            birthDate = actual.birthDate,
            startDate = LocalDate.now().toString(),
            op = actual.op,
            ingOp = actual.ingOp
        )

        assertEquals(expected, actual)
    }
}