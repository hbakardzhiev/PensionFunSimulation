package migration

import environment.generateUUID
import environment.globalEnv
import events.PolicyEvent
import models.FinalParticipant
import testAccount
import java.time.Instant
import java.time.LocalDate

fun initialMigration(participants: Map<String, FinalParticipant>) {
    participants.forEach { (id, participant) ->
        val createdPolicy = PolicyEvent.CreatedPolicyEvent(
            clientNumber = id,
            birthdate = LocalDate.parse(participant.birthDate),
            gender = participant.gender,
            appliesAt = Instant.now(),
            createdAt = Instant.now(),
            aggregateID = id,
            id = generateUUID(),
            createdBy = testAccount,
            op = participant.op ?: 0.0,
            ingOp = participant.ingOp ?: 0.0
        )

        globalEnv.eventProcessor.process(createdPolicy)
    }
}