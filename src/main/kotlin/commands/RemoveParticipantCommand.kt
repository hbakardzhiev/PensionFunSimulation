package commands

import environment.generateUUID
import environment.globalEnv
import events.PolicyEvent
import models.Account
import java.time.Instant

class RemoveParticipantCommand(val context: Account?, val id: String ): Command {
    override fun execute() {
        if (context == null) {
            throw Exception("Unauthenticated")
        }
        val event = PolicyEvent.RemovePolicyEvent(
            clientNumber = id,
            id = generateUUID(),
            createdAt = Instant.now(),
            createdBy = context,
            aggregateID = id,
            appliesAt = Instant.now()
        )
        globalEnv.eventProcessor.process(event)
    }
}