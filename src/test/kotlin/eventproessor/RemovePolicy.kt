package eventproessor

import createAccount
import createTestEnv
import environment.generateUUID
import environment.globalEnv
import events.PolicyEvent
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RemovePolicy {
    private val participantID = "1"
    private val testAccount = createAccount().account!!

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
    }

    @Test
    fun `Successfully removing policy`() {
        val testEvent = PolicyEvent.RemovePolicyEvent(
            clientNumber = participantID,
            createdBy = testAccount,
            id = generateUUID(),
            createdAt = globalEnv.clock.instant(),
            aggregateID = participantID,
            appliesAt = globalEnv.clock.instant()
        )

        globalEnv.eventProcessor.process(testEvent)

        val actual = globalEnv.inMemoryDatabase.getParticipantNullable(participantID)
        val expected = null

        assertEquals(expected, actual)
    }
}