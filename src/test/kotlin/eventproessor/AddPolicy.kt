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
import java.time.LocalDate
import kotlin.test.assertEquals

class AddPolicy {

    private val participantID = "1"
    private val testAccount = createAccount().account!!

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
    }

    @Test
    fun `Successfully add policy to DB`() {
        val testEvent = PolicyEvent.CreatedPolicyEvent(
            clientNumber = participantID,
            id = generateUUID(),
            gender = Gender.Vrouw,
            birthdate = LocalDate.now(),
            appliesAt = globalEnv.clock.instant(),
            createdBy = testAccount,
            createdAt = globalEnv.clock.instant(),
            aggregateID = participantID,
            ingOp = 0.0,
            op = 0.0
        )
        globalEnv.eventProcessor.process(testEvent)

        val actual = globalEnv.inMemoryDatabase.getParticipant(participantID)
        val expected = FinalParticipant(
            participantID,
            gender = Gender.Vrouw,
            ingOp = 0.0,
            op = 0.0,
            birthDate = LocalDate.now().toString()
        )

        assertEquals(expected, actual)
    }
}