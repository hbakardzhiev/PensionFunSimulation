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

class AddPartnerTest {
    private val participantID = "12"
    private val testAccount = createAccount().account!!

    @BeforeEach
    fun setUp() {
        globalEnv = createTestEnv()
    }

    @Test
    fun `Successfully adding partner`() {
        val testEvent = PolicyEvent.AddPartnerEvent(
            clientNumber = participantID,
            id = generateUUID(),
            createdAt = globalEnv.clock.instant(),
            appliesAt = globalEnv.clock.instant(),
            createdBy = testAccount,
            aggregateID = participantID,
            birthdatePartner = LocalDate.now(),
            genderPartner = Gender.Vrouw,
            startDate = LocalDate.now()
        )

        globalEnv.eventProcessor.process(testEvent)

        val actual = globalEnv.inMemoryDatabase.getParticipant(participantID)
        val expected = FinalParticipant(
            id = participantID,
            gender = Gender.Vrouw,
            startDate = LocalDate.now().toString(),
            birthDate = LocalDate.now().toString()
        )

        assertEquals(expected, actual)
    }
}